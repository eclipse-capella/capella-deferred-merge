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

import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManagerListener;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * @author Ali AKAR
 *
 */
public class DeferSessionManagerListener extends SessionManagerListener.Stub {

  /**
   * 
   * @see org.eclipse.sirius.business.api.session.SessionManagerListener.Stub#notifyRemoveSession(org.eclipse.sirius.business.api.session.Session)
   */
  @Override
  public void notifyRemoveSession(Session removedSession) {
    IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench()
        .getActiveWorkbenchWindow();
    if (activeWorkbenchWindow != null) {
      IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
      if (activePage != null) {
        IEditorReference[] editorReferences = activePage.getEditorReferences();
        for (IEditorReference editorRef : editorReferences) {
          if ("org.eclipse.compare.CompareEditor".equals(editorRef.getId())) { //$NON-NLS-1$
            IEditorPart editor = editorRef.getEditor(false);
            if (editor != null) {
              activePage.closeEditor(editor, true);
            }
          }
        }
      }
    }
  }
}
