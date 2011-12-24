/**
 * Copyright (c) 2010-2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.model;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Folder</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link uk.ac.bolton.archimate.model.IFolder#getElements <em>Elements</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.model.IFolder#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see uk.ac.bolton.archimate.model.IArchimatePackage#getFolder()
 * @model
 * @generated
 */
public interface IFolder extends IArchimateModelElement, IFolderContainer {
	/**
	 * Returns the value of the '<em><b>Elements</b></em>' containment reference list.
	 * The list contents are of type {@link uk.ac.bolton.archimate.model.IArchimateModelElement}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Elements</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Elements</em>' containment reference list.
	 * @see uk.ac.bolton.archimate.model.IArchimatePackage#getFolder_Elements()
	 * @model containment="true"
	 * @generated
	 */
	EList<IArchimateModelElement> getElements();

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * The literals are from the enumeration {@link uk.ac.bolton.archimate.model.FolderType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see uk.ac.bolton.archimate.model.FolderType
	 * @see #setType(FolderType)
	 * @see uk.ac.bolton.archimate.model.IArchimatePackage#getFolder_Type()
	 * @model
	 * @generated
	 */
	FolderType getType();

	/**
	 * Sets the value of the '{@link uk.ac.bolton.archimate.model.IFolder#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see uk.ac.bolton.archimate.model.FolderType
	 * @see #getType()
	 * @generated
	 */
	void setType(FolderType value);

} // IFolder
