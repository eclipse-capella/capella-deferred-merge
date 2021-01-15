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

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.diffmerge.diffdata.EComparison;
import org.eclipse.emf.diffmerge.generic.gdiffdata.impl.GIdentifiedImpl;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.edit.provider.IDisposable;

import org.polarsys.capella.diffmerge.defer.diffdeferdata.DeferredComparison;
import org.polarsys.capella.diffmerge.defer.diffdeferdata.DiffdeferdataPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Deferred Comparison</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.polarsys.capella.diffmerge.defer.diffdeferdata.impl.DeferredComparisonImpl#getComparisonMethodFactoryID <em>Comparison Method Factory ID</em>}</li>
 *   <li>{@link org.polarsys.capella.diffmerge.defer.diffdeferdata.impl.DeferredComparisonImpl#getTargetScopeDefinitionURI <em>Target Scope Definition URI</em>}</li>
 *   <li>{@link org.polarsys.capella.diffmerge.defer.diffdeferdata.impl.DeferredComparisonImpl#getActualComparison <em>Actual Comparison</em>}</li>
 *   <li>{@link org.polarsys.capella.diffmerge.defer.diffdeferdata.impl.DeferredComparisonImpl#getTransientData <em>Transient Data</em>}</li>
 *   <li>{@link org.polarsys.capella.diffmerge.defer.diffdeferdata.impl.DeferredComparisonImpl#getLogicalSourceRoots <em>Logical Source Roots</em>}</li>
 *   <li>{@link org.polarsys.capella.diffmerge.defer.diffdeferdata.impl.DeferredComparisonImpl#getTechnicalSourceRoots <em>Technical Source Roots</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DeferredComparisonImpl extends GIdentifiedImpl
    implements DeferredComparison {
  /**
   * The default value of the '{@link #getComparisonMethodFactoryID() <em>Comparison Method Factory ID</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getComparisonMethodFactoryID()
   * @generated
   * @ordered
   */
  protected static final String COMPARISON_METHOD_FACTORY_ID_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getComparisonMethodFactoryID() <em>Comparison Method Factory ID</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getComparisonMethodFactoryID()
   * @generated
   * @ordered
   */
  protected String comparisonMethodFactoryID = COMPARISON_METHOD_FACTORY_ID_EDEFAULT;

  /**
   * The default value of the '{@link #getTargetScopeDefinitionURI() <em>Target Scope Definition URI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTargetScopeDefinitionURI()
   * @generated
   * @ordered
   */
  protected static final String TARGET_SCOPE_DEFINITION_URI_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getTargetScopeDefinitionURI() <em>Target Scope Definition URI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTargetScopeDefinitionURI()
   * @generated
   * @ordered
   */
  protected String targetScopeDefinitionURI = TARGET_SCOPE_DEFINITION_URI_EDEFAULT;

  /**
   * The cached value of the '{@link #getActualComparison() <em>Actual Comparison</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getActualComparison()
   * @generated
   * @ordered
   */
  protected EComparison actualComparison;

  /**
   * The cached value of the '{@link #getTransientData() <em>Transient Data</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTransientData()
   * @generated
   * @ordered
   */
  protected EObject transientData;

  /**
   * The cached value of the '{@link #getLogicalSourceRoots() <em>Logical Source Roots</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLogicalSourceRoots()
   * @generated
   * @ordered
   */
  protected EList<EObject> logicalSourceRoots;

  /**
   * The cached value of the '{@link #getTechnicalSourceRoots() <em>Technical Source Roots</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTechnicalSourceRoots()
   * @generated
   * @ordered
   */
  protected EList<EObject> technicalSourceRoots;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected DeferredComparisonImpl() {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass() {
    return DiffdeferdataPackage.Literals.DEFERRED_COMPARISON;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getComparisonMethodFactoryID() {
    return comparisonMethodFactoryID;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setComparisonMethodFactoryID(
      String newComparisonMethodFactoryID) {
    String oldComparisonMethodFactoryID = comparisonMethodFactoryID;
    comparisonMethodFactoryID = newComparisonMethodFactoryID;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET,
          DiffdeferdataPackage.DEFERRED_COMPARISON__COMPARISON_METHOD_FACTORY_ID,
          oldComparisonMethodFactoryID, comparisonMethodFactoryID));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getTargetScopeDefinitionURI() {
    return targetScopeDefinitionURI;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTargetScopeDefinitionURI(String newTargetScopeDefinitionURI) {
    String oldTargetScopeDefinitionURI = targetScopeDefinitionURI;
    targetScopeDefinitionURI = newTargetScopeDefinitionURI;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET,
          DiffdeferdataPackage.DEFERRED_COMPARISON__TARGET_SCOPE_DEFINITION_URI,
          oldTargetScopeDefinitionURI, targetScopeDefinitionURI));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EComparison getActualComparison() {
    if (actualComparison != null && actualComparison.eIsProxy()) {
      InternalEObject oldActualComparison = (InternalEObject) actualComparison;
      actualComparison = (EComparison) eResolveProxy(oldActualComparison);
      if (actualComparison != oldActualComparison) {
        InternalEObject newActualComparison = (InternalEObject) actualComparison;
        NotificationChain msgs = oldActualComparison.eInverseRemove(this,
            EOPPOSITE_FEATURE_BASE
                - DiffdeferdataPackage.DEFERRED_COMPARISON__ACTUAL_COMPARISON,
            null, null);
        if (newActualComparison.eInternalContainer() == null) {
          msgs = newActualComparison.eInverseAdd(this,
              EOPPOSITE_FEATURE_BASE
                  - DiffdeferdataPackage.DEFERRED_COMPARISON__ACTUAL_COMPARISON,
              null, msgs);
        }
        if (msgs != null)
          msgs.dispatch();
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE,
              DiffdeferdataPackage.DEFERRED_COMPARISON__ACTUAL_COMPARISON,
              oldActualComparison, actualComparison));
      }
    }
    return actualComparison;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EComparison basicGetActualComparison() {
    return actualComparison;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public NotificationChain basicSetActualComparison(
      EComparison newActualComparison, NotificationChain notificationChain) {
    NotificationChain msgs = notificationChain;
    EComparison oldActualComparison = actualComparison;
    actualComparison = newActualComparison;
    if (eNotificationRequired()) {
      ENotificationImpl notification = new ENotificationImpl(this,
          Notification.SET,
          DiffdeferdataPackage.DEFERRED_COMPARISON__ACTUAL_COMPARISON,
          oldActualComparison, newActualComparison);
      if (msgs == null)
        msgs = notification;
      else
        msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setActualComparison(EComparison newActualComparison) {
    if (newActualComparison != actualComparison) {
      NotificationChain msgs = null;
      if (actualComparison != null)
        msgs = ((InternalEObject) actualComparison).eInverseRemove(this,
            EOPPOSITE_FEATURE_BASE
                - DiffdeferdataPackage.DEFERRED_COMPARISON__ACTUAL_COMPARISON,
            null, msgs);
      if (newActualComparison != null)
        msgs = ((InternalEObject) newActualComparison).eInverseAdd(this,
            EOPPOSITE_FEATURE_BASE
                - DiffdeferdataPackage.DEFERRED_COMPARISON__ACTUAL_COMPARISON,
            null, msgs);
      msgs = basicSetActualComparison(newActualComparison, msgs);
      if (msgs != null)
        msgs.dispatch();
    } else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET,
          DiffdeferdataPackage.DEFERRED_COMPARISON__ACTUAL_COMPARISON,
          newActualComparison, newActualComparison));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EObject getTransientData() {
    if (transientData != null && transientData.eIsProxy()) {
      InternalEObject oldTransientData = (InternalEObject) transientData;
      transientData = eResolveProxy(oldTransientData);
      if (transientData != oldTransientData) {
        InternalEObject newTransientData = (InternalEObject) transientData;
        NotificationChain msgs = oldTransientData.eInverseRemove(this,
            EOPPOSITE_FEATURE_BASE
                - DiffdeferdataPackage.DEFERRED_COMPARISON__TRANSIENT_DATA,
            null, null);
        if (newTransientData.eInternalContainer() == null) {
          msgs = newTransientData.eInverseAdd(this,
              EOPPOSITE_FEATURE_BASE
                  - DiffdeferdataPackage.DEFERRED_COMPARISON__TRANSIENT_DATA,
              null, msgs);
        }
        if (msgs != null)
          msgs.dispatch();
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE,
              DiffdeferdataPackage.DEFERRED_COMPARISON__TRANSIENT_DATA,
              oldTransientData, transientData));
      }
    }
    return transientData;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EObject basicGetTransientData() {
    return transientData;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public NotificationChain basicSetTransientData(EObject newTransientData,
      NotificationChain notificationChain) {
    NotificationChain msgs = notificationChain;
    EObject oldTransientData = transientData;
    transientData = newTransientData;
    if (eNotificationRequired()) {
      ENotificationImpl notification = new ENotificationImpl(this,
          Notification.SET,
          DiffdeferdataPackage.DEFERRED_COMPARISON__TRANSIENT_DATA,
          oldTransientData, newTransientData);
      if (msgs == null)
        msgs = notification;
      else
        msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTransientData(EObject newTransientData) {
    if (newTransientData != transientData) {
      NotificationChain msgs = null;
      if (transientData != null)
        msgs = ((InternalEObject) transientData).eInverseRemove(this,
            EOPPOSITE_FEATURE_BASE
                - DiffdeferdataPackage.DEFERRED_COMPARISON__TRANSIENT_DATA,
            null, msgs);
      if (newTransientData != null)
        msgs = ((InternalEObject) newTransientData).eInverseAdd(this,
            EOPPOSITE_FEATURE_BASE
                - DiffdeferdataPackage.DEFERRED_COMPARISON__TRANSIENT_DATA,
            null, msgs);
      msgs = basicSetTransientData(newTransientData, msgs);
      if (msgs != null)
        msgs.dispatch();
    } else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET,
          DiffdeferdataPackage.DEFERRED_COMPARISON__TRANSIENT_DATA,
          newTransientData, newTransientData));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<EObject> getLogicalSourceRoots() {
    if (logicalSourceRoots == null) {
      logicalSourceRoots = new EObjectContainmentEList.Resolving<EObject>(
          EObject.class, this,
          DiffdeferdataPackage.DEFERRED_COMPARISON__LOGICAL_SOURCE_ROOTS);
    }
    return logicalSourceRoots;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<EObject> getTechnicalSourceRoots() {
    if (technicalSourceRoots == null) {
      technicalSourceRoots = new EObjectContainmentEList.Resolving<EObject>(
          EObject.class, this,
          DiffdeferdataPackage.DEFERRED_COMPARISON__TECHNICAL_SOURCE_ROOTS);
    }
    return technicalSourceRoots;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void dispose() {
    EObject transientData = getTransientData();
    if (transientData instanceof IDisposable) {
      ((IDisposable) transientData).dispose();
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd,
      int featureID, NotificationChain msgs) {
    switch (featureID) {
    case DiffdeferdataPackage.DEFERRED_COMPARISON__ACTUAL_COMPARISON:
      return basicSetActualComparison(null, msgs);
    case DiffdeferdataPackage.DEFERRED_COMPARISON__TRANSIENT_DATA:
      return basicSetTransientData(null, msgs);
    case DiffdeferdataPackage.DEFERRED_COMPARISON__LOGICAL_SOURCE_ROOTS:
      return ((InternalEList<?>) getLogicalSourceRoots()).basicRemove(otherEnd,
          msgs);
    case DiffdeferdataPackage.DEFERRED_COMPARISON__TECHNICAL_SOURCE_ROOTS:
      return ((InternalEList<?>) getTechnicalSourceRoots())
          .basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType) {
    switch (featureID) {
    case DiffdeferdataPackage.DEFERRED_COMPARISON__COMPARISON_METHOD_FACTORY_ID:
      return getComparisonMethodFactoryID();
    case DiffdeferdataPackage.DEFERRED_COMPARISON__TARGET_SCOPE_DEFINITION_URI:
      return getTargetScopeDefinitionURI();
    case DiffdeferdataPackage.DEFERRED_COMPARISON__ACTUAL_COMPARISON:
      if (resolve)
        return getActualComparison();
      return basicGetActualComparison();
    case DiffdeferdataPackage.DEFERRED_COMPARISON__TRANSIENT_DATA:
      if (resolve)
        return getTransientData();
      return basicGetTransientData();
    case DiffdeferdataPackage.DEFERRED_COMPARISON__LOGICAL_SOURCE_ROOTS:
      return getLogicalSourceRoots();
    case DiffdeferdataPackage.DEFERRED_COMPARISON__TECHNICAL_SOURCE_ROOTS:
      return getTechnicalSourceRoots();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue) {
    switch (featureID) {
    case DiffdeferdataPackage.DEFERRED_COMPARISON__COMPARISON_METHOD_FACTORY_ID:
      setComparisonMethodFactoryID((String) newValue);
      return;
    case DiffdeferdataPackage.DEFERRED_COMPARISON__TARGET_SCOPE_DEFINITION_URI:
      setTargetScopeDefinitionURI((String) newValue);
      return;
    case DiffdeferdataPackage.DEFERRED_COMPARISON__ACTUAL_COMPARISON:
      setActualComparison((EComparison) newValue);
      return;
    case DiffdeferdataPackage.DEFERRED_COMPARISON__TRANSIENT_DATA:
      setTransientData((EObject) newValue);
      return;
    case DiffdeferdataPackage.DEFERRED_COMPARISON__LOGICAL_SOURCE_ROOTS:
      getLogicalSourceRoots().clear();
      getLogicalSourceRoots().addAll((Collection<? extends EObject>) newValue);
      return;
    case DiffdeferdataPackage.DEFERRED_COMPARISON__TECHNICAL_SOURCE_ROOTS:
      getTechnicalSourceRoots().clear();
      getTechnicalSourceRoots()
          .addAll((Collection<? extends EObject>) newValue);
      return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID) {
    switch (featureID) {
    case DiffdeferdataPackage.DEFERRED_COMPARISON__COMPARISON_METHOD_FACTORY_ID:
      setComparisonMethodFactoryID(COMPARISON_METHOD_FACTORY_ID_EDEFAULT);
      return;
    case DiffdeferdataPackage.DEFERRED_COMPARISON__TARGET_SCOPE_DEFINITION_URI:
      setTargetScopeDefinitionURI(TARGET_SCOPE_DEFINITION_URI_EDEFAULT);
      return;
    case DiffdeferdataPackage.DEFERRED_COMPARISON__ACTUAL_COMPARISON:
      setActualComparison((EComparison) null);
      return;
    case DiffdeferdataPackage.DEFERRED_COMPARISON__TRANSIENT_DATA:
      setTransientData((EObject) null);
      return;
    case DiffdeferdataPackage.DEFERRED_COMPARISON__LOGICAL_SOURCE_ROOTS:
      getLogicalSourceRoots().clear();
      return;
    case DiffdeferdataPackage.DEFERRED_COMPARISON__TECHNICAL_SOURCE_ROOTS:
      getTechnicalSourceRoots().clear();
      return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID) {
    switch (featureID) {
    case DiffdeferdataPackage.DEFERRED_COMPARISON__COMPARISON_METHOD_FACTORY_ID:
      return COMPARISON_METHOD_FACTORY_ID_EDEFAULT == null
          ? comparisonMethodFactoryID != null
          : !COMPARISON_METHOD_FACTORY_ID_EDEFAULT
              .equals(comparisonMethodFactoryID);
    case DiffdeferdataPackage.DEFERRED_COMPARISON__TARGET_SCOPE_DEFINITION_URI:
      return TARGET_SCOPE_DEFINITION_URI_EDEFAULT == null
          ? targetScopeDefinitionURI != null
          : !TARGET_SCOPE_DEFINITION_URI_EDEFAULT
              .equals(targetScopeDefinitionURI);
    case DiffdeferdataPackage.DEFERRED_COMPARISON__ACTUAL_COMPARISON:
      return actualComparison != null;
    case DiffdeferdataPackage.DEFERRED_COMPARISON__TRANSIENT_DATA:
      return transientData != null;
    case DiffdeferdataPackage.DEFERRED_COMPARISON__LOGICAL_SOURCE_ROOTS:
      return logicalSourceRoots != null && !logicalSourceRoots.isEmpty();
    case DiffdeferdataPackage.DEFERRED_COMPARISON__TECHNICAL_SOURCE_ROOTS:
      return technicalSourceRoots != null && !technicalSourceRoots.isEmpty();
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString() {
    if (eIsProxy())
      return super.toString();

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (comparisonMethodFactoryID: "); //$NON-NLS-1$
    result.append(comparisonMethodFactoryID);
    result.append(", targetScopeDefinitionURI: "); //$NON-NLS-1$
    result.append(targetScopeDefinitionURI);
    result.append(')');
    return result.toString();
  }

} //DeferredComparisonImpl
