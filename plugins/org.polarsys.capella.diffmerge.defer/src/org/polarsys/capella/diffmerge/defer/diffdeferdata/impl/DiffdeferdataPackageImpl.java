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

import org.eclipse.emf.diffmerge.diffdata.DiffdataPackage;
import org.eclipse.emf.diffmerge.generic.gdiffdata.GdiffdataPackage;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.polarsys.capella.diffmerge.defer.diffdeferdata.DeferredComparison;
import org.polarsys.capella.diffmerge.defer.diffdeferdata.DiffdeferdataFactory;
import org.polarsys.capella.diffmerge.defer.diffdeferdata.DiffdeferdataPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DiffdeferdataPackageImpl extends EPackageImpl
    implements DiffdeferdataPackage {
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass deferredComparisonEClass = null;

  /**
   * Creates an instance of the model <b>Package</b>, registered with
   * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
   * package URI value.
   * <p>Note: the correct way to create the package is via the static
   * factory method {@link #init init()}, which also performs
   * initialization of the package, or returns the registered package,
   * if one already exists.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.emf.ecore.EPackage.Registry
   * @see org.polarsys.capella.diffmerge.defer.diffdeferdata.DiffdeferdataPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private DiffdeferdataPackageImpl() {
    super(eNS_URI, DiffdeferdataFactory.eINSTANCE);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static boolean isInited = false;

  /**
   * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
   *
   * <p>This method is used to initialize {@link DiffdeferdataPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static DiffdeferdataPackage init() {
    if (isInited)
      return (DiffdeferdataPackage) EPackage.Registry.INSTANCE
          .getEPackage(DiffdeferdataPackage.eNS_URI);

    // Obtain or create and register package
    Object registeredDiffdeferdataPackage = EPackage.Registry.INSTANCE
        .get(eNS_URI);
    DiffdeferdataPackageImpl theDiffdeferdataPackage = registeredDiffdeferdataPackage instanceof DiffdeferdataPackageImpl
        ? (DiffdeferdataPackageImpl) registeredDiffdeferdataPackage
        : new DiffdeferdataPackageImpl();

    isInited = true;

    // Initialize simple dependencies
    DiffdataPackage.eINSTANCE.eClass();
    EcorePackage.eINSTANCE.eClass();
    GdiffdataPackage.eINSTANCE.eClass();

    // Create package meta-data objects
    theDiffdeferdataPackage.createPackageContents();

    // Initialize created meta-data
    theDiffdeferdataPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theDiffdeferdataPackage.freeze();

    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(DiffdeferdataPackage.eNS_URI,
        theDiffdeferdataPackage);
    return theDiffdeferdataPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getDeferredComparison() {
    return deferredComparisonEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDeferredComparison_ComparisonMethodFactoryID() {
    return (EAttribute) deferredComparisonEClass.getEStructuralFeatures()
        .get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDeferredComparison_TargetScopeDefinitionURI() {
    return (EAttribute) deferredComparisonEClass.getEStructuralFeatures()
        .get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDeferredComparison_ActualComparison() {
    return (EReference) deferredComparisonEClass.getEStructuralFeatures()
        .get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDeferredComparison_TransientData() {
    return (EReference) deferredComparisonEClass.getEStructuralFeatures()
        .get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDeferredComparison_LogicalSourceRoots() {
    return (EReference) deferredComparisonEClass.getEStructuralFeatures()
        .get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDeferredComparison_TechnicalSourceRoots() {
    return (EReference) deferredComparisonEClass.getEStructuralFeatures()
        .get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DiffdeferdataFactory getDiffdeferdataFactory() {
    return (DiffdeferdataFactory) getEFactoryInstance();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isCreated = false;

  /**
   * Creates the meta-model objects for the package.  This method is
   * guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void createPackageContents() {
    if (isCreated)
      return;
    isCreated = true;

    // Create classes and their features
    deferredComparisonEClass = createEClass(DEFERRED_COMPARISON);
    createEAttribute(deferredComparisonEClass,
        DEFERRED_COMPARISON__COMPARISON_METHOD_FACTORY_ID);
    createEAttribute(deferredComparisonEClass,
        DEFERRED_COMPARISON__TARGET_SCOPE_DEFINITION_URI);
    createEReference(deferredComparisonEClass,
        DEFERRED_COMPARISON__ACTUAL_COMPARISON);
    createEReference(deferredComparisonEClass,
        DEFERRED_COMPARISON__TRANSIENT_DATA);
    createEReference(deferredComparisonEClass,
        DEFERRED_COMPARISON__LOGICAL_SOURCE_ROOTS);
    createEReference(deferredComparisonEClass,
        DEFERRED_COMPARISON__TECHNICAL_SOURCE_ROOTS);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isInitialized = false;

  /**
   * Complete the initialization of the package and its meta-model.  This
   * method is guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void initializePackageContents() {
    if (isInitialized)
      return;
    isInitialized = true;

    // Initialize package
    setName(eNAME);
    setNsPrefix(eNS_PREFIX);
    setNsURI(eNS_URI);

    // Obtain other dependent packages
    GdiffdataPackage theGdiffdataPackage = (GdiffdataPackage) EPackage.Registry.INSTANCE
        .getEPackage(GdiffdataPackage.eNS_URI);
    EcorePackage theEcorePackage = (EcorePackage) EPackage.Registry.INSTANCE
        .getEPackage(EcorePackage.eNS_URI);
    DiffdataPackage theDiffdataPackage = (DiffdataPackage) EPackage.Registry.INSTANCE
        .getEPackage(DiffdataPackage.eNS_URI);

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    deferredComparisonEClass.getESuperTypes()
        .add(theGdiffdataPackage.getGIdentified());

    // Initialize classes and features; add operations and parameters
    initEClass(deferredComparisonEClass, DeferredComparison.class,
        "DeferredComparison", !IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
        IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getDeferredComparison_ComparisonMethodFactoryID(),
        theEcorePackage.getEString(), "comparisonMethodFactoryID", null, 1, 1, //$NON-NLS-1$
        DeferredComparison.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDeferredComparison_TargetScopeDefinitionURI(),
        theEcorePackage.getEString(), "targetScopeDefinitionURI", null, 1, 1, //$NON-NLS-1$
        DeferredComparison.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getDeferredComparison_ActualComparison(),
        theDiffdataPackage.getEComparison(), null, "actualComparison", null, 1, //$NON-NLS-1$
        1, DeferredComparison.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
        !IS_DERIVED, IS_ORDERED);
    initEReference(getDeferredComparison_TransientData(),
        ecorePackage.getEObject(), null, "transientData", null, 0, 1, //$NON-NLS-1$
        DeferredComparison.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
        !IS_DERIVED, IS_ORDERED);
    initEReference(getDeferredComparison_LogicalSourceRoots(),
        theEcorePackage.getEObject(), null, "logicalSourceRoots", null, 0, -1, //$NON-NLS-1$
        DeferredComparison.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
        !IS_DERIVED, IS_ORDERED);
    initEReference(getDeferredComparison_TechnicalSourceRoots(),
        ecorePackage.getEObject(), null, "technicalSourceRoots", null, 0, -1, //$NON-NLS-1$
        DeferredComparison.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE,
        !IS_DERIVED, IS_ORDERED);

    addEOperation(deferredComparisonEClass, null, "dispose", 0, 1, IS_UNIQUE, //$NON-NLS-1$
        IS_ORDERED);

    // Create resource
    createResource(eNS_URI);
  }

} //DiffdeferdataPackageImpl
