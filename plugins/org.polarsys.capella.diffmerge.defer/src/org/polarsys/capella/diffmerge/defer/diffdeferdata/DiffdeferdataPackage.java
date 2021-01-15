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
package org.polarsys.capella.diffmerge.defer.diffdeferdata;

import org.eclipse.emf.diffmerge.generic.gdiffdata.GdiffdataPackage;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.polarsys.capella.diffmerge.defer.diffdeferdata.DiffdeferdataFactory
 * @model kind="package"
 * @generated
 */
public interface DiffdeferdataPackage extends EPackage {
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "diffdeferdata"; //$NON-NLS-1$

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.eclipse.org/emf/diffmerge/defer/1.0.0/diffdeferdata"; //$NON-NLS-1$

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "org.polarsys.capella.diffmerge.defer"; //$NON-NLS-1$

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  DiffdeferdataPackage eINSTANCE = org.polarsys.capella.diffmerge.defer.diffdeferdata.impl.DiffdeferdataPackageImpl
      .init();

  /**
   * The meta object id for the '{@link org.polarsys.capella.diffmerge.defer.diffdeferdata.impl.DeferredComparisonImpl <em>Deferred Comparison</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.polarsys.capella.diffmerge.defer.diffdeferdata.impl.DeferredComparisonImpl
   * @see org.polarsys.capella.diffmerge.defer.diffdeferdata.impl.DiffdeferdataPackageImpl#getDeferredComparison()
   * @generated
   */
  int DEFERRED_COMPARISON = 0;

  /**
   * The feature id for the '<em><b>Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFERRED_COMPARISON__ID = GdiffdataPackage.GIDENTIFIED__ID;

  /**
   * The feature id for the '<em><b>Comparison Method Factory ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFERRED_COMPARISON__COMPARISON_METHOD_FACTORY_ID = GdiffdataPackage.GIDENTIFIED_FEATURE_COUNT
      + 0;

  /**
   * The feature id for the '<em><b>Target Scope Definition URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFERRED_COMPARISON__TARGET_SCOPE_DEFINITION_URI = GdiffdataPackage.GIDENTIFIED_FEATURE_COUNT
      + 1;

  /**
   * The feature id for the '<em><b>Actual Comparison</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFERRED_COMPARISON__ACTUAL_COMPARISON = GdiffdataPackage.GIDENTIFIED_FEATURE_COUNT
      + 2;

  /**
   * The feature id for the '<em><b>Transient Data</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFERRED_COMPARISON__TRANSIENT_DATA = GdiffdataPackage.GIDENTIFIED_FEATURE_COUNT
      + 3;

  /**
   * The feature id for the '<em><b>Logical Source Roots</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFERRED_COMPARISON__LOGICAL_SOURCE_ROOTS = GdiffdataPackage.GIDENTIFIED_FEATURE_COUNT
      + 4;

  /**
   * The feature id for the '<em><b>Technical Source Roots</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFERRED_COMPARISON__TECHNICAL_SOURCE_ROOTS = GdiffdataPackage.GIDENTIFIED_FEATURE_COUNT
      + 5;

  /**
   * The number of structural features of the '<em>Deferred Comparison</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFERRED_COMPARISON_FEATURE_COUNT = GdiffdataPackage.GIDENTIFIED_FEATURE_COUNT
      + 6;

  /**
   * Returns the meta object for class '{@link org.polarsys.capella.diffmerge.defer.diffdeferdata.DeferredComparison <em>Deferred Comparison</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Deferred Comparison</em>'.
   * @see org.polarsys.capella.diffmerge.defer.diffdeferdata.DeferredComparison
   * @generated
   */
  EClass getDeferredComparison();

  /**
   * Returns the meta object for the attribute '{@link org.polarsys.capella.diffmerge.defer.diffdeferdata.DeferredComparison#getComparisonMethodFactoryID <em>Comparison Method Factory ID</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Comparison Method Factory ID</em>'.
   * @see org.polarsys.capella.diffmerge.defer.diffdeferdata.DeferredComparison#getComparisonMethodFactoryID()
   * @see #getDeferredComparison()
   * @generated
   */
  EAttribute getDeferredComparison_ComparisonMethodFactoryID();

  /**
   * Returns the meta object for the attribute '{@link org.polarsys.capella.diffmerge.defer.diffdeferdata.DeferredComparison#getTargetScopeDefinitionURI <em>Target Scope Definition URI</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Target Scope Definition URI</em>'.
   * @see org.polarsys.capella.diffmerge.defer.diffdeferdata.DeferredComparison#getTargetScopeDefinitionURI()
   * @see #getDeferredComparison()
   * @generated
   */
  EAttribute getDeferredComparison_TargetScopeDefinitionURI();

  /**
   * Returns the meta object for the containment reference '{@link org.polarsys.capella.diffmerge.defer.diffdeferdata.DeferredComparison#getActualComparison <em>Actual Comparison</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Actual Comparison</em>'.
   * @see org.polarsys.capella.diffmerge.defer.diffdeferdata.DeferredComparison#getActualComparison()
   * @see #getDeferredComparison()
   * @generated
   */
  EReference getDeferredComparison_ActualComparison();

  /**
   * Returns the meta object for the containment reference '{@link org.polarsys.capella.diffmerge.defer.diffdeferdata.DeferredComparison#getTransientData <em>Transient Data</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Transient Data</em>'.
   * @see org.polarsys.capella.diffmerge.defer.diffdeferdata.DeferredComparison#getTransientData()
   * @see #getDeferredComparison()
   * @generated
   */
  EReference getDeferredComparison_TransientData();

  /**
   * Returns the meta object for the containment reference list '{@link org.polarsys.capella.diffmerge.defer.diffdeferdata.DeferredComparison#getLogicalSourceRoots <em>Logical Source Roots</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Logical Source Roots</em>'.
   * @see org.polarsys.capella.diffmerge.defer.diffdeferdata.DeferredComparison#getLogicalSourceRoots()
   * @see #getDeferredComparison()
   * @generated
   */
  EReference getDeferredComparison_LogicalSourceRoots();

  /**
   * Returns the meta object for the containment reference list '{@link org.polarsys.capella.diffmerge.defer.diffdeferdata.DeferredComparison#getTechnicalSourceRoots <em>Technical Source Roots</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Technical Source Roots</em>'.
   * @see org.polarsys.capella.diffmerge.defer.diffdeferdata.DeferredComparison#getTechnicalSourceRoots()
   * @see #getDeferredComparison()
   * @generated
   */
  EReference getDeferredComparison_TechnicalSourceRoots();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  DiffdeferdataFactory getDiffdeferdataFactory();

  /**
   * <!-- begin-user-doc -->
   * Defines literals for the meta objects that represent
   * <ul>
   *   <li>each class,</li>
   *   <li>each feature of each class,</li>
   *   <li>each operation of each class,</li>
   *   <li>each enum,</li>
   *   <li>and each data type</li>
   * </ul>
   * <!-- end-user-doc -->
   * @generated
   */
  interface Literals {
    /**
     * The meta object literal for the '{@link org.polarsys.capella.diffmerge.defer.diffdeferdata.impl.DeferredComparisonImpl <em>Deferred Comparison</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.polarsys.capella.diffmerge.defer.diffdeferdata.impl.DeferredComparisonImpl
     * @see org.polarsys.capella.diffmerge.defer.diffdeferdata.impl.DiffdeferdataPackageImpl#getDeferredComparison()
     * @generated
     */
    EClass DEFERRED_COMPARISON = eINSTANCE.getDeferredComparison();

    /**
     * The meta object literal for the '<em><b>Comparison Method Factory ID</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DEFERRED_COMPARISON__COMPARISON_METHOD_FACTORY_ID = eINSTANCE
        .getDeferredComparison_ComparisonMethodFactoryID();

    /**
     * The meta object literal for the '<em><b>Target Scope Definition URI</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DEFERRED_COMPARISON__TARGET_SCOPE_DEFINITION_URI = eINSTANCE
        .getDeferredComparison_TargetScopeDefinitionURI();

    /**
     * The meta object literal for the '<em><b>Actual Comparison</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DEFERRED_COMPARISON__ACTUAL_COMPARISON = eINSTANCE
        .getDeferredComparison_ActualComparison();

    /**
     * The meta object literal for the '<em><b>Transient Data</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DEFERRED_COMPARISON__TRANSIENT_DATA = eINSTANCE
        .getDeferredComparison_TransientData();

    /**
     * The meta object literal for the '<em><b>Logical Source Roots</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DEFERRED_COMPARISON__LOGICAL_SOURCE_ROOTS = eINSTANCE
        .getDeferredComparison_LogicalSourceRoots();

    /**
     * The meta object literal for the '<em><b>Technical Source Roots</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DEFERRED_COMPARISON__TECHNICAL_SOURCE_ROOTS = eINSTANCE
        .getDeferredComparison_TechnicalSourceRoots();

  }

} //DiffdeferdataPackage
