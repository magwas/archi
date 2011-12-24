/**
 * Copyright (c) 2010-2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.model.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IBorderObject;
import uk.ac.bolton.archimate.model.IDiagramModelImage;
import uk.ac.bolton.archimate.model.IDiagramModelImageProvider;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Diagram Model Image</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link uk.ac.bolton.archimate.model.impl.DiagramModelImage#getBorderColor <em>Border Color</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.model.impl.DiagramModelImage#getImagePath <em>Image Path</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DiagramModelImage extends DiagramModelObject implements IDiagramModelImage {
    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected DiagramModelImage() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
		return IArchimatePackage.Literals.DIAGRAM_MODEL_IMAGE;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getBorderColor() {
		return (String)eGet(IArchimatePackage.Literals.BORDER_OBJECT__BORDER_COLOR, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setBorderColor(String newBorderColor) {
		eSet(IArchimatePackage.Literals.BORDER_OBJECT__BORDER_COLOR, newBorderColor);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public String getImagePath() {
		return (String)eGet(IArchimatePackage.Literals.DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH, true);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setImagePath(String newImagePath) {
		eSet(IArchimatePackage.Literals.DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH, newImagePath);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == IBorderObject.class) {
			switch (derivedFeatureID) {
				case IArchimatePackage.DIAGRAM_MODEL_IMAGE__BORDER_COLOR: return IArchimatePackage.BORDER_OBJECT__BORDER_COLOR;
				default: return -1;
			}
		}
		if (baseClass == IDiagramModelImageProvider.class) {
			switch (derivedFeatureID) {
				case IArchimatePackage.DIAGRAM_MODEL_IMAGE__IMAGE_PATH: return IArchimatePackage.DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH;
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
		if (baseClass == IBorderObject.class) {
			switch (baseFeatureID) {
				case IArchimatePackage.BORDER_OBJECT__BORDER_COLOR: return IArchimatePackage.DIAGRAM_MODEL_IMAGE__BORDER_COLOR;
				default: return -1;
			}
		}
		if (baseClass == IDiagramModelImageProvider.class) {
			switch (baseFeatureID) {
				case IArchimatePackage.DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH: return IArchimatePackage.DIAGRAM_MODEL_IMAGE__IMAGE_PATH;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

} //DiagramModelImage
