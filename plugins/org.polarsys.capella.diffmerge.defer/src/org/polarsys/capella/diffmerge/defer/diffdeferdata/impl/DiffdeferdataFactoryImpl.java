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
package org.polarsys.capella.diffmerge.defer.diffdeferdata.impl;

import org.polarsys.capella.diffmerge.defer.diffdeferdata.*;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.polarsys.capella.diffmerge.defer.diffdeferdata.DeferredComparison;
import org.polarsys.capella.diffmerge.defer.diffdeferdata.DiffdeferdataFactory;
import org.polarsys.capella.diffmerge.defer.diffdeferdata.DiffdeferdataPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DiffdeferdataFactoryImpl extends EFactoryImpl
    implements DiffdeferdataFactory {
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static DiffdeferdataFactory init() {
    try {
      DiffdeferdataFactory theDiffdeferdataFactory = (DiffdeferdataFactory) EPackage.Registry.INSTANCE
          .getEFactory(DiffdeferdataPackage.eNS_URI);
      if (theDiffdeferdataFactory != null) {
        return theDiffdeferdataFactory;
      }
    } catch (Exception exception) {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new DiffdeferdataFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DiffdeferdataFactoryImpl() {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EObject create(EClass eClass) {
    switch (eClass.getClassifierID()) {
    case DiffdeferdataPackage.DEFERRED_COMPARISON:
      return createDeferredComparison();
    default:
      throw new IllegalArgumentException(
          "The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DeferredComparison createDeferredComparison() {
    DeferredComparisonImpl deferredComparison = new DeferredComparisonImpl();
    return deferredComparison;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DiffdeferdataPackage getDiffdeferdataPackage() {
    return (DiffdeferdataPackage) getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @SuppressWarnings("javadoc")
  @Deprecated
  public static DiffdeferdataPackage getPackage() {
    return DiffdeferdataPackage.eINSTANCE;
  }

} //DiffdeferdataFactoryImpl
