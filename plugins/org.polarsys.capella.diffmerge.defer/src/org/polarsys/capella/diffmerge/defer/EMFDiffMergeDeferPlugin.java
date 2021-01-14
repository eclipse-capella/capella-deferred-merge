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
package org.polarsys.capella.diffmerge.defer;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;


/**
 * The activator class for this plug-in.
 * @author Olivier Constant
 */
public class EMFDiffMergeDeferPlugin extends Plugin {
  
	/** The shared instance */
	private static EMFDiffMergeDeferPlugin plugin;
	
	
	/**
	 * Constructor
	 */
	public EMFDiffMergeDeferPlugin() {
	  // Nothing needed
	}
	
  /**
   * Return the shared instance of the activator
   * @return a non-null instance
   */
  public static EMFDiffMergeDeferPlugin getDefault() {
    return plugin;
  }
  
  /**
   * Return the ID of this plug-in according to MANIFEST.MF
   * @return a non-null string
   */
  public String getPluginId() {
    return getBundle().getSymbolicName();
  }
  
  /**
   * Log a message for this plug-in
   * @param severity the severity according to IStatus
   * @param message a potentially null string
   */
  public void log(int severity, String message) {
    getLog().log(new Status(severity, getPluginId(), message));
  }
  
  /**
   * @see org.eclipse.core.runtime.Plugin#start(org.osgi.framework.BundleContext)
   */
  @Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}
  
	/**
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}
}
