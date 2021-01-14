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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.diffmerge.diffdata.EMatch;
import org.eclipse.emf.diffmerge.diffdata.EReferenceValuePresence;
import org.eclipse.emf.diffmerge.generic.api.diff.IReferenceValuePresence;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.BasicResourceHandler;

/**
 * @author Ali AKAR
 *
 */
public class DiffdeferdataResourceHandler extends BasicResourceHandler {

  

  /**
   * @see org.eclipse.emf.ecore.xmi.impl.BasicResourceHandler#postLoad(org.eclipse.emf.ecore.xmi.XMLResource, java.io.InputStream, java.util.Map)
   */
  @Override
  public void postLoad(XMLResource resource, InputStream inputStream,
      Map<?, ?> options) {
    Collection<EObject> objectsToRemove = new ArrayList<>();
    Collection<EReferenceValuePresence> allRefValuePresences = new ArrayList<>();
    TreeIterator<EObject> allContents = resource.getAllContents();
    while(allContents.hasNext()) {
      EObject next = allContents.next();
      if(next instanceof EReferenceValuePresence) {
        EReferenceValuePresence eRefValuePresence = (EReferenceValuePresence)next;
        if(eRefValuePresence.getValue() == null || eRefValuePresence.getValueMatch() == null) {
          objectsToRemove.add(eRefValuePresence);
        }else {
          allRefValuePresences.add(eRefValuePresence);
        }
      }
    }
    if(!objectsToRemove.isEmpty()) {
      EcoreUtil.deleteAll(objectsToRemove, true);
      cleanReferenceMap(allRefValuePresences);
    }
  }

  /**
   * This is called to prevent NPE in org.eclipse.emf.diffmerge.diffdata.impl.EReferenceValuePresenceImpl.getSymmetrical()
   * @param allRefValuePresences the collection of all EReferenceValuePresence to clean their reference map.
   */
  private void cleanReferenceMap(
      Collection<EReferenceValuePresence> allRefValuePresences) {
    for (EReferenceValuePresence refValuePresence : allRefValuePresences) {
      EMatch elementMatch = refValuePresence.getElementMatch();
      EMap<EObject, IReferenceValuePresence<EObject>> forReference = elementMatch
          .getModifiableReferenceMap().get(refValuePresence.getFeature());
      if (forReference != null && forReference.values().contains(null)) {
        forReference.values().remove(null);        
      }
    }
  }
}
