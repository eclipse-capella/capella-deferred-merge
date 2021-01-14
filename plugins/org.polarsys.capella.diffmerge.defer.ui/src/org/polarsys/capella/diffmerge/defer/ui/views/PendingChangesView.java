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
package org.polarsys.capella.diffmerge.defer.ui.views;

import static org.polarsys.capella.diffmerge.defer.ui.DeferredComparisonLoader.DeferredComparisonUsage.WRITE_IN_EXISTING_SESSION;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.diffmerge.diffdata.EMatch;
import org.eclipse.emf.diffmerge.ui.EMFDiffMergeUIPlugin;
import org.eclipse.emf.diffmerge.ui.sirius.SiriusDifferenceCategoryProvider;
import org.eclipse.emf.diffmerge.ui.util.GitLikeDiffLabelDecorator;
import org.eclipse.emf.diffmerge.ui.util.UIUtil.MenuDropDownAction;
import org.eclipse.emf.diffmerge.ui.viewers.AbstractComparisonViewer;
import org.eclipse.emf.diffmerge.ui.viewers.ComparisonViewer;
import org.eclipse.emf.diffmerge.ui.viewers.EMFDiffNode;
import org.eclipse.emf.diffmerge.ui.viewers.EnhancedComparisonTreeViewer;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionListener;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.business.api.session.SessionManagerListener;
import org.eclipse.sirius.business.api.session.SessionStatus;
import org.eclipse.sirius.common.ui.tools.api.util.SWTUtil;
import org.eclipse.sirius.ui.business.api.session.SessionEditorInput;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISaveablePart;
import org.eclipse.ui.ISaveablePart2;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.polarsys.capella.core.compare.CapellaDiffMergeLabelProvider;

import org.polarsys.capella.diffmerge.defer.ui.DeferredComparisonLoader;
import org.polarsys.capella.diffmerge.defer.ui.EMFDiffMergeDeferUIPlugin;
import org.polarsys.capella.diffmerge.defer.ui.EMFDiffMergeDeferUIPlugin.ImageID;
import org.polarsys.capella.diffmerge.defer.ui.messages.Messages;
import org.polarsys.capella.diffmerge.defer.ui.resources.IResourceChangeHandler;
import org.polarsys.capella.diffmerge.defer.ui.resources.ResourceDeltaVisitor;

/**
 * A view for deferred changes.
 * 
 * @author Olivier Constant
 */
public class PendingChangesView extends ViewPart implements ISaveablePart {

  /**
   * A session listener that synchronizes the state of this view with the state
   * of sessions.
   */
  protected class ViewSessionListener implements SessionListener {
    /** The non-null session being tracked */
    protected final Session targetSession;

    /**
     * Constructor
     * 
     * @param session
     *          the non-null session to track
     */
    public ViewSessionListener(Session session) {
      this.targetSession = session;
    }

    /**
     * @see org.eclipse.sirius.business.api.session.SessionListener#notify(int)
     */
    public void notify(int changeKind) {
      switch (changeKind) {
      case CLOSING:
        notifyClosingSession(targetSession);
        break;
      case DIRTY:
      case SYNC:
        fireDirtySync();
        break;
      default:
        // Ignore other cases
      }
    }
  }

  /**
   * 
   * A session manager listener to listen to added session
   *
   */
  protected class ViewSessionManagerListener
      extends SessionManagerListener.Stub {
    /**
     * @see org.eclipse.sirius.business.api.session.SessionManagerListener.Stub#notifyAddSession(org.eclipse.sirius.business.api.session.Session)
     */
    @Override
    public void notifyAddSession(Session newSession) {
      Collection<DeferredComparisonLoader> dcLoadersWithNullSession = PendingChangesViewUtil
          .getDCLoadersWithNullSession(dcLoaders);
      if (!dcLoadersWithNullSession.isEmpty()) {
        Collection<DeferredComparisonLoader> dcLoadersRelatedToSession = PendingChangesViewUtil
            .getDCLoadersRelatedToSession(dcLoadersWithNullSession, newSession);
        for (DeferredComparisonLoader dcLoader : dcLoadersRelatedToSession) {
          URI uri = dcLoader.getURI();
          closeDComparison(dcLoader);
          openURI(uri);
        }
      }
    }
  }

  /** The ID of this view */
  public static final String VIEW_ID = "org.polarsys.capella.diffmerge.defer.ui.view"; //$NON-NLS-1$

  /** The name of the "current deferred comparison" property */
  public static final String PROPERTY_DC_CURRENT = "PROPERTY_DC_CURRENT"; //$NON-NLS-1$

  /** The name of the "currently opened deferred comparisons" property */
  public static final String PROPERTY_DC_OPEN = "PROPERTY_DC_OPEN"; //$NON-NLS-1$

  /** The inner viewer, non-null after init */
  protected AbstractComparisonViewer viewer;

  /** Whether the view is synchronized with editor selection */
  private boolean isSyncedWithEditor;

  /** The non-null map from Sirius sessions to deferred comparisons */
  protected final Map<Session, DeferredComparisonLoader> sessionToDCMap;

  /** The non-null map from Sirius sessions to dedicated session listeners */
  protected final Map<Session, ViewSessionListener> sessionToListenerMap;

  /** The non-null list of DC loaders */
  protected final List<DeferredComparisonLoader> dcLoaders;

  /** The potentially null current deferred comparison loader */
  protected DeferredComparisonLoader current;

  /**
   * An initially null listener for part activation for inter-part
   * synchronization
   */
  protected IPartListener activePartListener;

  /**
   * An initially null listener for selection service for inter-part
   * synchronization
   */
  protected ISelectionListener selectionServiceListener;

  /** A resource change listener to update the view when the file is deleted */
  protected IResourceChangeListener resourceChangeListener;

  /** A session manager listener */
  protected SessionManagerListener viewSessionManagerListener;

  /**
   * Constructor. Must be called from the UI thread.
   */
  public PendingChangesView() {
    this.dcLoaders = new LinkedList<>();
    this.sessionToDCMap = new HashMap<>();
    this.sessionToListenerMap = new HashMap<>();
    this.current = null;
    this.activePartListener = null;
    this.selectionServiceListener = null;
    setSyncedWithEditor(false);
  }

  /**
   * Close the given deferred comparison. May be called from a non-UI thread.
   * 
   * @param dcLoader
   *          a non-null deferred comparison loader
   */
  protected void closeDComparison(DeferredComparisonLoader dcLoader) {
    closeDComparisons(Collections.singleton(dcLoader));
  }

  /**
   * Close the given deferred comparisons. May be called from a non-UI thread.
   * 
   * @param deferredComparisonLoaders
   *          a non-null set of deferred comparisons
   */
  protected void closeDComparisons(
      Collection<DeferredComparisonLoader> deferredComparisonLoaders) {
    boolean isCurrentAffected = false;
    DeferredComparisonLoader currentDCLoader = getCurrent();
    for (DeferredComparisonLoader dcLoader : deferredComparisonLoaders) {
      doCloseDComparison(dcLoader);
      isCurrentAffected = isCurrentAffected || currentDCLoader == dcLoader;
    }
    if (isCurrentAffected) {
      setCurrent(getNewCurrent(currentDCLoader));
    }
    if (!deferredComparisonLoaders.isEmpty()) {
      firePartPropertyChangedSync(PROPERTY_DC_OPEN, null, null);
    }
  }

  /**
   * 
   * @param deferredComparisonLoaders
   *          The collection of DeferredComparisonLoader from which to return
   *          the dirty one.
   * @return the collection of dirty DeferredComparisonLoader
   */
  protected Collection<DeferredComparisonLoader> getDirtyDCLoaders(
      Collection<DeferredComparisonLoader> deferredComparisonLoaders) {
    Collection<DeferredComparisonLoader> result = new HashSet<>();
    for (DeferredComparisonLoader dcLoader : deferredComparisonLoaders) {
      if (isDirty(dcLoader)) {
        result.add(dcLoader);
      }
    }
    return result;
  }

  /**
   * 
   * @param dcLoader
   *          a non-null DeferredComparisonLoader
   * @return true if the pending resource related to this loader is modified.
   */
  protected boolean isDirty(DeferredComparisonLoader dcLoader) {
    return dcLoader.getResultingResource() != null
        && dcLoader.getResultingResource().isModified();
  }

  /**
   * Return the deferred comparison that should be the current one, based on the
   * given previous one if available. May be called from a non-UI thread.
   * 
   * @param previous
   *          a potentially null deferred comparison
   * @return a potentially null deferred comparison
   */
  protected DeferredComparisonLoader getNewCurrent(
      DeferredComparisonLoader previous) {
    DeferredComparisonLoader result = null;
    Session currentSession = getCurrentEditorSession();
    if (currentSession != null) {
      result = this.sessionToDCMap.get(currentSession);
    }
    if (result == null && !dcLoaders.isEmpty()) {
      result = dcLoaders.get(0);
    }
    return result;
  }

  /**
   * Create and return the listener for part activation for inter-part
   * synchronization. May be called from a non-UI thread.
   * 
   * @return a potentially null listener
   */
  protected IPartListener createActivePartListener() {
    // Input sync with editor
    return new IPartListener() {
      /**
       * @see org.eclipse.ui.IPartListener#partOpened(org.eclipse.ui.IWorkbenchPart)
       */
      public void partOpened(IWorkbenchPart part) {
        // Nothing
      }

      /**
       * @see org.eclipse.ui.IPartListener#partDeactivated(org.eclipse.ui.IWorkbenchPart)
       */
      public void partDeactivated(IWorkbenchPart part) {
        // Nothing
      }

      /**
       * @see org.eclipse.ui.IPartListener#partClosed(org.eclipse.ui.IWorkbenchPart)
       */
      public void partClosed(IWorkbenchPart part) {
        // Nothing
      }

      /**
       * @see org.eclipse.ui.IPartListener#partBroughtToTop(org.eclipse.ui.IWorkbenchPart)
       */
      public void partBroughtToTop(IWorkbenchPart part) {
        // Nothing
      }

      /**
       * @see org.eclipse.ui.IPartListener#partActivated(org.eclipse.ui.IWorkbenchPart)
       */
      public void partActivated(IWorkbenchPart part) {
        if (isSyncedWithEditor()) {
          Session editorSession = getEditorSession(part);
          if (editorSession != null) {
            DeferredComparisonLoader currentDCLoader = getCurrent();
            Session currentSession = currentDCLoader == null ? null
                : currentDCLoader.getResultingSession();
            if (editorSession != currentSession) {
              DeferredComparisonLoader candidate = sessionToDCMap
                  .get(editorSession);
              if (candidate != null && candidate != currentDCLoader) {
                setCurrent(candidate);
              }
            }
          }
        }
      }
    };
  }

  /**
   * Create and return the comparison viewer for this view. Must be called from
   * the UI thread.
   * 
   * @param parent
   *          a non-null composite
   * @return a non-null viewer
   */
  protected AbstractComparisonViewer createComparisonViewer(Composite parent) {
    ComparisonViewer result = new ComparisonViewer(parent, getActionBars()) {
      /**
       * @see org.eclipse.emf.diffmerge.ui.viewers.ComparisonViewer#acceptToolBarAdditions(org.eclipse.jface.viewers.Viewer)
       */
      @Override
      protected boolean acceptToolBarAdditions(Viewer viewer) {
        return false;
      }

      /**
       * @see org.eclipse.emf.diffmerge.ui.viewers.ComparisonViewer#doCreateViewerSynthesis(org.eclipse.swt.widgets.Composite)
       */
      @Override
      protected EnhancedComparisonTreeViewer doCreateViewerSynthesis(
          Composite localParent) {
        return new EnhancedComparisonTreeViewer(localParent) {
          /**
           * @see org.eclipse.emf.diffmerge.ui.viewers.HeaderViewer#setHeaderText(java.lang.String)
           */
          @Override
          public boolean setHeaderText(String text) {
            String newText = text;
            if (isReadOnly()) {
              String suffix = getReadOnlySuffix();
              if (!newText.endsWith(suffix)) {
                newText = newText + suffix;
              }
            }
            return super.setHeaderText(newText);
          }
        };
      }

      /**
       * @see org.eclipse.emf.diffmerge.ui.viewers.AbstractComparisonViewer#getSite()
       */
      @Override
      protected IWorkbenchPartSite getSite() {
        return PendingChangesView.this.getSite();
      }
    };
    // Capella dependency used here. Alternative: use the
    // getResultingConfiguration()
    // of the current loader if there is one, and if it is a comparison method,
    // to create
    // the viewer, assuming the ComparisonViewer can be recreated for each
    // loader.
    result
        .setDelegateLabelProvider(CapellaDiffMergeLabelProvider.getInstance());
    result.setDiffLabelDecorator(GitLikeDiffLabelDecorator.getInstance());
    result.setCategoryProvider(new SiriusDifferenceCategoryProvider());
    return result;
  }

  /**
   * Create the "close" item in the given context and return it. Must be called
   * from the UI thread.
   * 
   * @param context
   *          a non-null object
   * @return a potentially null item
   */
  protected ActionContributionItem createItemClose(
      IContributionManager context) {
    final IAction action = new Action() {
      /**
       * @see org.eclipse.jface.action.Action#run()
       */
      @Override
      public void run() {
        if (isDirty(getCurrent())
            && getCurrent().getResultingSession() != null) {
          int choice = SWTUtil.showSaveDialogWithMessage(
              getCurrent().getResultingSession(),
              Messages.PendingChangesView_PendingFileModified, true);
          if (choice == ISaveablePart2.YES) {
            doSave(new NullProgressMonitor());
            closeDComparison(getCurrent());
          } else if (choice == ISaveablePart2.NO) {
            closeDComparison(getCurrent());
          }
        } else {
          closeDComparison(getCurrent());
        }
      }
    };
    action.setToolTipText(Messages.PendingChangesView_Close_Tooltip);
    action.setImageDescriptor(EMFDiffMergeDeferUIPlugin.getDefault()
        .getImageDescriptor(ImageID.REMOVE));
    // Initialization
    action.setEnabled(false);
    addPartPropertyListener(new IPropertyChangeListener() {
      /**
       * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
       */
      public void propertyChange(PropertyChangeEvent event) {
        if (PROPERTY_DC_CURRENT.equals(event.getProperty())) {
          action.setEnabled(getCurrent() != null);
        }
      }
    });
    ActionContributionItem result = new ActionContributionItem(action);
    context.add(result);
    return result;
  }

  /**
   * Create the "close" item in the given context and return it. Must be called
   * from the UI thread.
   * 
   * @param context
   *          a non-null object
   * @return a potentially null item
   */
  protected ActionContributionItem createItemLinkWithEditor(
      IContributionManager context) {
    final IAction action = new Action(null, IAction.AS_CHECK_BOX) {
      /**
       * @see org.eclipse.jface.action.Action#run()
       */
      @Override
      public void run() {
        setSyncedWithEditor(isChecked());
      }
    };
    action.setChecked(isSyncedWithEditor());
    action.setToolTipText(Messages.PendingChangesView_SyncEditor_ToolTip);
    action.setImageDescriptor(EMFDiffMergeUIPlugin.getDefault()
        .getImageDescriptor(EMFDiffMergeUIPlugin.ImageID.SYNCED));
    ActionContributionItem result = new ActionContributionItem(action);
    context.add(result);
    return result;
  }

  /**
   * Create the "select current" item in the given context and return it. Must
   * be called from the UI thread.
   * 
   * @param context
   *          a non-null object
   * @return a potentially null item
   */
  protected ActionContributionItem createItemSelectCurrent(
      IContributionManager context) {
    final MenuDropDownAction action = new MenuDropDownAction();
    MenuManager mgr = action.getMenuManager();
    mgr.setRemoveAllWhenShown(true);
    mgr.addMenuListener(new IMenuListener() {
      /**
       * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
       */
      public void menuAboutToShow(IMenuManager manager) {
        final DeferredComparisonLoader currentDCLoader = getCurrent();
        for (final DeferredComparisonLoader loader : dcLoaders) {
          String name = PendingChangesViewUtil.getName(loader);
          IAction selectAction = new Action(name, IAction.AS_CHECK_BOX) {
            /**
             * @see org.eclipse.jface.action.Action#run()
             */
            @Override
            public void run() {
              if (loader != currentDCLoader) {
                setCurrent(loader);
              }
            }
          };
          selectAction.setChecked(loader == currentDCLoader);
          manager.add(selectAction);
        }
      }
    });
    action.setToolTipText(Messages.PendingChangesView_SelectCurrent_Tooltip);
    action.setImageDescriptor(EMFDiffMergeDeferUIPlugin.getDefault()
        .getImageDescriptor(ImageID.CHANGE_OBJ));
    // Initialization
    action.setEnabled(false);
    addPartPropertyListener(new IPropertyChangeListener() {
      /**
       * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
       */
      public void propertyChange(PropertyChangeEvent event) {
        if (PROPERTY_DC_OPEN.equals(event.getProperty())) {
          action.setEnabled(!dcLoaders.isEmpty());
        }
      }
    });
    ActionContributionItem result = new ActionContributionItem(action);
    context.add(result);
    return result;
  }

  /**
   * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
   *      Must be called from the UI thread.
   */
  @Override
  public void createPartControl(Composite parent) {
    viewer = createComparisonViewer(parent);
    getSite().setSelectionProvider(viewer);
    viewer.getControl().setVisible(hasContent());
    updateContentDescription();
  }

  /**
   * Update the content description of the view.
   */
  protected void updateContentDescription() {
    setContentDescription(computeContentDescription());
  }

  /**
   * 
   * @return the computed content description from the current
   *         {@link DeferredComparisonLoader} if any.
   */
  protected String computeContentDescription() {
    if (hasContent()) {
      // Pending file
      URI pendingFileURI = getCurrent().getURI();
      String relativePendingFileURI = PendingChangesViewUtil
          .getRelativeURI(pendingFileURI);
      
      // Session
      URI sessionURI = getCurrent().getResultingSessionURI();
      String relativeSessionURI = PendingChangesViewUtil
          .getRelativeURI(sessionURI);

      // Description
      return NLS.bind(Messages.PendingChangesView_ContentDescription,
          relativePendingFileURI != null ? relativePendingFileURI
              : pendingFileURI.toString(),
          relativeSessionURI != null ? relativeSessionURI
              : sessionURI.toString());
    }
    return Messages.PendingChangesView_NoContentToDisplay;
  }

  /**
   * Create and return the listener for selection service for inter-part
   * synchronization. Must be called from the UI thread.
   * 
   * @return a potentially null listener
   */
  protected ISelectionListener createSelectionServiceListener() {
    // Selection sync with editor
    return new ISelectionListener() {
      /**
       * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.IWorkbenchPart,
       *      org.eclipse.jface.viewers.ISelection)
       */
      public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        if (isSyncedWithEditor() && part != PendingChangesView.this
            && viewer != null && !viewer.getControl().isDisposed()) {
          if (viewer instanceof ComparisonViewer) {
            ((ComparisonViewer) viewer).setTreeSelection(selection);
          } else {
            viewer.setSelection(selection, true);
          }
        }
      }
    };
  }

  /**
   * Set up the tools available the tool bar of the view. Must be called from
   * the UI thread.
   */
  protected void createTools() {
    IActionBars actionBars = getActionBars();
    assert actionBars != null; // Guaranteed by calling this operation at the
                               // end of init(...)
    IToolBarManager manager = actionBars.getToolBarManager();
    createItemClose(manager);
    createItemSelectCurrent(manager);
    createItemLinkWithEditor(manager);
  }

  /**
   * @see org.eclipse.ui.part.WorkbenchPart#dispose() Must be called from the UI
   *      thread.
   */
  @Override
  public void dispose() {
    super.dispose();
    Collection<DeferredComparisonLoader> deferredComparisonLoaders = new ArrayList<>(
        dcLoaders);
    closeDComparisons(deferredComparisonLoaders);
    IWorkbenchPartSite site = getSite();
    if (site != null) {
      if (activePartListener != null) {
        site.getPage().removePartListener(activePartListener);
        activePartListener = null;
      }
      if (selectionServiceListener != null) {
        site.getWorkbenchWindow().getSelectionService()
            .removeSelectionListener(selectionServiceListener);
        selectionServiceListener = null;
      }
    }

    if (resourceChangeListener != null) {
      ResourcesPlugin.getWorkspace()
          .removeResourceChangeListener(resourceChangeListener);
    }

    if (viewSessionManagerListener != null) {
      SessionManager.INSTANCE
          .removeSessionsListener(viewSessionManagerListener);
    }
  }

  /**
   * Close the given deferred comparison without issuing any notification. Must
   * be called from the UI thread.
   * 
   * @param dcLoader
   *          a non-null deferred comparisons
   */
  protected void doCloseDComparison(DeferredComparisonLoader dcLoader) {
    dcLoaders.remove(dcLoader);
    Session session = dcLoader.getResultingSession();
    if (session != null) {
      sessionToDCMap.remove(session);
      ViewSessionListener listener = sessionToListenerMap.remove(session);
      session.removeListener(listener);
    }
    dcLoader.dispose();
  }

  /**
   * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
   *      May be called from a non-UI thread.
   */
  public void doSave(IProgressMonitor monitor) {
    for(DeferredComparisonLoader loader : dcLoaders) {
      doSave(loader, monitor);
    }
  }

  /**
   * 
   * @param dcLoader
   *          The DeferredComparisonLoader
   * @param monitor
   *          The IProgressMonitor
   */
  private void doSave(DeferredComparisonLoader dcLoader,
      IProgressMonitor monitor) {
    Session currentSession = dcLoader.getResultingSession();
    if (currentSession != null) {
      currentSession.save(monitor);
    }

    Resource resultingResource = dcLoader.getResultingResource();
    if (resultingResource != null) {
      try {
        resultingResource.save(null);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * @see org.eclipse.ui.ISaveablePart#doSaveAs() May be called from a non-UI
   *      thread.
   */
  public void doSaveAs() {
    throw new UnsupportedOperationException();
  }

  /**
   * Notify that the dirty state has changed. May be called from a non-UI
   * thread.
   */
  protected void fireDirtySync() {
    Display.getDefault().syncExec(new Runnable() {
      /**
       * @see java.lang.Runnable#run()
       */
      @SuppressWarnings("synthetic-access")
      public void run() {
        firePropertyChange(ISaveablePart.PROP_DIRTY);
      }
    });
  }

  /**
   * Notify that a property of the view has changed. May be called from a non-UI
   * thread.
   * 
   * @param key
   *          a non-null string
   * @param oldValue
   *          an optional string
   * @param newValue
   *          an optional string
   */
  protected void firePartPropertyChangedSync(final String key,
      final String oldValue, final String newValue) {
    Display.getDefault().syncExec(new Runnable() {
      /**
       * @see java.lang.Runnable#run()
       */
      @SuppressWarnings("synthetic-access")
      public void run() {
        firePartPropertyChanged(key, oldValue, newValue);
      }
    });
  }

  /**
   * Return the action bars of the view Must be called from the UI thread.
   * 
   * @return an object which is non-null if the view has been initialized
   */
  protected IActionBars getActionBars() {
    IActionBars result = null;
    IViewSite site = getViewSite();
    if (site != null) {
      result = site.getActionBars();
    }
    return result;
  }

  /**
   * Return the current deferred comparison, if any. May be called from a non-UI
   * thread.
   * 
   * @return a potentially null deferred comparison
   */
  public DeferredComparisonLoader getCurrent() {
    return current;
  }

  /**
   * Return the session of the currently active editor, if any. Must be called
   * from the UI thread.
   * 
   * @return a potentially null session
   */
  protected Session getCurrentEditorSession() {
    IEditorPart editor = getSite().getWorkbenchWindow().getActivePage()
        .getActiveEditor();
    return getEditorSession(editor);
  }

  /**
   * Return the session of the currently active editor, if any. Must be called
   * from the UI thread.
   * 
   * @return a potentially null session
   */
  protected Session getEditorSession(IWorkbenchPart workpenchPart) {
    Session result = null;
    if (workpenchPart instanceof IEditorPart) {
      IEditorInput input = ((IEditorPart) workpenchPart).getEditorInput();
      if (input instanceof SessionEditorInput) {
        result = ((SessionEditorInput) input).getSession();
      } else if (input != null) {
        result = input.getAdapter(Session.class);
      }
    }
    return result;
  }

  /**
   * Return the suffix to be applied to a header to inform the user that the
   * comparison is in read-only mode. May be called from a non-UI thread.
   * 
   * @return a non-null string
   */
  protected String getReadOnlySuffix() {
    return Messages.PendingChangesView_ReadOnly_Suffix;
  }

  /**
   * Return whether this view currently has content to display. May be called
   * from a non-UI thread.
   */
  protected boolean hasContent() {
    return current != null;
  }

  /**
   * @see org.eclipse.ui.part.ViewPart#init(org.eclipse.ui.IViewSite) Must be
   *      called from the UI thread.
   */
  @Override
  public void init(IViewSite site) throws PartInitException {
    super.init(site);
    createTools();
    // Dirty state
    addPartPropertyListener(new IPropertyChangeListener() {
      /**
       * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
       */
      public void propertyChange(PropertyChangeEvent event) {
        if (PROPERTY_DC_CURRENT.equals(event.getProperty())) {
          fireDirtySync();
        }
      }
    });
    // Input sync with editor
    activePartListener = createActivePartListener();
    if (activePartListener != null) {
      site.getPage().addPartListener(activePartListener);
    }
    // Selection sync with editor
    selectionServiceListener = createSelectionServiceListener();
    if (selectionServiceListener != null) {
      // Does not work as post selection listener (called only at part
      // activation)
      site.getWorkbenchWindow().getSelectionService()
          .addSelectionListener(selectionServiceListener);
    }

    // Resource changed listener
    resourceChangeListener = createResourceChangedListener();
    ResourcesPlugin.getWorkspace().addResourceChangeListener(
        resourceChangeListener, IResourceChangeEvent.POST_CHANGE);

    // Session manager listener
    viewSessionManagerListener = createSessionManagerListener();
    SessionManager.INSTANCE.addSessionsListener(viewSessionManagerListener);
  }

  /**
   * 
   * @return a new session manager listener
   */
  protected SessionManagerListener createSessionManagerListener() {
    return new ViewSessionManagerListener();
  }

  /**
   * 
   * @return the resource change listener
   */
  protected IResourceChangeListener createResourceChangedListener() {

    return new IResourceChangeListener() {

      public void resourceChanged(IResourceChangeEvent event) {
        IResourceDelta delta = event.getDelta();
        if (delta != null) {
          IResourceDeltaVisitor visitor = new ResourceDeltaVisitor(
              event.getType(), new IResourceChangeHandler() {

                public void handleFileRemoved(int eventType, IFile file) {
                  if (EMFDiffMergeDeferUIPlugin.getDefault().getFileExtension()
                      .equals(file.getFileExtension())) {
                    URI fileURI = URI.createPlatformResourceURI(
                        file.getFullPath().toString(), true);
                    closeDComparisons(PendingChangesViewUtil
                        .getDCLoadersForURI(dcLoaders, fileURI));
                  }
                }
              });
          try {
            delta.accept(visitor);
          } catch (CoreException e) {
            e.printStackTrace();
          }
        }
      }
    };
  }

  /**
   * @see org.eclipse.ui.ISaveablePart#isDirty() May be called from a non-UI
   *      thread.
   */
  public boolean isDirty() {
    boolean result = false;
    if (current != null) {
      Session currentSession = current.getResultingSession();
      if (currentSession != null) {
        result = currentSession.getStatus() == SessionStatus.DIRTY;
      }
    }
    return result;
  }

  /**
   * Return whether the view is in read-only mode given its current input. If
   * there is no current input then false is returned. May be called from a
   * non-UI thread.
   */
  protected boolean isReadOnly() {
    boolean result = false;
    DeferredComparisonLoader currentLoader = getCurrent();
    if (currentLoader != null) {
      result = currentLoader.isReadOnly();
    }
    return result;
  }

  /**
   * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed() May be called from a
   *      non-UI thread.
   */
  public boolean isSaveAsAllowed() {
    return false;
  }

  /**
   * @see org.eclipse.ui.ISaveablePart#isSaveOnCloseNeeded() May be called from
   *      a non-UI thread.
   */
  public boolean isSaveOnCloseNeeded() {
    return isDirty();
  }

  /**
   * Return whether the view is synchronized with editor selection. May be
   * called from a non-UI thread.
   */
  protected boolean isSyncedWithEditor() {
    return isSyncedWithEditor;
  }

  /**
   * Notify that the given session is closing. May be called from a non-UI
   * thread.
   * 
   * @param session
   *          a non-null session
   */
  protected void notifyClosingSession(Session session) {
    closeDComparison(sessionToDCMap.get(session));
  }

  /**
   * Open this view if possible. Must be called from the UI thread.
   */
  public static PendingChangesView open() {
    PendingChangesView result = null;
    try {
      result = (PendingChangesView) PlatformUI.getWorkbench()
          .getActiveWorkbenchWindow().getActivePage().showView(VIEW_ID);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * Open the deferred comparison at the given URI, if any. Must be called from
   * the UI thread.
   * 
   * @param uri
   *          a non-null URI
   * @return whether the operation was successful
   */
  public boolean openURI(URI uri) {
    // Check if the given URI is already loaded
    for (DeferredComparisonLoader deferredComparisonLoader : dcLoaders) {
      if (deferredComparisonLoader.getURI().equals(uri)) {
        setCurrent(deferredComparisonLoader);
        return deferredComparisonLoader.isSuccessful();
      }
    }

    // If not already loaded create a new loader and load it
    DeferredComparisonLoader loader = new DeferredComparisonLoader(uri,
        WRITE_IN_EXISTING_SESSION);
    loader.run();
    boolean result = loader.isSuccessful();
    
    if (result) {
      List<?> contents = loader.getResultingDiffNode().getActualComparison().getContents();
      for(Object match : contents) {
        if(match instanceof EMatch && (((EMatch)match).getTarget() == null || ((EMatch)match).getTarget().eIsProxy())) {
          MessageDialog.openError(getSite().getShell(), Messages.PendingChangesView_Error, Messages.PendingChangesView_ErrorMsg);
          return false;
        }
      }
      registerDComparison(loader);
    }
    return result;
  }

  /**
   * Register the given deferred comparison loader. Must be called from the UI
   * thread.
   * 
   * @param dcLoader
   *          a non-null deferred comparison loader which is isSuccessful()
   */
  protected void registerDComparison(DeferredComparisonLoader dcLoader) {
    boolean proceed = true;
    Session session = dcLoader.getResultingSession();
    if (session != null) {
      DeferredComparisonLoader previous = sessionToDCMap.get(session);
      boolean sessionRegistered = previous != null;
      if (sessionRegistered) {
        String previousName = PendingChangesViewUtil.getName(previous);
        proceed = MessageDialog.openConfirm(getSite().getShell(), getPartName(),
            String.format(Messages.PendingChangesView_ConfirmReplace,
                previousName));
        if (proceed) {
          closeDComparison(previous);
        }
      }
      if (proceed) {
        sessionToDCMap.put(session, dcLoader);
        ViewSessionListener listener = new ViewSessionListener(session);
        sessionToListenerMap.put(session, listener);
        session.addListener(listener);
      }
    }
    if (proceed) {
      dcLoaders.add(dcLoader);
      setCurrent(dcLoader);
      firePartPropertyChangedSync(PROPERTY_DC_OPEN, null, null);
    }
  }

  /**
   * Set the current deferred comparison. May be called from a non-UI thread.
   * 
   * @param loader
   *          a potentially null deferred comparison loader
   */
  protected void setCurrent(final DeferredComparisonLoader loader) {
    current = loader;
    if (viewer != null) {
      final Control control = viewer.getControl();
      if (control != null && !control.isDisposed()) {
        Display.getDefault().syncExec(new Runnable() {
          /**
           * @see java.lang.Runnable#run()
           */
          @SuppressWarnings("synthetic-access")
          public void run() {
            control.setVisible(hasContent());
            updateContentDescription();
            if (loader != null) {
              EMFDiffNode diffNode = loader.getResultingDiffNode();
              viewer.setInput(diffNode);
              if (viewer instanceof ComparisonViewer) {
                EnhancedComparisonTreeViewer synthesisViewer = ((ComparisonViewer) viewer)
                    .getSynthesisViewer();
                if (synthesisViewer != null) {
                  synthesisViewer
                      .setHeaderText(synthesisViewer.getDefaultHeaderText());
                }
              }
            }
            firePartPropertyChanged(PROPERTY_DC_CURRENT, null, null);
          }
        });
      }
    }
  }

  /**
   * @see org.eclipse.ui.part.WorkbenchPart#setFocus() Must be called from the
   *      UI thread.
   */
  @Override
  public void setFocus() {
    if (viewer != null) {
      Control viewerControl = viewer.getControl();
      if (viewerControl != null && !viewerControl.isDisposed()) {
        viewerControl.setFocus();
      }
    }
  }

  /**
   * Set whether the view is synchronized with editor selection May be called
   * from a non-UI thread.
   * 
   * @param synced
   *          whether it is synchronized
   */
  protected void setSyncedWithEditor(boolean synced) {
    isSyncedWithEditor = synced;
  }

}
