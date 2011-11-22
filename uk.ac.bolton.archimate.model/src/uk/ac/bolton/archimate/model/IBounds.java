/**
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.model;

import org.eclipse.emf.cdo.CDOObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Bounds</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link uk.ac.bolton.archimate.model.IBounds#getX <em>X</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.model.IBounds#getY <em>Y</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.model.IBounds#getWidth <em>Width</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.model.IBounds#getHeight <em>Height</em>}</li>
 * </ul>
 * </p>
 *
 * @see uk.ac.bolton.archimate.model.IArchimatePackage#getBounds()
 * @model
 * @extends CDOObject
 * @generated
 */
public interface IBounds extends CDOObject {
    /**
	 * Returns the value of the '<em><b>X</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>X</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>X</em>' attribute.
	 * @see #setX(int)
	 * @see uk.ac.bolton.archimate.model.IArchimatePackage#getBounds_X()
	 * @model
	 * @generated
	 */
    int getX();

    /**
	 * Sets the value of the '{@link uk.ac.bolton.archimate.model.IBounds#getX <em>X</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>X</em>' attribute.
	 * @see #getX()
	 * @generated
	 */
    void setX(int value);

    /**
	 * Returns the value of the '<em><b>Y</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Y</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Y</em>' attribute.
	 * @see #setY(int)
	 * @see uk.ac.bolton.archimate.model.IArchimatePackage#getBounds_Y()
	 * @model
	 * @generated
	 */
    int getY();

    /**
	 * Sets the value of the '{@link uk.ac.bolton.archimate.model.IBounds#getY <em>Y</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Y</em>' attribute.
	 * @see #getY()
	 * @generated
	 */
    void setY(int value);

    /**
	 * Returns the value of the '<em><b>Width</b></em>' attribute.
	 * The default value is <code>"-1"</code>.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Width</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Width</em>' attribute.
	 * @see #setWidth(int)
	 * @see uk.ac.bolton.archimate.model.IArchimatePackage#getBounds_Width()
	 * @model default="-1"
	 * @generated
	 */
    int getWidth();

    /**
	 * Sets the value of the '{@link uk.ac.bolton.archimate.model.IBounds#getWidth <em>Width</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Width</em>' attribute.
	 * @see #getWidth()
	 * @generated
	 */
    void setWidth(int value);

    /**
	 * Returns the value of the '<em><b>Height</b></em>' attribute.
	 * The default value is <code>"-1"</code>.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Height</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Height</em>' attribute.
	 * @see #setHeight(int)
	 * @see uk.ac.bolton.archimate.model.IArchimatePackage#getBounds_Height()
	 * @model default="-1"
	 * @generated
	 */
    int getHeight();

    /**
	 * Sets the value of the '{@link uk.ac.bolton.archimate.model.IBounds#getHeight <em>Height</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Height</em>' attribute.
	 * @see #getHeight()
	 * @generated
	 */
    void setHeight(int value);

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
    IBounds getCopy();

} // IBounds
