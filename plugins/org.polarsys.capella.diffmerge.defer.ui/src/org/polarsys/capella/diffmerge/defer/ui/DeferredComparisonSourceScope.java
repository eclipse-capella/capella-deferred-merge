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

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.diffmerge.sirius.SiriusScope;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.sirius.viewpoint.DRepresentation;

import org.polarsys.capella.diffmerge.defer.diffdeferdata.DeferredComparison;


/**
 * A model scope for the source side of a given deferred comparison.
 * @author Olivier Constant
 */
public class DeferredComparisonSourceScope extends SiriusScope {
  
  /** The non-null deferred comparison */
  private final DeferredComparison dComparison;
  
  
  /**
   * Constructor
   * @param deferredComparison a non-null deferred comparison that belongs to a resource set
   */
  public DeferredComparisonSourceScope(DeferredComparison deferredComparison) {
    super(deferredComparison.eResource().getURI(),
        deferredComparison.eResource().getResourceSet(), true);
    this.dComparison = deferredComparison;
  }
  
  /**
   * @see org.eclipse.emf.diffmerge.impl.scopes.FragmentedModelScope#getContents()
   */
  @Override
  public List<EObject> getContents() {
    return Collections.unmodifiableList(getDeferredComparison().getLogicalSourceRoots());
  }
  
  /**
   * Return the deferred comparison of this scope
   * @return a non-null object
   */
  public DeferredComparison getDeferredComparison() {
    return dComparison;
  }
  
  /**
   * @see org.eclipse.emf.diffmerge.impl.scopes.AbstractModelScope#getOriginator()
   */
  @Override
  public Object getOriginator() {
    Object result = null;
    Resource resource = getDeferredComparison().eResource();
    if (resource != null) {
      URI uri = resource.getURI();
      String lastSegment = uri.lastSegment();
      result = URI.decode(lastSegment);
    }
    if (result == null) {
      result = super.getOriginator();
    }
    return result;
  }
  
  /**
   * 
   * @see org.eclipse.emf.diffmerge.sirius.SiriusScope#getContainer(org.eclipse.emf.ecore.EObject)
   */
  @Override
  public EObject getContainer(EObject element) {
    if (element instanceof DRepresentation
        && _idToDescriptor.get(((DRepresentation) element).getUid()) != null) {
      return _idToDescriptor.get(((DRepresentation) element).getUid());
    }
    return super.getContainer(element);
  }
  
  /**
   * @see org.eclipse.emf.diffmerge.impl.scopes.AbstractModelScope#resolveProxies()
   */
  @Override
  protected boolean resolveProxies() {
    // Should be self-contained anyway
    return true;
  }
  
}
