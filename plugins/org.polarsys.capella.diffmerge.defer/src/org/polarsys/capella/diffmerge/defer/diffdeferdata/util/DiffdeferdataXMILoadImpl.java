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
package org.polarsys.capella.diffmerge.defer.diffdeferdata.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.UnresolvedReferenceException;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.impl.XMILoadImpl;

/**
 * A XMILoad to process specific cases while loading deferred comparisons.
 * 
 * @author Ali AKAR
 *
 */
public class DiffdeferdataXMILoadImpl extends XMILoadImpl {

  /**
   * Constructor
   * 
   * @param helper
   *          the non-null XMLHelper
   */
  public DiffdeferdataXMILoadImpl(XMLHelper helper) {
    super(helper);
  }

  /**
   * 
   * @see org.eclipse.emf.ecore.xmi.impl.XMLLoadImpl#handleErrors()
   */
  @Override
  protected void handleErrors() throws IOException {
    // Remove errors that are related to unresolved references
    if (!resource.getErrors().isEmpty()) {
      Collection<Resource.Diagnostic> errors = new ArrayList<>(
          resource.getErrors());
      for (Resource.Diagnostic error : errors) {
        if (error instanceof UnresolvedReferenceException) {
          resource.getErrors().remove(error);
        }
      }
    }
    super.handleErrors();
  }
}
