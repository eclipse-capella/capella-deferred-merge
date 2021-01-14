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
package org.polarsys.capella.diffmerge.defer.tests.ju.testcases;

import java.util.Arrays;
import java.util.List;

import org.polarsys.capella.test.framework.api.BasicTestCase;

public class DiffMergeDeferTestCase extends BasicTestCase {
  
  private static final String MODEL_PATH = "TestModel";

  @Override
  public List<String> getRequiredTestModels() {
    return Arrays.asList(MODEL_PATH);
  }

  @Override
  public void test() {
    // TODO
  }
}
