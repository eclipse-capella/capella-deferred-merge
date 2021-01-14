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

import java.util.HashMap;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.diffmerge.diffdata.DiffdataPackage;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLLoad;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLInfoImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLMapImpl;

/**
 * <!-- begin-user-doc -->
 * The <b>Resource </b> associated with the package.
 * <!-- end-user-doc -->
 * @see org.polarsys.capella.diffmerge.defer.diffdeferdata.util.DiffdeferdataResourceFactoryImpl
 * @generated
 */
public class DiffdeferdataResourceImpl extends XMIResourceImpl {
  /**
   * Creates an instance of the resource.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param uri the URI of the new resource.
   * @generated NOT
   */
  public DiffdeferdataResourceImpl(URI uri) {
    super(uri);
  }

  /**
   * @see org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl#attachedHelper(org.eclipse.emf.ecore.EObject)
   * @generated NOT
   */
  @Override
  protected void attachedHelper(EObject eObject) {
    // Enforce presence of Ecore IDs
    String id = EcoreUtil.getID(eObject);
    if (id == null && eObject.eClass().getEIDAttribute() != null) {
      id = EcoreUtil.generateUUID();
      EcoreUtil.setID(eObject, id);
    }
    super.attachedHelper(eObject);
  }

  /**
   * @see org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl#init()
   * @generated NOT
   */
  @Override
  protected void init() {
    super.init();
    // Misc
    setEncoding("UTF-8"); //$NON-NLS-1$
    setIntrinsicIDToEObjectMap(new HashMap<String, EObject>());
    // Load options: defer ID resolution
    getDefaultLoadOptions().put(OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);
    // Load options: Resource handler
    getDefaultLoadOptions().put(OPTION_RESOURCE_HANDLER,
        new DiffdeferdataResourceHandler());
    // Save options: discard errors and serialize value of attribute value presence as element
    getDefaultSaveOptions().put(OPTION_PROCESS_DANGLING_HREF,
        OPTION_PROCESS_DANGLING_HREF_DISCARD);
    XMLMapImpl xmlMap = new XMLMapImpl();
    XMLInfoImpl xmlInfo = new XMLInfoImpl();
    xmlInfo.setXMLRepresentation(XMLInfo.ELEMENT);
    xmlMap.add(DiffdataPackage.Literals.EATTRIBUTE_VALUE_PRESENCE__VALUE,
        xmlInfo);
    getDefaultSaveOptions().put(XMLResource.OPTION_XML_MAP, xmlMap);
  }

  /**
   * @see org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl#createXMLHelper()
   * @generated NOT
   */
  @Override
  public XMLHelper createXMLHelper() {
    return new DiffdeferdataXMIHelper(this);
  }

  /**
   * @see org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl#createXMLLoad()
   * @generated NOT
   */
  @Override
  protected XMLLoad createXMLLoad() {
    return new DiffdeferdataXMILoadImpl(createXMLHelper());
  }

} //DiffdeferdataResourceImpl
