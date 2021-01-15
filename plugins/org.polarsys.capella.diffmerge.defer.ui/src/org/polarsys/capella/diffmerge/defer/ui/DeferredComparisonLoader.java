/*******************************************************************************
 * Copyright (c) 2019, 2021 THALES GLOBAL SERVICES.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.diffmerge.defer.ui;

import static org.polarsys.capella.diffmerge.defer.ui.DeferredComparisonLoader.DeferredComparisonUsage.READ_ONLY;
import static org.polarsys.capella.diffmerge.defer.ui.DeferredComparisonLoader.DeferredComparisonUsage.WRITE_IN_EXISTING_SESSION;
import static org.polarsys.capella.diffmerge.defer.ui.DeferredComparisonLoader.DeferredComparisonUsage.WRITE_IN_SESSION;
import static org.polarsys.capella.diffmerge.defer.ui.views.PendingChangesViewUtil.P_DEFERRED_LOADER;
import static org.eclipse.emf.diffmerge.ui.viewers.DefaultUserProperties.P_SHOW_SIDES_POSSIBLE;
import static org.eclipse.emf.diffmerge.ui.viewers.DefaultUserProperties.P_SUPPORT_UNDO_REDO_OPTIONAL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.amalgam.explorer.activity.ui.api.editor.ActivityExplorerEditor;
import org.eclipse.amalgam.explorer.activity.ui.api.editor.input.ActivityExplorerEditorInput;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.diffmerge.api.scopes.IEditableModelScope;
import org.eclipse.emf.diffmerge.api.scopes.IPersistentModelScope;
import org.eclipse.emf.diffmerge.diffdata.EComparison;
import org.eclipse.emf.diffmerge.generic.api.Role;
import org.eclipse.emf.diffmerge.generic.api.config.IComparisonConfiguration;
import org.eclipse.emf.diffmerge.generic.api.scopes.IEditableTreeDataScope;
import org.eclipse.emf.diffmerge.ui.EMFDiffMergeUIPlugin;
import org.eclipse.emf.diffmerge.ui.setup.ComparisonSetupManager;
import org.eclipse.emf.diffmerge.ui.specification.IComparisonMethodFactory;
import org.eclipse.emf.diffmerge.ui.specification.IModelScopeDefinition;
import org.eclipse.emf.diffmerge.ui.specification.IModelScopeDefinitionFactory;
import org.eclipse.emf.diffmerge.ui.util.MiscUtil;
import org.eclipse.emf.diffmerge.ui.viewers.EMFDiffNode;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.osgi.util.NLS;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.polarsys.capella.core.sirius.ui.helper.SessionHelper;

import org.polarsys.capella.diffmerge.defer.diffdeferdata.DeferredComparison;
import org.polarsys.capella.diffmerge.defer.diffdeferdata.DiffdeferdataPackage;
import org.polarsys.capella.diffmerge.defer.ui.messages.Messages;
import org.polarsys.capella.diffmerge.defer.ui.views.PendingChangesViewUtil;

/**
 * A Runnable that loads and sets up a deferred comparison.
 * 
 * @author Olivier Constant
 */
public class DeferredComparisonLoader implements Runnable, IDisposable {

  /**
   * The different usages for a deferred comparison.
   */
  public static enum DeferredComparisonUsage {
    /** Always read-only */
    READ_ONLY,
    /** Write only if an existing session is found */
    WRITE_IN_EXISTING_SESSION,
    /** Write only if a session can be found or opened */
    WRITE_IN_SESSION,
    /** Always read-write */
    WRITE_ALWAYS
  }

  /** Input: The non-null URI of the deferred comparison */
  protected final URI dComparisonURI;

  /** Input: How the deferred comparison must be used */
  protected final DeferredComparisonUsage usage;

  /** Output: The initially null Sirius target session */
  protected Session resultingSession;

  /**
   * Output: The initially null editing domain where the deferred comparison is
   * held
   */
  protected EditingDomain resultingEditingDomain;

  /**
   * Output: Whether the resource set of the target scope is dedicated to this
   * deferred comparison
   */
  protected boolean isDedicatedResourceSet;

  /** Output: The initially null deferred comparison */
  protected DeferredComparison resultingDComparison;

  /** Output: The initially null comparison configuration */
  protected IComparisonConfiguration<EObject> resultingConfiguration;

  /** Output: The initially null diff node */
  protected EMFDiffNode resultingDiffNode;

  /**
   * The session resource URI calculated by {@link #getTargetScopeDefinitionURI}
   */
  protected URI resultingSessionURI;

  /**
   * Constructor
   * 
   * @param dComparisonURI
   *          the URI of the deferred comparison to load
   * @param usage
   *          the intended usage of the deferred comparison
   */
  public DeferredComparisonLoader(URI dComparisonURI,
      DeferredComparisonUsage usage) {
    this.dComparisonURI = dComparisonURI;
    this.usage = usage;
    this.resultingSession = null;
    this.resultingEditingDomain = null;
    this.resultingDComparison = null;
    this.resultingDiffNode = null;
    this.isDedicatedResourceSet = true;
  }

  /**
   * Apply the given configuration to the given comparison
   * 
   * @param config
   *          a non-null configuration
   * @param comparison
   *          a non-null comparison
   */
  protected void applyOn(IComparisonConfiguration<EObject> config,
      EComparison comparison) {
    comparison.setLastMatchPolicy(config.getMatchPolicy());
    comparison.setLastDiffPolicy(config.getDiffPolicy());
    comparison.setLastMergePolicy(config.getMergePolicy());
  }

  /**
   * @see org.eclipse.emf.edit.provider.IDisposable#dispose()
   */
  public void dispose() {
    final MiscUtil.ExtendedUnloader unloader = MiscUtil.ExtendedUnloader
        .getDefault();
    final Set<Resource> unloaded = new HashSet<>();
    EditingDomain domain = resultingEditingDomain;
    MiscUtil.execute(domain,
        Messages.DeferredComparisonLoader_CommandDisposalName, new Runnable() {
          /**
           * @see java.lang.Runnable#run()
           */
          public void run() {
            if (resultingDComparison != null) {
              resultingDComparison.dispose();
              Resource dcResource = resultingDComparison.eResource();
              if (dcResource != null) {
                ResourceSet rs = dcResource.getResourceSet();
                if (rs != null && isDedicatedResourceSet) {
                  // Unload all resources
                  unloaded.addAll(rs.getResources());
                  unloader.unloadResources(unloaded);
                } else {
                  // Unload DComparison resource only
                  unloaded.add(dcResource);
                  unloader.unloadResource(dcResource);
                }
              }
              resultingDComparison = null;
            }
            if (resultingDiffNode != null) {
              resultingDiffNode.dispose();
              resultingDiffNode = null;
            }
            resultingSession = null;
            if (isDedicatedResourceSet
                && resultingEditingDomain instanceof TransactionalEditingDomain) {
              ((TransactionalEditingDomain) resultingEditingDomain).dispose();
            }
            resultingEditingDomain = null;
          }
        }, false);
    unloader.disconnectResources(domain, unloaded);
  }

  /**
   * Return the comparison method factory of the given ID
   * 
   * @param factoryID
   *          a potentially null string
   * @return a potentially null comparison method factory
   */
  @SuppressWarnings("unchecked")
  protected IComparisonMethodFactory<EObject> getComparisonMethodFactory(
      String factoryID) {
    IComparisonMethodFactory<EObject> result = null;
    if (factoryID != null) {
      ComparisonSetupManager manager = EMFDiffMergeUIPlugin.getDefault()
          .getSetupManager();
      result = (IComparisonMethodFactory<EObject>) manager.getComparisonMethodFactory(factoryID);
    }
    return result;
  }

  /**
   * Return a diff node for the given comparison in the context of the given
   * editing domain
   * 
   * @param comparison
   *          a non-null comparison
   * @param domain
   *          an optional editing domain
   * @param readOnly
   *          whether the deferred comparison must remain read-only
   * @return a non-null node
   */
  protected EMFDiffNode getDiffNode(EComparison comparison,
      EditingDomain domain, boolean readOnly) {
    EMFDiffNode node = new EMFDiffNode(comparison, domain, false, !readOnly,
        Role.REFERENCE);
    node.setReferenceRole(Role.TARGET);
    node.setDrivingRole(Role.TARGET);
    node.addUserProperty(P_SHOW_SIDES_POSSIBLE, Boolean.FALSE);
    node.addUserProperty(P_SUPPORT_UNDO_REDO_OPTIONAL, Boolean.FALSE);
    node.addUserProperty(P_DEFERRED_LOADER, this);
    return node;
  }

  /**
   * Return the editing domain that encompasses the given scope, if any
   * 
   * @param scope
   *          a non-null scope
   * @return a potentially null editing domain
   */
  protected EditingDomain getEditingDomain(IEditableTreeDataScope<?> scope) {
    EditingDomain result = null;
    if (scope instanceof IEditingDomainProvider) {
      result = ((IEditingDomainProvider) scope).getEditingDomain();
    }
    if (result == null && scope instanceof IPersistentModelScope) {
      Resource resource = ((IPersistentModelScope) scope).getHoldingResource();
      ResourceSet rs = (resource == null) ? null : resource.getResourceSet();
      result = AdapterFactoryEditingDomain.getEditingDomainFor(rs);
    }
    return result;
  }

  /**
   * Return a resource set for the deferred comparison resource in order to
   * operate on the given optional editing domain
   * 
   * @param domain
   *          an optional editing domain
   * @return a non-null resource set
   */
  protected ResourceSet getResourceSet(EditingDomain domain) {
    ResourceSet result = null;
    if (domain != null) {
      result = domain.getResourceSet();
    } else {
      result = getDefaultResourceSet();
    }
    return result;
  }

  /**
   * Return the comparison configuration resulting from this operation
   * 
   * @return a potentially null object
   */
  public IComparisonConfiguration<EObject> getResultingConfiguration() {
    return resultingConfiguration;
  }

  /**
   * Return the deferred comparison resulting from this operation
   * 
   * @return a potentially null object
   */
  public DeferredComparison getResultingDeferredComparison() {
    return resultingDComparison;
  }

  /**
   * Return the diff node resulting from this operation
   * 
   * @return a potentially null object
   */
  public EMFDiffNode getResultingDiffNode() {
    return resultingDiffNode;
  }

  /**
   * Return the editing domain where the resulting deferred comparison is held
   * 
   * @return a potentially null object
   */
  public EditingDomain getResultingEditingDomain() {
    return resultingEditingDomain;
  }

  /**
   * Return the resource that holds the deferred comparison if any
   * 
   * @return a potentially null object
   */
  public Resource getResultingResource() {
    Resource result = null;
    DeferredComparison dComparison = getResultingDeferredComparison();
    if (dComparison != null) {
      result = dComparison.eResource();
    }
    return result;
  }

  /**
   * Return the target Sirius session of the deferred comparison
   * 
   * @return a potentially null object
   */
  public Session getResultingSession() {
    return resultingSession;
  }

  /**
   * Return the comparison configuration for the given deferred comparison
   * 
   * @param dComparison
   *          a non-null deferred comparison
   * @return a potentially null configuration
   */
  protected IComparisonConfiguration<EObject> getConfiguration(
      DeferredComparison dComparison) {
    IComparisonConfiguration<EObject> result = null;
    String cmFactoryID = dComparison.getComparisonMethodFactoryID();
    IComparisonMethodFactory<EObject> cmFactory = getComparisonMethodFactory(
        cmFactoryID);
    if (cmFactory != null) {
      result = cmFactory.createComparisonConfiguration();
    }
    return result;
  }

  /**
   * Return a default resource set for the deferred comparison resource
   * 
   * @return a non-null resource set
   */
  protected ResourceSet getDefaultResourceSet() {
    return new ResourceSetImpl();
  }

  /**
   * Return the deferred comparison that is contained in the given resource
   * 
   * @param comparisonResource
   *          a potentially null resource
   * @return a potentially null object
   */
  protected DeferredComparison getDeferredComparison(
      Resource comparisonResource) {
    DeferredComparison result = null;
    if (comparisonResource != null && comparisonResource.isLoaded()) {
      Iterator<EObject> it = comparisonResource.getContents().iterator();
      while (result == null && it.hasNext()) {
        EObject root = it.next();
        if (root instanceof DeferredComparison) {
          result = (DeferredComparison) root;
        }
      }
    }
    return result;
  }

  /**
   * Return the deferred comparison located at the given URI, if any
   * 
   * @param dComparisonResourceURI
   *          a non-null URI
   * @param domain
   *          the optional editing domain for the target scope
   * @return a (potentially null) deferred comparison
   */
  protected DeferredComparison getDeferredComparison(URI dComparisonResourceURI,
      EditingDomain domain) {
    ResourceSet dComparisonRS = getResourceSet(domain);
    Resource dComparisonResource = dComparisonRS
        .getResource(dComparisonResourceURI, true);
    DeferredComparison dComparison = getDeferredComparison(dComparisonResource);
    return dComparison;
  }

  /**
   * Return the session for the given URI, if any and if possible
   * 
   * @param uri
   *          a non-null URI of a Sirius resource
   * @param forceOpen
   *          whether the session must be opened if it cannot be found
   * @return a potentially null session
   */
  protected Session getSession(URI uri, boolean forceOpen) {
    SessionManager manager = SessionManager.INSTANCE;
    Session result = manager.getExistingSession(uri);
    if (result != null) {
      // An existing session is being reused so the resource set is not
      // dedicated
      isDedicatedResourceSet = false;
    } else if (forceOpen) {
      result = manager.openSession(uri, new NullProgressMonitor(), null);
    }
    return result;
  }

  /**
   * Return a source scope for the given deferred comparison
   * 
   * @param dComparison
   *          a non-null deferred comparison
   * @return a potentially null scope
   */
  protected IEditableModelScope getSourceScope(DeferredComparison dComparison) {
    IEditableModelScope result = new DeferredComparisonSourceScope(dComparison);
    return result;
  }

  /**
   * Return a target scope for the given URI in the context of the given editing
   * domain
   * 
   * @param targetScopeDefURI
   *          a non-null URI
   * @param domain
   *          an optional editing domain
   * @param readOnly
   *          whether the deferred comparison must remain read-only
   * @return a potentially null scope
   */
  @SuppressWarnings("unchecked")
  protected IEditableTreeDataScope<EObject> getTargetScope(URI targetScopeDefURI,
      EditingDomain domain, boolean readOnly) {
    IEditableTreeDataScope<?> result = null;
    ComparisonSetupManager manager = EMFDiffMergeUIPlugin.getDefault()
        .getSetupManager();
    List<IModelScopeDefinitionFactory> targetScopeFactories = manager
        .getApplicableModelScopeFactories(targetScopeDefURI);
    if (!targetScopeFactories.isEmpty()) {
      IModelScopeDefinitionFactory selectedTargetScopeFactory = targetScopeFactories
          .get(0);
      IModelScopeDefinition targetScopeDef = selectedTargetScopeFactory
          .createScopeDefinition(targetScopeDefURI, null, !readOnly);
      result =  targetScopeDef.createScope(domain);
    }
    return (IEditableTreeDataScope<EObject>) result;
  }

  /**
   * Return the URI of the target scope definition for the deferred comparison
   * located at the given URI without loading the corresponding resource
   * 
   * @param dComparisonResourceURI
   *          a non-null URI
   * @return a potentially null URI
   */
  protected URI getTargetScopeDefinitionURI(URI dComparisonResourceURI) {
    String stringURI = null;
    URIConverter converter = new ExtensibleURIConverterImpl();
    // XMI persistence assumed
    try {
      InputStream stream = converter.createInputStream(dComparisonResourceURI);
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
        final String ATT_NAME = DiffdeferdataPackage.eINSTANCE
            .getDeferredComparison_TargetScopeDefinitionURI().getName();
        int position = -1;
        String nextLine = null;
        while (position == -1 && (nextLine = reader.readLine()) != null) {
          position = nextLine.indexOf(ATT_NAME);
        }
        if (position != -1 && nextLine != null) {
          int start = position + ATT_NAME.length() + 2;
          int end = nextLine.indexOf("\"", start); //$NON-NLS-1$
          stringURI = nextLine.substring(start, end);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return stringURI == null ? null : URI.createURI(stringURI);
  }

  /**
   * 
   * @return The session resource URI calculated by
   *         {@link #getTargetScopeDefinitionURI}
   */
  public URI getResultingSessionURI() {
    if (resultingSessionURI == null) {
      resultingSessionURI = getTargetScopeDefinitionURI(getURI());
    }
    return resultingSessionURI;
  }

  /**
   * Return the URI of the deferred comparison
   * 
   * @return a non-null URI
   */
  public URI getURI() {
    return dComparisonURI;
  }

  /**
   * Return whether the resulting deferred comparison is in read-only mode
   */
  public boolean isReadOnly() {
    return usage == READ_ONLY
        || (usage == WRITE_IN_EXISTING_SESSION || usage == WRITE_IN_SESSION)
            && resultingSession == null;
  }

  /**
   * Return whether this loader has completed successfully
   */
  public boolean isSuccessful() {
    return getResultingDiffNode() != null;
  }

  /**
   * @see java.lang.Runnable#run()
   */
  public void run() {
    URI targetScopeDefinitionURI = getTargetScopeDefinitionURI(dComparisonURI);
    if (targetScopeDefinitionURI != null) {
      if (!PendingChangesViewUtil.getFile(targetScopeDefinitionURI).exists()) {
        String relativeURI = PendingChangesViewUtil
            .getRelativeURI(targetScopeDefinitionURI);
        String targetFile = relativeURI != null ? relativeURI
            : targetScopeDefinitionURI.toString();
        MessageDialog.openError(Display.getCurrent().getActiveShell(),
            Messages.DeferredComparisonLoader_Error,
            NLS.bind(Messages.DeferredComparisonLoader_ErrorMsg, targetFile));
        return;
      }
      // Get target scope
      resultingSession = getSession(targetScopeDefinitionURI,
          usage == WRITE_IN_SESSION);
      if (resultingSession == null
          && MessageDialog.openQuestion(Display.getCurrent().getActiveShell(),
              Messages.DeferredComparisonLoader_OpenSessionTitle,
              Messages.DeferredComparisonLoader_OpenSessionQuestion)) {
        doOpenSession(targetScopeDefinitionURI);
        doOpenActivityExplorer();
      }
      EditingDomain knownDomain = (resultingSession == null) ? null
          : resultingSession.getTransactionalEditingDomain();
      boolean targetScopeReadOnly = isReadOnly();
      IEditableTreeDataScope<EObject> targetScope = getTargetScope(targetScopeDefinitionURI,
          knownDomain, targetScopeReadOnly);
      if (targetScope != null) {
        // Get deferred comparison
        resultingEditingDomain = getEditingDomain(targetScope);
        resultingDComparison = getDeferredComparison(dComparisonURI,
            resultingEditingDomain);
        if (resultingDComparison != null) {
          // Disconnect from resource set
          Resource dcResource = getResultingResource();
          ResourceSet rs = (dcResource == null) ? null
              : dcResource.getResourceSet();
          if (rs != null) {
            rs.getResources().remove(dcResource);
            new ResourceSetImpl().getResources().add(dcResource);
          }
          // Set scopes
          EComparison comparison = resultingDComparison.getActualComparison();
          comparison.setTargetScope(targetScope);
          IEditableModelScope sourceScope = getSourceScope(
              resultingDComparison);
          comparison.setReferenceScope(sourceScope);
          // Apply configuration policies
          resultingConfiguration = getConfiguration(resultingDComparison);
          if (resultingConfiguration != null) {
            applyOn(resultingConfiguration, comparison);
            // Define diff node
            resultingDiffNode = getDiffNode(comparison, resultingEditingDomain,
                targetScopeReadOnly);
            resultingDComparison
                .setTransientData(resultingDiffNode.getUIComparison());
          }
          // Reconnect to resource set
          if (rs != null) {
            rs.getResources().add(dcResource);
          }
        }
      }
    }
  }

  /**
   * 
   * @param targetScopeDefinitionURI
   *          The URI of the file to open the session.
   */
  private void doOpenSession(URI targetScopeDefinitionURI) {
    IRunnableWithProgress runnable = new IRunnableWithProgress() {
      @Override
      public void run(IProgressMonitor monitor) {
        resultingSession = SessionManager.INSTANCE
            .getSession(targetScopeDefinitionURI, monitor);
        if (resultingSession != null) {
          resultingSession.open(monitor);
        }
      }
    };

    try {
      PlatformUI.getWorkbench().getProgressService().run(false, false,
          runnable);
    } catch (InvocationTargetException | InterruptedException e) {
      e.printStackTrace();
    }

    resultingSession = getSession(targetScopeDefinitionURI, false);
  }

  /**
   * Open the activity explorer. Called after opening the session.
   */
  protected void doOpenActivityExplorer() {

    if (resultingSession != null) {
      // Create a runnable that open the Activity Explorer.
      Runnable runnable = new Runnable() {

        @Override
        public void run() {
          try {
            IWorkbenchPage activePage = PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getActivePage();
            if (resultingSession.isOpen()) {
              activePage.openEditor(
                  new ActivityExplorerEditorInput(resultingSession,
                      SessionHelper.getCapellaProject(resultingSession)),
                  ActivityExplorerEditor.ID, true,
                  IWorkbenchPage.MATCH_ID | IWorkbenchPage.MATCH_INPUT);
            }
          } catch (PartInitException exception) {
            exception.printStackTrace();
          }
        }
      };
      PlatformUI.getWorkbench().getDisplay().asyncExec(runnable);
    }
  }
}