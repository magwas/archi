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
 * A representation of the model object '<em><b>Adapter</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see uk.ac.bolton.archimate.model.IArchimatePackage#getAdapter()
 * @model interface="true" abstract="true"
 * @extends CDOObject
 * @generated
 */
public interface IAdapter extends CDOObject {
    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
    Object getAdapter(Object adapter);

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
    void setAdapter(Object adapter, Object object);

} // IAdapter
