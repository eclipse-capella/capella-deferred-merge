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
package org.polarsys.capella.diffmerge.defer.helpers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.diffmerge.diffdata.EComparison;
import org.eclipse.emf.diffmerge.generic.api.Role;
import org.eclipse.emf.diffmerge.generic.impl.helpers.AbstractExpensiveOperation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.TransactionalCommandStack;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.sirius.business.api.helper.SiriusUtil;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;

import org.polarsys.capella.diffmerge.defer.EMFDiffMergeDeferPlugin;
import org.polarsys.capella.diffmerge.defer.Messages;
import org.polarsys.capella.diffmerge.defer.diffdeferdata.DeferredComparison;
import org.polarsys.capella.diffmerge.defer.diffdeferdata.DiffdeferdataFactory;

/**
 * An operation that defers merging by saving a comparison.
 * 
 * @author Olivier Constant
 */
public class DeferComparisonOperation extends AbstractExpensiveOperation {

  /** The non-null comparison whose changes to defer */
  protected final EComparison comparison;

  /** The non-null resource set for storing the deferred comparison */
  protected final ResourceSet resourceSet;

  /** The non-null URI for the deferred comparison resource */
  protected final URI changeResourceURI;

  /** The non-null URI of the target scope definition */
  protected final URI targetScopeURI;

  /** The non-null ID of the comparison method factory */
  protected final String comparisonMethodFactoryID;

  /**
   * The non-null copier used to extract a minimal copy of the given comparison
   */
  protected final TrimmingCopier copier;

  /**
   * Constructor
   * 
   * @param comparison
   *          a non-null comparison
   * @param resourceSet
   *          a non-null resource set for storing the deferred comparison
   * @param changeResourceURI
   *          a non-null URI for the deferred comparison resource
   * @param targetScopeURI
   *          the non-null URI of the target scope definition
   * @param comparisonMethodFactoryID
   *          the non-null ID of the comparison method factory
   */
  public DeferComparisonOperation(EComparison comparison,
      ResourceSet resourceSet, URI changeResourceURI, URI targetScopeURI,
      String comparisonMethodFactoryID) {
    this.comparison = comparison;
    this.resourceSet = resourceSet;
    this.changeResourceURI = changeResourceURI;
    this.targetScopeURI = targetScopeURI;
    this.comparisonMethodFactoryID = comparisonMethodFactoryID;
    this.copier = new TrimmingCopier(comparison);
  }

  /**
   * Create and define the deferred comparison and return it
   * 
   * @return a non-null object
   */
  protected DeferredComparison createDComparison() {
    DeferredComparison dComparison = DiffdeferdataFactory.eINSTANCE
        .createDeferredComparison();
    // Basic attributes
    URI targetSiriusURI = targetScopeURI.trimFileExtension()
        .appendFileExtension(SiriusUtil.SESSION_RESOURCE_EXTENSION);
    String targetSiriusURIText = targetSiriusURI.toString();
    dComparison.setComparisonMethodFactoryID(comparisonMethodFactoryID);
    dComparison.setTargetScopeDefinitionURI(targetSiriusURIText);
    // Content and root source elements
    TrimmingCopier copier = getCopier();
    EComparison trimmedComparison = copier.copy();
    dComparison.setActualComparison(trimmedComparison);
    List<EObject> originalSourceRoots = comparison.getScope(Role.REFERENCE)
        .getRoots();
    List<EObject> logicalRoots = dComparison.getLogicalSourceRoots();
    List<EObject> technicalRoots = dComparison.getTechnicalSourceRoots();
    for (EObject originalSourceElement : copier.getOriginalSourceElements()) {
      EObject copySourceElement = copier.get(originalSourceElement);
      if (copySourceElement.eContainer() == null) {
        if (originalSourceRoots.contains(originalSourceElement)) {
          logicalRoots.add(copySourceElement);
        } else {
          technicalRoots.add(copySourceElement);
        }
      }
    }
    return dComparison;
  }

  /**
   * Return the copier used to extract a minimal copy of the comparison
   * 
   * @return a non-null object
   */
  protected TrimmingCopier getCopier() {
    return copier;
  }

  /**
   * Return a resource for storing the deferred comparison
   * 
   * @return a non-null resource
   */
  protected Resource getDComparisonResource() {
    Resource result = resourceSet.createResource(changeResourceURI);
    return result;
  }

  /**
   * @see org.eclipse.emf.diffmerge.util.IExpensiveOperation#getOperationName()
   */
  public String getOperationName() {
    return Messages.DeferChangesOperation_Name;
  }

  /**
   * @see org.eclipse.emf.diffmerge.impl.helpers.AbstractExpensiveOperation#getWorkAmount()
   */
  @Override
  protected int getWorkAmount() {
    return comparison.getMapping().size();
  }

  /**
   * @see org.eclipse.emf.diffmerge.util.IExpensiveOperation#run()
   */
  public IStatus run() {
    // Definition of DeferredComparison
    DeferredComparison dComparison = createDComparison();
    // Saving of resource
    Resource dComparisonRessource = getDComparisonResource();

    TransactionalEditingDomain editingDomain = TransactionUtil
        .getEditingDomain(dComparisonRessource);
    if (editingDomain != null) {
      return executeInReadWriteTransaction(dComparison, dComparisonRessource,
          editingDomain);
    } else {
      return doRun(dComparison, dComparisonRessource);
    }
  }

  /**
   * Calls the {@link #doRun(DeferredComparison, Resource)} in a write
   * transaction.
   * 
   * @param editingDomain the editing domain
   * @return the status
   */
  protected IStatus executeInReadWriteTransaction(
      DeferredComparison dComparison, Resource dComparisonRessource,
      TransactionalEditingDomain editingDomain) {
    TransactionalCommandStack stack = (TransactionalCommandStack) editingDomain
        .getCommandStack();
    final List<IStatus> list = new ArrayList<>();
    stack.execute(new AbstractNonDirtyingRecordingCommand(editingDomain) {

      @Override
      protected void doExecute() {
        list.add(doRun(dComparison, dComparisonRessource));
      }
    });
    return list.get(0);
  }

  /**
   * 
   * @param dComparison the DeferredComparison
   * @param dComparisonRessource the dComparisonRessource
   * @return the status
   */
  protected IStatus doRun(DeferredComparison dComparison,
      Resource dComparisonRessource) {
    IStatus result = Status.OK_STATUS;
    List<EObject> resourceContents = dComparisonRessource.getContents();
    resourceContents.add(dComparison);
    for (EObject technicalRoot : dComparison.getTechnicalSourceRoots()) {
      resourceContents.add(technicalRoot);
    }
    try {
      resourceSet.getResources().add(dComparisonRessource);
      updateRepresentationDescriptors();
      dComparisonRessource.save(null);
    } catch (java.io.IOException e) {
      e.printStackTrace();
      result = new Status(IStatus.ERROR,
          EMFDiffMergeDeferPlugin.getDefault().getPluginId(), e.getMessage(),
          e);
    } finally {
      dComparisonRessource.unload();
      resourceSet.getResources().remove(dComparisonRessource);
      copier.clear();
    }
    return result;
  }
  
  /**
   * Update the copy DRepresentationDescriptors now that all copy elements are in a resource
   */
  protected void updateRepresentationDescriptors() {
    for (DRepresentationDescriptor originalDescriptor : copier.getOriginalRepresentationDescriptors()) {
      EObject copyDescriptor = copier.get(originalDescriptor);
      DRepresentation originalRepresentation = originalDescriptor.getRepresentation();
      EObject copyRepresentation = copier.get(originalRepresentation);
      if (copyDescriptor instanceof DRepresentationDescriptor &&
          copyRepresentation instanceof DRepresentation) {
        ((DRepresentationDescriptor) copyDescriptor).setRepresentation(
            (DRepresentation) copyRepresentation);
      }
    }
  }
  
}