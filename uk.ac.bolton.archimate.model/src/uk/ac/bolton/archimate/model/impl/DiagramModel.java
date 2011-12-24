/**
 * Copyright (c) 2010-2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.model.impl;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.internal.cdo.CDOObjectImpl;

import uk.ac.bolton.archimate.model.IAdapter;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.ICloneable;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IDiagramModelComponent;
import uk.ac.bolton.archimate.model.IDiagramModelContainer;
import uk.ac.bolton.archimate.model.IDiagramModelObject;
import uk.ac.bolton.archimate.model.IDocumentable;
import uk.ac.bolton.archimate.model.INameable;
import uk.ac.bolton.archimate.model.IProperties;
import uk.ac.bolton.archimate.model.IProperty;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Diagram Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link uk.ac.bolton.archimate.model.impl.DiagramModel#getId <em>Id</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.model.impl.DiagramModel#getName <em>Name</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.model.impl.DiagramModel#getProperties <em>Properties</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.model.impl.DiagramModel#getDocumentation <em>Documentation</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.model.impl.DiagramModel#getArchimateModel <em>Archimate Model</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.model.impl.DiagramModel#getDiagramModel <em>Diagram Model</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.model.impl.DiagramModel#getChildren <em>Children</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.model.impl.DiagramModel#getConnectionRouterType <em>Connection Router Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class DiagramModel extends CDOObjectImpl implements IDiagramModel {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DiagramModel() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return IArchimatePackage.Literals.DIAGRAM_MODEL;
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
	public IDiagramModel getDiagramModel() {
		return (IDiagramModel)eGet(IArchimatePackage.Literals.DIAGRAM_MODEL_COMPONENT__DIAGRAM_MODEL, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public EList<IDiagramModelObject> getChildren() {
		return (EList<IDiagramModelObject>)eGet(IArchimatePackage.Literals.DIAGRAM_MODEL_CONTAINER__CHILDREN, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getConnectionRouterType() {
		return (Integer)eGet(IArchimatePackage.Literals.DIAGRAM_MODEL__CONNECTION_ROUTER_TYPE, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setConnectionRouterType(int newConnectionRouterType) {
		eSet(IArchimatePackage.Literals.DIAGRAM_MODEL__CONNECTION_ROUTER_TYPE, newConnectionRouterType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getCopy() {
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
				case IArchimatePackage.DIAGRAM_MODEL__NAME: return IArchimatePackage.NAMEABLE__NAME;
				default: return -1;
			}
		}
		if (baseClass == IProperties.class) {
			switch (derivedFeatureID) {
				case IArchimatePackage.DIAGRAM_MODEL__PROPERTIES: return IArchimatePackage.PROPERTIES__PROPERTIES;
				default: return -1;
			}
		}
		if (baseClass == IDocumentable.class) {
			switch (derivedFeatureID) {
				case IArchimatePackage.DIAGRAM_MODEL__DOCUMENTATION: return IArchimatePackage.DOCUMENTABLE__DOCUMENTATION;
				default: return -1;
			}
		}
		if (baseClass == IAdapter.class) {
			switch (derivedFeatureID) {
				default: return -1;
			}
		}
		if (baseClass == ICloneable.class) {
			switch (derivedFeatureID) {
				default: return -1;
			}
		}
		if (baseClass == IDiagramModelComponent.class) {
			switch (derivedFeatureID) {
				case IArchimatePackage.DIAGRAM_MODEL__DIAGRAM_MODEL: return IArchimatePackage.DIAGRAM_MODEL_COMPONENT__DIAGRAM_MODEL;
				default: return -1;
			}
		}
		if (baseClass == IDiagramModelContainer.class) {
			switch (derivedFeatureID) {
				case IArchimatePackage.DIAGRAM_MODEL__CHILDREN: return IArchimatePackage.DIAGRAM_MODEL_CONTAINER__CHILDREN;
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
				case IArchimatePackage.NAMEABLE__NAME: return IArchimatePackage.DIAGRAM_MODEL__NAME;
				default: return -1;
			}
		}
		if (baseClass == IProperties.class) {
			switch (baseFeatureID) {
				case IArchimatePackage.PROPERTIES__PROPERTIES: return IArchimatePackage.DIAGRAM_MODEL__PROPERTIES;
				default: return -1;
			}
		}
		if (baseClass == IDocumentable.class) {
			switch (baseFeatureID) {
				case IArchimatePackage.DOCUMENTABLE__DOCUMENTATION: return IArchimatePackage.DIAGRAM_MODEL__DOCUMENTATION;
				default: return -1;
			}
		}
		if (baseClass == IAdapter.class) {
			switch (baseFeatureID) {
				default: return -1;
			}
		}
		if (baseClass == ICloneable.class) {
			switch (baseFeatureID) {
				default: return -1;
			}
		}
		if (baseClass == IDiagramModelComponent.class) {
			switch (baseFeatureID) {
				case IArchimatePackage.DIAGRAM_MODEL_COMPONENT__DIAGRAM_MODEL: return IArchimatePackage.DIAGRAM_MODEL__DIAGRAM_MODEL;
				default: return -1;
			}
		}
		if (baseClass == IDiagramModelContainer.class) {
			switch (baseFeatureID) {
				case IArchimatePackage.DIAGRAM_MODEL_CONTAINER__CHILDREN: return IArchimatePackage.DIAGRAM_MODEL__CHILDREN;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

} //DiagramModel
