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
package org.polarsys.capella.diffmerge.defer.ui.resources;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

/**
 * 
 * @author Ali AKAR
 *
 */
public class ResourceDeltaVisitor implements IResourceDeltaVisitor {

  protected int eventType;

  protected IResourceChangeHandler resourceChangeHandler;
  
  /**
   * A resource delta visitor to analyze change and delegate to handler
   * @param eventType the event type
   * @param resourceChangeHandler the resource change handler
   */
  public ResourceDeltaVisitor(int eventType, IResourceChangeHandler resourceChangeHandler) {
    this.eventType = eventType;
    this.resourceChangeHandler = resourceChangeHandler;
  }
  
  /**
   * Visits the given resource delta.
   */
  public boolean visit(IResourceDelta delta) throws CoreException {
    IResource resource = delta.getResource();
    
    switch (delta.getKind()) {
    case IResourceDelta.CHANGED:
      return true;
    case IResourceDelta.REMOVED:
      if(resource instanceof IFile) {
        resourceChangeHandler.handleFileRemoved(eventType, (IFile)resource);
      }else if(resource instanceof IContainer) {
        return true;
      }
      break;
    default:
      break;
    }
    return false;
  }
}
