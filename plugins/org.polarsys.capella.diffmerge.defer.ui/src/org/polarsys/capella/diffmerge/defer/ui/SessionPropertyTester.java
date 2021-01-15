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

import java.util.Collection;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;

/**
 * 
 * @author Ali AKAR
 *
 */
public class SessionPropertyTester extends PropertyTester {

  /**
   * @see org.eclipse.core.expressions.IPropertyTester#test(java.lang.Object,
   *      java.lang.String, java.lang.Object[], java.lang.Object)
   */
  @SuppressWarnings("rawtypes")
  public boolean test(Object receiver, String property, Object[] args,
      Object expectedValue) {
    if (receiver instanceof Collection && !((Collection) receiver).isEmpty()) {
      Object next = ((Collection) receiver).iterator().next();
      if (next instanceof EObject) {
        Session session = SessionManager.INSTANCE.getSession((EObject) next);
        return session == null;
      }
    }
    return false;
  }
}
