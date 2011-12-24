/**
 * Copyright (c) 2010-2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.model.impl;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.internal.cdo.CDOObjectImpl;

import uk.ac.bolton.archimate.model.FolderType;
import uk.ac.bolton.archimate.model.IAdapter;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IDocumentable;
import uk.ac.bolton.archimate.model.IFolder;
import uk.ac.bolton.archimate.model.IFolderContainer;
import uk.ac.bolton.archimate.model.INameable;
import uk.ac.bolton.archimate.model.IProperties;
import uk.ac.bolton.archimate.model.IProperty;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link uk.ac.bolton.archimate.model.impl.ArchimateModel#getId <em>Id</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.model.impl.ArchimateModel#getName <em>Name</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.model.impl.ArchimateModel#getProperties <em>Properties</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.model.impl.ArchimateModel#getDocumentation <em>Documentation</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.model.impl.ArchimateModel#getArchimateModel <em>Archimate Model</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.model.impl.ArchimateModel#getFolders <em>Folders</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.model.impl.ArchimateModel#getFile <em>File</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.model.impl.ArchimateModel#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ArchimateModel extends CDOObjectImpl implements IArchimateModel {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ArchimateModel() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return IArchimatePackage.Literals.ARCHIMATE_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected int eStaticFeatureCount() {
		return 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getId() {
		return (String)eGet(IArchimatePackage.Literals.IDENTIFIER__ID, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setId(String newId) {
		eSet(IArchimatePackage.Literals.IDENTIFIER__ID, newId);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return (String)eGet(IArchimatePackage.Literals.NAMEABLE__NAME, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		eSet(IArchimatePackage.Literals.NAMEABLE__NAME, newName);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public EList<IProperty> getProperties() {
		return (EList<IProperty>)eGet(IArchimatePackage.Literals.PROPERTIES__PROPERTIES, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDocumentation() {
		return (String)eGet(IArchimatePackage.Literals.DOCUMENTABLE__DOCUMENTATION, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDocumentation(String newDocumentation) {
		eSet(IArchimatePackage.Literals.DOCUMENTABLE__DOCUMENTATION, newDocumentation);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IArchimateModel getArchimateModel() {
		return (IArchimateModel)eGet(IArchimatePackage.Literals.ARCHIMATE_MODEL_ELEMENT__ARCHIMATE_MODEL, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public EList<IFolder> getFolders() {
		return (EList<IFolder>)eGet(IArchimatePackage.Literals.FOLDER_CONTAINER__FOLDERS, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public URI getFile() {
		return (URI)eGet(IArchimatePackage.Literals.ARCHIMATE_MODEL__FILE, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFile(URI newFile) {
		eSet(IArchimatePackage.Literals.ARCHIMATE_MODEL__FILE, newFile);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getVersion() {
		return (String)eGet(IArchimatePackage.Literals.ARCHIMATE_MODEL__VERSION, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVersion(String newVersion) {
		eSet(IArchimatePackage.Literals.ARCHIMATE_MODEL__VERSION, newVersion);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDefaults() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IFolder addDerivedRelationsFolder() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void removeDerivedRelationsFolder() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IFolder getDefaultFolderForElement(EObject element) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IDiagramModel getDefaultDiagramModel() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<IDiagramModel> getDiagramModels() {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IFolder getFolder(FolderType type) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getAdapter(Object adapter) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAdapter(Object adapter, Object object) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == INameable.class) {
			switch (derivedFeatureID) {
				case IArchimatePackage.ARCHIMATE_MODEL__NAME: return IArchimatePackage.NAMEABLE__NAME;
				default: return -1;
			}
		}
		if (baseClass == IProperties.class) {
			switch (derivedFeatureID) {
				case IArchimatePackage.ARCHIMATE_MODEL__PROPERTIES: return IArchimatePackage.PROPERTIES__PROPERTIES;
				default: return -1;
			}
		}
		if (baseClass == IDocumentable.class) {
			switch (derivedFeatureID) {
				case IArchimatePackage.ARCHIMATE_MODEL__DOCUMENTATION: return IArchimatePackage.DOCUMENTABLE__DOCUMENTATION;
				default: return -1;
			}
		}
		if (baseClass == IAdapter.class) {
			switch (derivedFeatureID) {
				default: return -1;
			}
		}
		if (baseClass == IFolderContainer.class) {
			switch (derivedFeatureID) {
				case IArchimatePackage.ARCHIMATE_MODEL__FOLDERS: return IArchimatePackage.FOLDER_CONTAINER__FOLDERS;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == INameable.class) {
			switch (baseFeatureID) {
				case IArchimatePackage.NAMEABLE__NAME: return IArchimatePackage.ARCHIMATE_MODEL__NAME;
				default: return -1;
			}
		}
		if (baseClass == IProperties.class) {
			switch (baseFeatureID) {
				case IArchimatePackage.PROPERTIES__PROPERTIES: return IArchimatePackage.ARCHIMATE_MODEL__PROPERTIES;
				default: return -1;
			}
		}
		if (baseClass == IDocumentable.class) {
			switch (baseFeatureID) {
				case IArchimatePackage.DOCUMENTABLE__DOCUMENTATION: return IArchimatePackage.ARCHIMATE_MODEL__DOCUMENTATION;
				default: return -1;
			}
		}
		if (baseClass == IAdapter.class) {
			switch (baseFeatureID) {
				default: return -1;
			}
		}
		if (baseClass == IFolderContainer.class) {
			switch (baseFeatureID) {
				case IArchimatePackage.FOLDER_CONTAINER__FOLDERS: return IArchimatePackage.ARCHIMATE_MODEL__FOLDERS;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

} //ArchimateModel
