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
package org.polarsys.capella.diffmerge.defer.helpers;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.diffmerge.diffdata.EComparison;
import org.eclipse.emf.diffmerge.diffdata.EMapping;
import org.eclipse.emf.diffmerge.diffdata.EMatch;
import org.eclipse.emf.diffmerge.diffdata.EReferenceValuePresence;
import org.eclipse.emf.diffmerge.generic.api.Role;
import org.eclipse.emf.diffmerge.generic.api.diff.IDifference;
import org.eclipse.emf.diffmerge.generic.api.scopes.IEditableTreeDataScope;
import org.eclipse.emf.diffmerge.generic.gdiffdata.GdiffdataPackage;
import org.eclipse.emf.diffmerge.structures.common.FHashSet;
import org.eclipse.emf.diffmerge.structures.common.FLinkedList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.sirius.viewpoint.DRepresentationDescriptor;


/**
 * A Copier that copies a comparison into a trimmed comparison.
 * @author Olivier Constant
 */
public class TrimmingCopier extends EcoreUtil.Copier {
  
  /** Serialization tag */
  private static final long serialVersionUID = 1L;
  
  /** The containment feature for matches */
  protected static final EReference EMATCH_CONTAINMENT =
      GdiffdataPackage.eINSTANCE.getGMapping_ModifiableContents();      
  
  /** The role of the target side */
  protected static final Role TARGET = Role.TARGET;
  
  /** The non-null comparison to copy */
  protected final EComparison originalComparison;
  
  /** The non-null, potentially empty list of copies of matches.
   *  It must be a list because that is implicitly required by EcoreEList::set. */
  protected final List<EMatch> copyMatches;
  
  /** The non-null, potentially empty set of original source elements
   *  copied from the non-target side */
  protected final Set<EObject> originalSourceElements;
  
  /** The non-null, potentially empty set of DRepresentationDescriptors to copy */
  protected final Set<DRepresentationDescriptor> originalRepresentationDescriptors;
  
  
  /**
   * Default constructor
   * @param comparison the non-null comparison to copy
   */
  public TrimmingCopier(EComparison comparison) {
    super(true, true);
    this.originalComparison = comparison;
    this.copyMatches = new FLinkedList<>();
    this.originalSourceElements = new FHashSet<>();
    this.originalRepresentationDescriptors = new FHashSet<>();
  }
  
  /**
   * Copy the source comparison and return the trimmed copy
   * @return a non-null comparison
   */
  public EComparison copy() {
    EComparison result = (EComparison)copy(originalComparison);
    copyComparedContainments();
    copyReferences();
    return result;
  }
  
  /**
   * @see org.eclipse.emf.ecore.util.EcoreUtil.Copier#copy(org.eclipse.emf.ecore.EObject)
   */
  @Override
  public EObject copy(EObject originalElement) {
    return copy(originalElement, true);
  }
  
  /**
   * Works similarly to copy(EObject) except that parameter whether the contents
   * of the element must be copied too is specified
   * @param originalElement the potentially null element to copy
   * @param copyContents whether its contents (sub-elements) must be copied
   * @return a potentially null element
   */
  protected EObject copy(EObject originalElement, boolean copyContents) {
    EObject result = null;
    if (originalElement != null) {
      result = createCopy(originalElement);
      if (result != null) {
        boolean isEMatch = result instanceof EMatch;
        if (isEMatch) {
          // Special case of matches for equals method and dependent elements
          initMatch((EMatch)originalElement, (EMatch)result);
        }
        // Registration
        put(originalElement, result);
        // Attributes and Containments
        copyFeatures(originalElement, result, copyContents);
        // Proxy URI
        copyProxyURI(originalElement, result);
        // Match hierarchy
        if (isEMatch) {
          copyMatchHierarchy((EMatch)originalElement);
        }
      }
    }
    return result;
  }
  
  /**
   * Copy containments between the copied compared elements within the
   * given comparison copy
   */
  protected void copyComparedContainments() {
    for (EObject originalElement : originalSourceElements) {
      try {
      copyComparedContainment(originalElement);
      } catch (Exception e) {
        e.printStackTrace();
        // Non-critical: proceed
      }
    }
  }
  
  /**
   * Copy the containment ownership of the given original compared element
   * in its copy
   */
  @SuppressWarnings("unchecked")
  protected void copyComparedContainment(EObject originalElement) {
    EReference containment = originalElement.eContainmentFeature();
    EObject originalContainer = originalElement.eContainer();
    if (originalContainer != null && containment != null) {
      EObject copyContainer = get(originalContainer);
      if (copyContainer != null) {
        EObject copyElement = get(originalElement); // Non-null by construction
        Setting copyContainerSetting =
            ((InternalEObject)copyContainer).eSetting(containment);
        if (containment.isMany()) {
          Object value = copyContainerSetting.get(true);
          ((List<EObject>)value).add(copyElement); // Assumed changeable
        } else {
          copyContainerSetting.set(copyElement);
        }
      }
    }
  }
  
  /**
   * @see org.eclipse.emf.ecore.util.EcoreUtil.Copier#copyContainment(org.eclipse.emf.ecore.EReference, org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject)
   */
  @Override
  protected void copyContainment(EReference containment, EObject originalElement,
      EObject copyElement) {
    if (originalElement == originalComparison.getMapping() &&
        containment == EMATCH_CONTAINMENT) {
      copyMatchContainment((EMapping)originalElement, (EMapping)copyElement);
    } else {
      super.copyContainment(containment, originalElement, copyElement);
    }
  }
  
  /**
   * Copy the features of the given original element into the given
   * copy, skipping cross-references
   * @param originalElement a non-null element
   * @param copyElement the non-null copy
   * @param copyContents whether the contained elements must be copied too
   */
  protected void copyFeatures(EObject originalElement, EObject copyElement,
      boolean copyContents) {
    EClass eClass = originalElement.eClass();
    for (int i = 0, size = eClass.getFeatureCount(); i < size; ++i) {
      EStructuralFeature eStructuralFeature = eClass.getEStructuralFeature(i);
      if (eStructuralFeature.isChangeable() && !eStructuralFeature.isDerived() ||
          eStructuralFeature == EMATCH_CONTAINMENT) { // Due to Bug 541547
        if (eStructuralFeature instanceof EAttribute) {
          copyAttribute((EAttribute)eStructuralFeature, originalElement, copyElement);
        } else {
          EReference eReference = (EReference)eStructuralFeature;
          if (eReference.isContainment() && copyContents) {
            copyContainment(eReference, originalElement, copyElement);
          }
        }
      }
    }
  }
  
  /**
   * Copy the containment setting of matches between the given source
   * and target mappings
   * @param originalMapping the non-null source mapping
   * @param copyMapping the non-null target mapping
   */
  protected void copyMatchContainment(EMapping originalMapping,
      EMapping copyMapping) {
    if (originalMapping.eIsSet(EMATCH_CONTAINMENT)) {
      EStructuralFeature.Setting setting =
          ((InternalEObject)copyMapping).eSetting(EMATCH_CONTAINMENT);
      if (setting != null) {
        // Get matches
        Object rawValue = originalMapping.eGet(EMATCH_CONTAINMENT);
        @SuppressWarnings("unchecked")
        List<EObject> sourceValues = (List<EObject>)rawValue;
        // Select and copy the eligible ones
        for (EObject sourceValue : sourceValues) {
          // For a given match, copy the corresponding selected ones
          Collection<EMatch> selectedOriginalMatches =
              getSelectedOriginalMatches((EMatch)sourceValue);
          for (EMatch selectedOriginalMatch : selectedOriginalMatches) {
            if (get(selectedOriginalMatch) == null) {
              copy(selectedOriginalMatch);
            }
          }
        }
        setting.set(copyMatches);
      }
    }
  }
  
  /**
   * Copy the hierarchy of the given original match
   * @param originalMatch a non-null match
   */
  protected void copyMatchHierarchy(EMatch originalMatch) {
    // Get the element of the match
    Role side = TARGET; // Try TARGET first
    EObject comparedElement = originalMatch.get(side);
    if (comparedElement == null) {
      side = side.opposite(); // Then try REFERENCE
      comparedElement = originalMatch.get(side);
    }
    // Get its container
    IEditableTreeDataScope<EObject> originalScope = originalComparison.getScope(side);
    EObject comparedContainer = originalScope.getContainer(comparedElement);
    if (comparedContainer != null) {
      EMatch sourceContainerMatch = originalComparison.getMapping().getMatchFor(
          comparedContainer, side);
      if (sourceContainerMatch != null && get(sourceContainerMatch) == null) {
        copy(sourceContainerMatch); // Hierarchy covered by recursion
      }
    }
  }
  
  /**
   * @see org.eclipse.emf.ecore.util.EcoreUtil.Copier#copyReference(org.eclipse.emf.ecore.EReference, org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject)
   */
  @Override
  protected void copyReference(EReference eReference, EObject eObject,
      EObject copyEObject) {
    if (eObject.eIsSet(eReference)) {
      EStructuralFeature.Setting setting = getTarget(eReference, eObject, copyEObject);
      if (setting != null) {
        Object value = eObject.eGet(eReference, resolveProxies);
        if (eReference.isMany()) {
          @SuppressWarnings("unchecked")
          InternalEList<EObject> source = (InternalEList<EObject>)value;
          @SuppressWarnings("unchecked")
          InternalEList<EObject> target = (InternalEList<EObject>)setting;
          if (source.isEmpty()) {
            target.clear();
          } else {
            boolean isBidirectional = eReference.getEOpposite() != null;
            int index = 0;
            for (Iterator<EObject> k = resolveProxies ? source.iterator() :
              source.basicIterator(); k.hasNext();) {
              EObject referencedEObject = k.next();
              EObject copyReferencedEObject = get(referencedEObject);
              if (copyReferencedEObject == null) {
                if (useOriginalReferences && !isBidirectional) {
                  // Take target side of comparison if available
                  EMatch match = originalComparison.getMapping().getMatchFor(
                      referencedEObject, TARGET.opposite());
                  if (match != null && !match.isPartial()) {
                    referencedEObject = match.get(TARGET);
                  }
                  target.addUnique(index, referencedEObject);
                  ++index;
                }
              } else {
                if (isBidirectional) {
                  int position = target.indexOf(copyReferencedEObject);
                  if (position == -1) {
                    target.addUnique(index, copyReferencedEObject);
                  } else if (index != position) {
                    target.move(index, copyReferencedEObject);
                  }
                } else {
                  target.addUnique(index, copyReferencedEObject);
                }
                ++index;
              }
            }
          }
        } else {
          if (value == null) {
            setting.set(null);
          } else {
            EObject copyReferencedEObject = get(value);
            if (copyReferencedEObject == null) {
              if (useOriginalReferences && eReference.getEOpposite() == null) {
                // Take target side of comparison if available
                EMatch match = originalComparison.getMapping().getMatchFor(
                    value, TARGET.opposite());
                if (match != null && !match.isPartial()) {
                  value = match.get(TARGET);
                }
                setting.set(value);
              }
            } else {
              setting.set(copyReferencedEObject);
            }
          }
        }
      }
    }
  }
  
  /**
   * @see org.eclipse.emf.ecore.util.EcoreUtil.Copier#createCopy(org.eclipse.emf.ecore.EObject)
   */
  @Override
  protected EObject createCopy(EObject eObject) {
    EObject result = super.createCopy(eObject);
    if (eObject instanceof DRepresentationDescriptor && result instanceof DRepresentationDescriptor) {
      originalRepresentationDescriptors.add((DRepresentationDescriptor) eObject);
    }
    return result;
  }
  
  /**
   * Return the DRepresentationDescriptors that have been copied
   * @return a non-null, potentially empty set
   */
  public Set<DRepresentationDescriptor> getOriginalRepresentationDescriptors() {
    return originalRepresentationDescriptors;
  }
  
  /**
   * Return the set of original elements from the non-target side that have been copied
   * @return a non-null, potentially empty, modifiable collection
   */
  public Collection<EObject> getOriginalSourceElements() {
    return originalSourceElements;
  }
  
  /**
   * Return the original matches that must be copied among the given match
   * and neighboring matches
   * @param originalMatch a non-null match
   * @return a non-null, potentially empty set of matches
   */
  protected Collection<EMatch> getSelectedOriginalMatches(EMatch originalMatch) {
    Collection<EMatch> result = new FHashSet<>();
    if (!originalMatch.getAllDifferences().isEmpty()) {
      result.add(originalMatch);
      for (IDifference<EObject> difference : originalMatch.getRelatedDifferences()) {
        if (difference instanceof EReferenceValuePresence) {
          EMatch valueMatch = ((EReferenceValuePresence)difference).getValueMatch();
          result.add(valueMatch); //TODO next elements for orders
        }
      }
    }
    return result;
  }
  
  /**
   * Initialize the given copy of a match according to the original one
   * @param originalMatch a non-null match
   * @param copyMatch a non-null match
   */
  protected void initMatch(EMatch originalMatch, EMatch copyMatch) {
    // Fully initialize match because of its equals method
    copyMatch.set(TARGET, originalMatch.get(TARGET));
    EObject originalComparedElement = originalMatch.get(TARGET.opposite());
    if (originalComparedElement != null) {
      // Copy source element without its contents
      EObject copyComparedElement = copy(originalComparedElement, false);
      copyMatch.set(TARGET.opposite(), copyComparedElement);
      originalSourceElements.add(originalComparedElement);
    }
    copyMatches.add(copyMatch);
  }
  
}