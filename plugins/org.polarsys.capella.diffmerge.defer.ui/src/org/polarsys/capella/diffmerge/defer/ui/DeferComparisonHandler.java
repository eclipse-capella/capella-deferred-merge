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

import java.util.Collections;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.ui.dialogs.WorkspaceResourceDialog;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.diffmerge.api.scopes.IPersistentModelScope;
import org.eclipse.emf.diffmerge.generic.api.Role;
import org.eclipse.emf.diffmerge.generic.api.scopes.IEditableTreeDataScope;
import org.eclipse.emf.diffmerge.ui.diffuidata.ComparisonSelection;
import org.eclipse.emf.diffmerge.ui.setup.EMFDiffMergeEditorInput;
import org.eclipse.emf.diffmerge.ui.viewers.EMFDiffNode;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.handlers.HandlerUtil;

import org.polarsys.capella.diffmerge.defer.ui.messages.Messages;
import org.polarsys.capella.diffmerge.defer.ui.views.PendingChangesViewUtil;


/**
 * A command handler for deferring changes.
 * @author Olivier Constant
 */
public class DeferComparisonHandler extends AbstractHandler {
  
  /** A label for the command */
  public static final String DEFER_MERGE_LABEL = Messages.DeferChangesHandler_Header;
  
  /** The role of the target side of the merge */
  protected static final Role TARGET_ROLE = Role.TARGET;
  
  
  /**
   * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
   */
  public Object execute(ExecutionEvent event) throws ExecutionException {
    Shell shell = HandlerUtil.getActiveShell(event);
    if (shell != null) {
      EMFDiffNode node = getDiffNode(event);
      if (node != null) {
        execute(node, shell);
      }
    }
    return null;
  }
  
  /**
   * Execute the command on the given diff node with the given shell
   * @param node a non-null diff node
   * @param shell a non-null shell
   */
  protected void execute(final EMFDiffNode node, final Shell shell) {
    final URI deferFileURI = getDeferFileURI(node, shell);
    if (deferFileURI != null) {
      Job deferJob = new DeferComparisonJob(node, deferFileURI);
      deferJob.schedule();
    }
  }
  
  /**
   * Return a platform resource URI for storing the changes
   * @param node the non-null node for the changes
   * @param shell a non-null shell
   * @return a potentially null URI, where null stands for cancel
   */
  public URI getDeferFileURI(EMFDiffNode node, Shell shell) {
    URI result = null;
    URI targetScopeURI = null;
    IEditableTreeDataScope<?> targetScope =
        node.getUIComparison().getActualComparison().getScope(TARGET_ROLE);
    if (targetScope instanceof IPersistentModelScope) {
      Resource mainResource = ((IPersistentModelScope)targetScope).getHoldingResource();
      if (mainResource != null) {
        targetScopeURI = mainResource.getURI();
      }
    }
    String fileExt = EMFDiffMergeDeferUIPlugin.getDefault().getFileExtension();
    final URI proposedURI = (targetScopeURI == null
        || !targetScopeURI.isPlatformResource()) ? null
            : targetScopeURI.trimFileExtension().appendFileExtension(fileExt);
    IPath proposedPath = (proposedURI == null) ? null
        : PendingChangesViewUtil.getAutoNewNameFor(
            new Path(proposedURI.toPlatformString(true)),
            ResourcesPlugin.getWorkspace());
    boolean confirmed;
    IFile changeFile;
    do {
      confirmed = true;
      changeFile = WorkspaceResourceDialog.openNewFile(shell,
          DEFER_MERGE_LABEL,
          Messages.DeferChangesHandler_DefineFileDescription,
          proposedPath, Collections.<ViewerFilter>emptyList());
      if (changeFile != null && changeFile.exists()) {
        // Overwrite?
        confirmed = MessageDialog.openQuestion(shell, DEFER_MERGE_LABEL,
            Messages.DeferComparisonHandler_FileOverwrite);
        if (!confirmed) {
          proposedPath = changeFile.getFullPath();
        }
      }
    } while (!confirmed);
    if (changeFile != null) {
      result = URI.createPlatformResourceURI(
          changeFile.getFullPath().toString(), true);
    }
    return result;
  }
  
  /**
   * Return the current diff node in the context of the given event, if any
   * @param event a non-null event
   * @return a potentially null diff node
   */
  protected EMFDiffNode getDiffNode(ExecutionEvent event) {
    EMFDiffNode result = null;
    ISelection selection = HandlerUtil.getCurrentSelection(event);
    if (selection instanceof ComparisonSelection) {
      result = ((ComparisonSelection)selection).getDiffNode();
    }
    if (result == null) {
      IEditorInput input = HandlerUtil.getActiveEditorInput(event);
      if (input instanceof EMFDiffMergeEditorInput)
        result = ((EMFDiffMergeEditorInput)input).getCompareResult();
    }
    return result;
  }
  
}
