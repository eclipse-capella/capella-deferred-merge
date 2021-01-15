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

import static org.polarsys.capella.diffmerge.defer.ui.views.PendingChangesViewUtil.P_DEFERRED_LOADER;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.diffmerge.ui.diffuidata.ComparisonSelection;
import org.eclipse.emf.diffmerge.ui.viewers.EMFDiffNode;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.sirius.ui.tools.api.project.ModelingProjectManager;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * 
 * @author Ali AKAR
 *
 */
public class OpenSessionCommandHandler extends AbstractHandler {
  
  /**
   * 
   * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
   */
  public Object execute(ExecutionEvent event) throws ExecutionException {
    EMFDiffNode diffNode = getDiffNode(event);
    if(diffNode != null) {
      DeferredComparisonLoader dcLoader = diffNode.getUserPropertyValue(P_DEFERRED_LOADER);
      if(dcLoader !=  null && dcLoader.getResultingSessionURI() != null) {
        URI resultingSessionURI = dcLoader.getResultingSessionURI();
        ModelingProjectManager.INSTANCE
        .loadAndOpenRepresentationsFile(resultingSessionURI, true);
      }
    }
    return null;
  }
  
  /**
   * Return the current diff node in the context of the given event, if any
   * @param event a non-null event
   * @return a potentially null diff node
   */
  protected EMFDiffNode getDiffNode(ExecutionEvent event) {
    ISelection selection = HandlerUtil.getCurrentSelection(event);
    if (selection instanceof ComparisonSelection) {
      return ((ComparisonSelection)selection).getDiffNode();
    }
  
    return null;
  }
}
