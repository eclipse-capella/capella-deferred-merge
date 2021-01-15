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

import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;


/**
 * The activator for this plug-in.
 * @author Olivier Constant
 */
public class EMFDiffMergeDeferUIPlugin extends AbstractUIPlugin {
  
  /** Identifiers for UI images */
  @SuppressWarnings({"javadoc", "nls"})
  public enum ImageID {
    CHANGE_OBJ, CHANGESET_OBJ, BLANK, EXPORT_WIZ, REMOVE;
    /** Whether the image file is a PNG instead of a GIF */
    private boolean isPNG;
    /** Default constructor */
    private ImageID() {
      this(true);
    }
    /** Complete constructor */
    private ImageID(boolean isPNGImage) {
      this.isPNG = isPNGImage;
    }
    /**
     * Return the short name of the corresponding image file
     * @return a non-null, non-empty string
     */
    public String toImageFileName() {
      String ext = isPNG? "png": "gif";
      return name().toLowerCase() + '.' + ext;
    }
  }
  
  /** The local path to icons */
  protected static final String ICON_PATH = "icons/full/"; //$NON-NLS-1$
  
  /** The shared instance */
  private static EMFDiffMergeDeferUIPlugin plugin;
  
  
  /**
   * Constructor
   */
  public EMFDiffMergeDeferUIPlugin() {
    // Nothing needed
  }
  
  /**
   * Return the shared instance
   * @return a non-null instance of this class
   */
  public static EMFDiffMergeDeferUIPlugin getDefault() {
    return plugin;
  }
  
  /**
   * Return the default file extension for deferred comparisons
   * @return a non-null string
   */
  public String getFileExtension() {
    return "pending"; //$NON-NLS-1$
  }
  
  /**
   * Return the image of the given ID
   * @param id a non-null image ID
   * @return a (normally) non-null image
   */
  public Image getImage(ImageID id) {
    return getImageRegistry().get(id.name());
  }
  
  /**
   * Return the image descriptor of the given ID
   * @param id a non-null image ID
   * @return a (normally) non-null image descriptor
   */
  public ImageDescriptor getImageDescriptor(ImageID id) {
    return getImageRegistry().getDescriptor(id.name());
  }
  
  /**
   * Return the ID of this plug-in according to MANIFEST.MF
   * @return a non-null string
   */
  public String getPluginId() {
    return getBundle().getSymbolicName();
  }
  
  /**
   * @see org.eclipse.ui.plugin.AbstractUIPlugin#initializeImageRegistry(org.eclipse.jface.resource.ImageRegistry)
   */
  @Override
  protected void initializeImageRegistry(ImageRegistry reg) {
    super.initializeImageRegistry(reg);
    for (ImageID imageId : ImageID.values()) {
      registerLocalIcon(imageId, reg);
    }
  }
  
  /**
   * Register and return the image descriptor obtained from the given ID of a local icon
   * @param imageID a non-null image ID
   * @param reg the non-null image registry in which to register
   * @return a potentially null image descriptor
   */
  protected ImageDescriptor registerLocalIcon(ImageID imageID, ImageRegistry reg) {
    ImageDescriptor result = null;
    String path = ICON_PATH + imageID.toImageFileName();
    try {
      result = ImageDescriptor.createFromURL(FileLocator.toFileURL(
          getBundle().getEntry(path)));
    } catch (IOException e) {
      // Nothing needed
    }
    if (result != null)
      reg.put(imageID.name(), result);
    return result;
  }
  
  /**
   * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
   */
  @Override
  public void start(BundleContext context) throws Exception {
    super.start(context);
    plugin = this;
  }
  
  /**
   * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
   */
  @Override
  public void stop(BundleContext context) throws Exception {
    plugin = null;
    super.stop(context);
  }
  
}