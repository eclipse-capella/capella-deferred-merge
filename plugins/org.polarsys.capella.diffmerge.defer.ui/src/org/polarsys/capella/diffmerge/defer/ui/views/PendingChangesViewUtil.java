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
package org.polarsys.capella.diffmerge.defer.ui.views;

import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.diffmerge.ui.util.UserProperty.Identifier;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.sirius.business.api.session.Session;

import org.polarsys.capella.diffmerge.defer.ui.DeferredComparisonLoader;

/**
 * Utility class
 * 
 * @author Ali AKAR
 *
 */
public class PendingChangesViewUtil {

  /**
   * Private constructor to hide the implicit public one.
   */
  private PendingChangesViewUtil() {
    // To hide the implicit public one.
  }

  /**
   * A property to get the loader from the diff node.
   */
  public static Identifier<DeferredComparisonLoader> P_DEFERRED_LOADER = new Identifier<DeferredComparisonLoader>(
      "PROPERTY_DEFERRED_LOADER"); //$NON-NLS-1$
  /**
   * 
   * @return The DeferredComparisonLoader(s) with null session from the given
   *         loaders.
   */
  public static Collection<DeferredComparisonLoader> getDCLoadersWithNullSession(
      Collection<DeferredComparisonLoader> dcLoaders) {
    Collection<DeferredComparisonLoader> result = new HashSet<>();
    for (DeferredComparisonLoader loader : dcLoaders) {
      if (loader.getResultingSession() == null) {
        result.add(loader);
      }
    }
    return result;
  }

  /**
   * 
   * @param dcLoaders
   *          the DeferredComparisonLoader to look over
   * @param session
   *          the session
   * @return the DeferredComparisonLoader related to the given session.
   */
  public static Collection<DeferredComparisonLoader> getDCLoadersRelatedToSession(
      Collection<DeferredComparisonLoader> dcLoaders, Session session) {
    Collection<DeferredComparisonLoader> result = new HashSet<>();
    Resource sessionResource = session.getSessionResource();
    if (sessionResource != null && sessionResource.getURI() != null) {
      URI uri = sessionResource.getURI();
      for (DeferredComparisonLoader loader : dcLoaders) {
        if (uri.equals(loader.getResultingSessionURI())) {
          result.add(loader);
        }
      }
    }
    return result;
  }

  /**
   * 
   * @param dcLoaders
   *          the DeferredComparisonLoader to look over
   * @param uri
   *          the URI
   * @return the DeferredComparisonLoader for to the given URI.
   */
  public static Collection<DeferredComparisonLoader> getDCLoadersForURI(
      Collection<DeferredComparisonLoader> dcLoaders, URI uri) {
    Collection<DeferredComparisonLoader> result = new HashSet<>();
    for (DeferredComparisonLoader loader : dcLoaders) {
      if (uri.equals(loader.getURI())) {
        result.add(loader);
      }
    }
    return result;
  }
  
  /**
   * Return a name of the given deferred comparison. May be called from a non-UI
   * thread.
   * 
   * @param loader
   *          a non-null object
   * @return a potentially null string
   */
  public static String getName(DeferredComparisonLoader loader) {
    return getRelativeURI(loader.getURI());
  }
  
  /**
   * 
   * @param uri
   *          a non-null URI
   * @return a potentially null string. see
   *         {@link URI#toPlatformString(boolean)}
   */
  public static String getRelativeURI(URI uri) {
    String result = null;
    result = uri.toPlatformString(true);
    if (result != null && result.startsWith("/")) { //$NON-NLS-1$
      result = result.substring(1);
    }
    return result;
  }
  
  /**
   * Returns a new name for the resource at the given path in the
   * given workspace. This name is determined automatically.
   *
   * @param originalName
   *            the full path of the resource
   * @param workspace
   *            the workspace
   * @return the new full path for the resource
   */
  public static IPath getAutoNewNameFor(IPath originalName, IWorkspace workspace) {
    String resourceName = originalName.lastSegment();
    IPath leadupSegment = originalName.removeLastSegments(1);
    boolean isFile = !originalName.hasTrailingSeparator();

    String newName = resourceName;
    while (true) {
      IPath pathToTry = leadupSegment.append(newName);
      if (!workspace.getRoot().exists(pathToTry)) {
        return pathToTry;
      }
      newName = computeNewName(newName, isFile);
    }
  }

  /**
   * 
   * @param baseName the base name
   * @param isFile a boolean to indicate whether it's a file
   * @return a new name from the given base name
   */
  private static String computeNewName(String baseName, boolean isFile) {
    int lastIndexOfDot = baseName.lastIndexOf('.');
    String fileExtension = ""; //$NON-NLS-1$
    String fileNameNoExtension = baseName;
    if (isFile && lastIndexOfDot > 0) {
      fileExtension = baseName.substring(lastIndexOfDot);
      fileNameNoExtension = baseName.substring(0, lastIndexOfDot);
    }
    Pattern p = Pattern.compile("[0-9]+$"); //$NON-NLS-1$
    Matcher m = p.matcher(fileNameNoExtension);
    if (m.find()) {
      // String ends with a number: increment it by 1
      int newNumber = Integer.parseInt(m.group()) + 1;
      String numberStr = m.replaceFirst(Integer.toString(newNumber));
      return numberStr + fileExtension;
    }
    return fileNameNoExtension + "2" + fileExtension; //$NON-NLS-1$
  }

  /**
   * Retrieves the {@linkplain IFile file} corresponding to the given {@linkplain org.eclipse.emf.common.util.URI}.
   *
   * @param uri
   *            The {@linkplain URI} of the file to return.
   * @return The file corresponding to the specified {@link URI uri}.
   */
  public static IFile getFile(URI uri) {
    if (uri != null && Platform.isRunning()) {
      // Create dummy resource transporting given URI
      Resource resource = new ResourceImpl(uri);

      // Create dummy resource set transporting appropriate URI converter
      ResourceSet resourceSet = new ResourceSetImpl();
      resourceSet.getResources().add(resource);
      resourceSet.setURIConverter(getURIConverter(null));

      // Delegate to getFile(Resource) method
      return getFile(resource);
    }
    return null;
  }
  
  /**
   * Retrieves the {@link IFile file} corresponding to the given <code>resource</code>.
   *
   * @param resource
   *            The {@link Resource resource} for which the file is to be returned.
   * @return The file corresponding to the specified <code>resource</code>.
   */
  public static IFile getFile(final Resource resource) {
    if (resource != null && Platform.isRunning()) {
      return WorkspaceSynchronizer.getFile(resource);
    }
    return null;
  }
  
  /**
   * Returns the {@link URIConverter URI converter} of given {@link ResourceSet resource set}. If no
   * {@link ResourceSet resource set} is provided an instance of {@link ExtensibleURIConverterImpl} is returned
   * instead. In both cases, the {@link URIConverter URI converter}'s URI mappings are initialized in such a way that
   * normalization of non-platform:/resource {@link URI}s which reference resources inside the workspace yields the
   * corresponding platform:/resource {@link URI}s.
   *
   * @param resourceSet
   *            The {@link ResourceSet resource set} whose {@link URIConverter URI converter} is to be retrieved.
   * @return The {@link URIConverter URI converter} of given {@link ResourceSet resource set}, or an instance of
   *         {@link ExtensibleURIConverterImpl} if no such is provided, containing URI mappings for normalizing
   *         non-platform:/resource {@link URI}s referencing workspace resources to corresponding platform:/resource
   *         {@link URI}s.
   */
  public static URIConverter getURIConverter(ResourceSet resourceSet) {
    // Retrieve or create URI converter
    URIConverter uriConverter;
    if (resourceSet != null) {
      uriConverter = resourceSet.getURIConverter();
    } else {
      uriConverter = new ExtensibleURIConverterImpl();
    }
    if (Platform.isRunning()) {
      // Initialize URI mappings
      IPath workspaceRootPath = ResourcesPlugin.getWorkspace().getRoot().getFullPath().addTrailingSeparator();
      URI workspaceRootURI = URI.createPlatformResourceURI(workspaceRootPath.toString(), true);

      IPath workspaceRootLocation = ResourcesPlugin.getWorkspace().getRoot().getLocation().addTrailingSeparator();
      URI workspaceRootLocationURI = URI.createURI(workspaceRootLocation.toString(), true);
      URI workspaceRootLocationFileURI = URI.createFileURI(workspaceRootLocation.toString());

      uriConverter.getURIMap().put(workspaceRootLocationURI, workspaceRootURI);
      uriConverter.getURIMap().put(workspaceRootLocationFileURI, workspaceRootURI);
    }
    return uriConverter;
  }
}
