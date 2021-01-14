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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.diffmerge.diffdata.EComparison;
import org.eclipse.emf.diffmerge.generic.api.scopes.IEditableTreeDataScope;
import org.eclipse.emf.diffmerge.ui.setup.EMFDiffMergeEditorInput;
import org.eclipse.emf.diffmerge.ui.specification.IComparisonMethod;
import org.eclipse.emf.diffmerge.ui.specification.IComparisonMethodFactory;
import org.eclipse.emf.diffmerge.ui.specification.IModelScopeDefinition;
import org.eclipse.emf.diffmerge.ui.util.MiscUtil;
import org.eclipse.emf.diffmerge.ui.viewers.EMFDiffNode;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.ui.IEditorInput;

import org.polarsys.capella.diffmerge.defer.helpers.DeferComparisonOperation;


/**
 * A job that defers merging by saving a comparison.
 * @author Olivier Constant
 */
public class DeferComparisonJob extends Job {
  
  /** The non-null diff node */
  protected final EMFDiffNode diffNode;
  
  /** The non-null URI for the deferred comparison */
  protected final URI deferFileURI;
  
  /** The initially null defer operation */
  protected DeferComparisonOperation deferOperation;
  
  
  /**
   * Constructor
   * @param node a non-null diff node
   * @param deferFileURI a non-null URI for the deferred comparison
   */
  public DeferComparisonJob(EMFDiffNode node, URI deferFileURI) {
    super(""); //$NON-NLS-1$
    this.diffNode = node;
    this.deferFileURI = deferFileURI;
    this.deferOperation = null;
    setUser(true);
  }
  
  /**
   * Create and return a defer comparison operation based on the given diff node
   * and file URI
   * @param node a non-null diff node
   * @param deferFileURI a non-null URI
   * @return a potentially null object
   */
  public DeferComparisonOperation createDeferComparisonOperation(EMFDiffNode node,
      URI deferFileURI) {
    DeferComparisonOperation result = null;
    IEditableTreeDataScope<?> targetScope =
        node.getUIComparison().getActualComparison().getScope(DeferComparisonHandler.TARGET_ROLE);
    Object firstRoot = targetScope.getRoots().get(0);
    final EditingDomain ed = AdapterFactoryEditingDomain.getEditingDomainFor(firstRoot);
    if (ed != null) {
      IEditorInput ei = node.getEditorInput();
      if (ei instanceof EMFDiffMergeEditorInput) {
        IComparisonMethod<?> method = ((EMFDiffMergeEditorInput)ei).getComparisonMethod();
        IComparisonMethodFactory<?> factory = method == null? null: method.getFactory();
        if (method != null && factory != null) {
          IModelScopeDefinition scopeDef = method.getModelScopeDefinition(DeferComparisonHandler.TARGET_ROLE);
          Object entrypoint = scopeDef.getEntrypoint();
          if (entrypoint instanceof URI) {
            result = createDeferComparisonOperation(
                (EComparison) node.getActualComparison(), ed.getResourceSet(),
                deferFileURI, (URI)entrypoint, factory.getID());
          }
        }
      }
    }
    return result;
  }
  
  /**
   * Create and return a defer comparison operation based on the given inputs
   * @param comparison a non-null comparison
   * @param resourceSet a non-null resource set for storing the deferred comparison
   * @param changeResourceURI a non-null URI for the deferred comparison resource
   * @param targetScopeURI the non-null URI of the target scope definition
   * @param comparisonMethodFactoryID the non-null ID of the comparison method factory
   * @return a non-null object
   */
  protected DeferComparisonOperation createDeferComparisonOperation(EComparison comparison,
      ResourceSet resourceSet, URI changeResourceURI, URI targetScopeURI,
      String comparisonMethodFactoryID) {
    return new DeferComparisonOperation(comparison, resourceSet, changeResourceURI,
        targetScopeURI, comparisonMethodFactoryID);
  }
  
  /**
   * Return the defer operation to execute
   * @return a non-null object
   */
  public DeferComparisonOperation getDeferOperation() {
    return deferOperation;
  }
  
  /**
   * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
   */
  @Override
  protected IStatus run(final IProgressMonitor monitor) {
    final IStatus[] result = new IStatus[1];
    deferOperation = createDeferComparisonOperation(diffNode, deferFileURI);
    if (deferOperation != null) {
      setName(deferOperation.getOperationName());
      Runnable behavior = new Runnable() {
        /**
         * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
         */
        public void run()  {
          result[0] = deferOperation.run(monitor);
        }
      };
      try {
        MiscUtil.execute(
            diffNode.getEditingDomain(), getName(), behavior, false);
      } catch (Exception e) {
        result[0] = new Status(IStatus.ERROR,
            EMFDiffMergeDeferUIPlugin.getDefault().getPluginId(), e.getMessage(), e);
      }
    } else {
      result[0] = new Status(IStatus.ERROR,
          EMFDiffMergeDeferUIPlugin.getDefault().getPluginId(), "Cannot initialize operation"); //$NON-NLS-1$
    }
    return result[0];
  }
  
}