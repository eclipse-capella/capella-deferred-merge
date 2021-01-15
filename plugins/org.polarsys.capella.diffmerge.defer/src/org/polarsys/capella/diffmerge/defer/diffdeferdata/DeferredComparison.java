/*******************************************************************************
 * Copyright (c) 2018, 2021 THALES GLOBAL SERVICES.
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

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.diffmerge.diffdata.EComparison;
import org.eclipse.emf.diffmerge.generic.gdiffdata.GIdentified;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Deferred Comparison</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.polarsys.capella.diffmerge.defer.diffdeferdata.DeferredComparison#getComparisonMethodFactoryID <em>Comparison Method Factory ID</em>}</li>
 *   <li>{@link org.polarsys.capella.diffmerge.defer.diffdeferdata.DeferredComparison#getTargetScopeDefinitionURI <em>Target Scope Definition URI</em>}</li>
 *   <li>{@link org.polarsys.capella.diffmerge.defer.diffdeferdata.DeferredComparison#getActualComparison <em>Actual Comparison</em>}</li>
 *   <li>{@link org.polarsys.capella.diffmerge.defer.diffdeferdata.DeferredComparison#getTransientData <em>Transient Data</em>}</li>
 *   <li>{@link org.polarsys.capella.diffmerge.defer.diffdeferdata.DeferredComparison#getLogicalSourceRoots <em>Logical Source Roots</em>}</li>
 *   <li>{@link org.polarsys.capella.diffmerge.defer.diffdeferdata.DeferredComparison#getTechnicalSourceRoots <em>Technical Source Roots</em>}</li>
 * </ul>
 *
 * @see org.polarsys.capella.diffmerge.defer.diffdeferdata.DiffdeferdataPackage#getDeferredComparison()
 * @model
 * @generated
 */
public interface DeferredComparison extends GIdentified {
  /**
   * Returns the value of the '<em><b>Comparison Method Factory ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Comparison Method Factory ID</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Comparison Method Factory ID</em>' attribute.
   * @see #setComparisonMethodFactoryID(String)
   * @see org.polarsys.capella.diffmerge.defer.diffdeferdata.DiffdeferdataPackage#getDeferredComparison_ComparisonMethodFactoryID()
   * @model required="true"
   * @generated
   */
  String getComparisonMethodFactoryID();

  /**
   * Sets the value of the '{@link org.polarsys.capella.diffmerge.defer.diffdeferdata.DeferredComparison#getComparisonMethodFactoryID <em>Comparison Method Factory ID</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Comparison Method Factory ID</em>' attribute.
   * @see #getComparisonMethodFactoryID()
   * @generated
   */
  void setComparisonMethodFactoryID(String value);

  /**
   * Returns the value of the '<em><b>Target Scope Definition URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Target Scope Definition URI</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Target Scope Definition URI</em>' attribute.
   * @see #setTargetScopeDefinitionURI(String)
   * @see org.polarsys.capella.diffmerge.defer.diffdeferdata.DiffdeferdataPackage#getDeferredComparison_TargetScopeDefinitionURI()
   * @model required="true"
   * @generated
   */
  String getTargetScopeDefinitionURI();

  /**
   * Sets the value of the '{@link org.polarsys.capella.diffmerge.defer.diffdeferdata.DeferredComparison#getTargetScopeDefinitionURI <em>Target Scope Definition URI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Target Scope Definition URI</em>' attribute.
   * @see #getTargetScopeDefinitionURI()
   * @generated
   */
  void setTargetScopeDefinitionURI(String value);

  /**
   * Returns the value of the '<em><b>Actual Comparison</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Actual Comparison</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Actual Comparison</em>' containment reference.
   * @see #setActualComparison(EComparison)
   * @see org.polarsys.capella.diffmerge.defer.diffdeferdata.DiffdeferdataPackage#getDeferredComparison_ActualComparison()
   * @model containment="true" resolveProxies="true" required="true"
   * @generated
   */
  EComparison getActualComparison();

  /**
   * Sets the value of the '{@link org.polarsys.capella.diffmerge.defer.diffdeferdata.DeferredComparison#getActualComparison <em>Actual Comparison</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Actual Comparison</em>' containment reference.
   * @see #getActualComparison()
   * @generated
   */
  void setActualComparison(EComparison value);

  /**
   * Returns the value of the '<em><b>Transient Data</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Transient Data</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Transient Data</em>' containment reference.
   * @see #setTransientData(EObject)
   * @see org.polarsys.capella.diffmerge.defer.diffdeferdata.DiffdeferdataPackage#getDeferredComparison_TransientData()
   * @model containment="true" resolveProxies="true" transient="true"
   * @generated
   */
  EObject getTransientData();

  /**
   * Sets the value of the '{@link org.polarsys.capella.diffmerge.defer.diffdeferdata.DeferredComparison#getTransientData <em>Transient Data</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Transient Data</em>' containment reference.
   * @see #getTransientData()
   * @generated
   */
  void setTransientData(EObject value);

  /**
   * Returns the value of the '<em><b>Logical Source Roots</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Logical Source Roots</em>' reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Logical Source Roots</em>' containment reference list.
   * @see org.polarsys.capella.diffmerge.defer.diffdeferdata.DiffdeferdataPackage#getDeferredComparison_LogicalSourceRoots()
   * @model containment="true" resolveProxies="true"
   * @generated
   */
  EList<EObject> getLogicalSourceRoots();

  /**
   * Returns the value of the '<em><b>Technical Source Roots</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Technical Source Roots</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Technical Source Roots</em>' containment reference list.
   * @see org.polarsys.capella.diffmerge.defer.diffdeferdata.DiffdeferdataPackage#getDeferredComparison_TechnicalSourceRoots()
   * @model containment="true" resolveProxies="true"
   * @generated
   */
  EList<EObject> getTechnicalSourceRoots();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model
   * @generated
   */
  void dispose();

} // DeferredComparison
