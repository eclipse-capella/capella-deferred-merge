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
package org.polarsys.capella.diffmerge.defer.ui.messages;

import org.eclipse.osgi.util.NLS;


/**
 * String management.
 * @author Olivier Constant
 */
@SuppressWarnings("javadoc")
public class Messages extends NLS {
  private static final String BUNDLE_NAME = "org.polarsys.capella.diffmerge.defer.ui.messages.messages"; //$NON-NLS-1$
  static {
    // initialize resource bundle
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages() {
    // Nothing needed
  }

  public static String DeferChangesHandler_DefineFileDescription;
  public static String DeferChangesHandler_Header;
  public static String DeferComparisonHandler_FileOverwrite;
  public static String DeferredComparisonLoader_CommandDisposalName;
  public static String LoadComparisonRunnable_CommandName;
  public static String PendingChangesView_Close_Tooltip;
  public static String PendingChangesView_ConfirmReplace;
  public static String PendingChangesView_ReadOnly_Suffix;
  public static String PendingChangesView_SelectCurrent_Tooltip;
  public static String PendingChangesView_SyncEditor_ToolTip;
  public static String PendingChangesView_NoContentToDisplay;
  public static String PendingChangesView_ContentDescription;
  public static String PendingChangesView_PendingFileModified;
  public static String PendingChangesView_Error;
  public static String PendingChangesView_ErrorMsg;
  public static String DeferredComparisonLoader_OpenSessionTitle;
  public static String DeferredComparisonLoader_OpenSessionQuestion;
  public static String DeferredComparisonLoader_Error;
  public static String DeferredComparisonLoader_ErrorMsg;
}
