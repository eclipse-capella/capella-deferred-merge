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

import org.eclipse.emf.diffmerge.diffdata.DiffdataPackage;
import org.eclipse.emf.diffmerge.diffdata.EAttributeValuePresence;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIHelperImpl;

/**
 * A XMIHelper to process cases when deferred comparisons containing references
 * to pure Java Objects are serialized and deserialized.
 * 
 * ValuePresence values are serialized first.</br>
 * ValuePresence attributes are serialized second.
 * 
 * 
 * @author Minh-Tu Ton That
 * @author Olivier Constant
 */
public class DiffdeferdataXMIHelper extends XMIHelperImpl {

  /**
   * The last encountered Value Presence Attribute.
   */
  protected EAttribute lastValuePresenceAttribute;

  /**
   * The last encountered Value Presence Value.
   */
  protected Object lastValuePresenceValue;

  /**
   * Constructor
   * 
   * @param resource
   *          the non-null local resource
   */
  public DiffdeferdataXMIHelper(XMLResource resource) {
    super(resource);
    lastValuePresenceAttribute = null;
    lastValuePresenceValue = null;
  }

  /**
   * Returns the value that will be serialized.
   * 
   * @see org.eclipse.emf.ecore.xmi.impl.XMLHelperImpl#getValue(org.eclipse.emf.ecore.EObject,
   *      org.eclipse.emf.ecore.EStructuralFeature)
   */
  @Override
  public Object getValue(EObject object, EStructuralFeature feature) {

    // we know that the value is serialized before the feature
    // we save the feature to use it in the convertToString method for the value
    if (object instanceof EAttributeValuePresence
        && feature == DiffdataPackage.Literals.EATTRIBUTE_VALUE_PRESENCE__VALUE) {

      EAttributeValuePresence valuePresence = (EAttributeValuePresence) object;
      lastValuePresenceAttribute = valuePresence.getAttribute();
    }

    return super.getValue(object, feature);
  }

  /**
   * @see org.eclipse.emf.ecore.xmi.impl.XMLHelperImpl#convertToString(org.eclipse.emf.ecore.EFactory,
   *      org.eclipse.emf.ecore.EDataType, java.lang.Object)
   */
  @Override
  public String convertToString(EFactory eFactory, EDataType eDataType,
      Object value) {

    if (isJavaObjectSerialization(eFactory, eDataType)) {
      EDataType emfDataType = extractEMFDataType(lastValuePresenceAttribute);
      EFactory emfFactory = extractEMFFactory(emfDataType);

      return super.convertToString(emfFactory, emfDataType, value);
    }

    return super.convertToString(eFactory, eDataType, value);
  }

  /**
   * Returns the value that will be serialized.
   * 
   * @see org.eclipse.emf.ecore.xmi.impl.XMLHelperImpl#setValue(org.eclipse.emf.ecore.EObject,
   *      org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object, int)
   */
  @Override
  public void setValue(EObject object, EStructuralFeature feature, Object value,
      int position) {
    if (object instanceof EAttributeValuePresence) {
      EAttributeValuePresence valuePresence = (EAttributeValuePresence) object;

      // we know that the value is serialized before the feature
      // we save the value and will deserialize once we have the feature
      if (feature == DiffdataPackage.Literals.EATTRIBUTE_VALUE_PRESENCE__VALUE) {
        lastValuePresenceValue = value;
        return;

      } else if (feature == DiffdataPackage.Literals.EATTRIBUTE_VALUE_PRESENCE__ATTRIBUTE) {

        // set the feature first, this resolves the proxies
        super.setValue(object, feature, value, position);

        // save the feature
        lastValuePresenceAttribute = valuePresence.getAttribute();

        EDataType emfDataType = extractEMFDataType(lastValuePresenceAttribute);
        EFactory emfFactory = extractEMFFactory(emfDataType);

        // save the value that we delayed
        Object valuePresenceValue = createFromString(emfFactory, emfDataType,
            lastValuePresenceValue.toString());

        valuePresence.setValue(valuePresenceValue);
        return;
      }
    }

    super.setValue(object, feature, value, position);
  }

  /**
   * @see org.eclipse.emf.ecore.xmi.impl.XMLHelperImpl#createFromString(org.eclipse.emf.ecore.EFactory,
   *      org.eclipse.emf.ecore.EDataType, java.lang.String)
   */
  @Override
  protected Object createFromString(EFactory eFactory, EDataType eDataType,
      String value) {

    if (isJavaObjectSerialization(eFactory, eDataType)) {
      EDataType emfDataType = extractEMFDataType(lastValuePresenceAttribute);
      EFactory emfFactory = extractEMFFactory(emfDataType);

      return super.createFromString(emfFactory, emfDataType, value);
    }
    return super.createFromString(eFactory, eDataType, value);
  }

  /**
   * Extracts the EMF data type from the current attribute.
   * 
   * @param attribute
   *          the attribute.
   * @return the EMF data type of the current attribute.
   */
  private EDataType extractEMFDataType(EAttribute attribute) {
    return attribute.getEAttributeType();
  }

  /**
   * Extracts the EMF factory from the current data type.
   * 
   * @param dataType
   *          the data type.
   * @return the EMF factory of the current data type.
   */
  private EFactory extractEMFFactory(EDataType dataType) {
    return dataType.getEPackage().getEFactoryInstance();
  }

  /**
   * Returns true if we are currently processing a Java Object in a
   * serialization context.
   * 
   * @param eFactory
   *          the EMF factory that will be used.
   * @param eDataType
   *          the EMF data type of the object.
   * @return true if we are currently processing a Java Object in a
   *         serialization context, false otherwise.
   */
  private boolean isJavaObjectSerialization(EFactory eFactory,
      EDataType eDataType) {
    return lastValuePresenceAttribute != null
        && eDataType == EcorePackage.eINSTANCE.getEJavaObject()
        && eFactory instanceof EFactoryImpl;
  }
}