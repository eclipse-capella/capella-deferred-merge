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
package org.polarsys.capella.diffmerge.defer.ui.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import org.polarsys.capella.diffmerge.defer.ui.views.PendingChangesView;


/**
 * An editor for deferred changes.
 * @author Olivier Constant
 *
 */
public class PendingChangesEditor extends EditorPart {
  
  /**
   * Constructor
   */
  public PendingChangesEditor() {
    // Nothing needed
  }
  
  /**
   * Programmatically close this editor
   */
  protected void autoClose() {
    IEditorSite site = getEditorSite();
    site.getPage().closeEditor((IEditorPart)site.getPart(), false);
  }
  
  /**
   * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
   */
  @Override
  public void createPartControl(Composite parent) {
    PendingChangesView view = PendingChangesView.open();
    if (view != null) {
      IFileEditorInput input = getEditorInput();
      IFile file = input.getFile();
      URI fileURI = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
      view.openURI(fileURI);
    }
  }
  
  /**
   * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
   */
  @Override
  public void doSave(IProgressMonitor monitor) {
    // Not available
  }
  
  /**
   * @see org.eclipse.ui.part.EditorPart#doSaveAs()
   */
  @Override
  public void doSaveAs() {
    // Not available
  }
  
  /**
   * @see org.eclipse.ui.part.EditorPart#getEditorInput()
   */
  @Override
  public IFileEditorInput getEditorInput() {
    return (IFileEditorInput)super.getEditorInput();
  }
  
  /**
   * @see org.eclipse.ui.part.EditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
   */
  @Override
  public void init(IEditorSite site, IEditorInput input)
      throws PartInitException {
    if (!(input instanceof IFileEditorInput))
      throw new PartInitException("Invalid Input: Must be IFileEditorInput"); //$NON-NLS-1$
    setSite(site);
    setInput(input);
  }
  
  /**
   * @see org.eclipse.ui.part.EditorPart#isDirty()
   */
  @Override
  public boolean isDirty() {
    return false;
  }
  
  /**
   * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
   */
  @Override
  public boolean isSaveAsAllowed() {
    return false;
  }
  
  /**
   * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
   */
  @Override
  public void setFocus() {
    autoClose();
  }
  
}
