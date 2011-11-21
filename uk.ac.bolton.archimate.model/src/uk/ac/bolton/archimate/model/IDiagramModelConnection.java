/**
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.model;

import org.eclipse.emf.common.util.EList;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Diagram Model Connection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link uk.ac.bolton.archimate.model.IDiagramModelConnection#getText <em>Text</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.model.IDiagramModelConnection#getTextPosition <em>Text Position</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.model.IDiagramModelConnection#getSource <em>Source</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.model.IDiagramModelConnection#getTarget <em>Target</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.model.IDiagramModelConnection#getBendpoints <em>Bendpoints</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.model.IDiagramModelConnection#getLineWidth <em>Line Width</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.model.IDiagramModelConnection#getLineColor <em>Line Color</em>}</li>
 *   <li>{@link uk.ac.bolton.archimate.model.IDiagramModelConnection#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see uk.ac.bolton.archimate.model.IArchimatePackage#getDiagramModelConnection()
 * @model
 * @generated
 */
public interface IDiagramModelConnection extends IDiagramModelComponent, IFontAttribute, IProperties, IDocumentable {
    
    /**
     * Text Position at source
     */
    int TEXT_POSITION_SOURCE = 0;

    /**
     * Text Position at middle
     */
    int TEXT_POSITION_MID = 1;
    
    /**
     * Text Position at target
     */
    int TEXT_POSITION_TARGET = 2;
    
    /**
	 * Returns the value of the '<em><b>Text</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Text</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Text</em>' attribute.
	 * @see #setText(String)
	 * @see uk.ac.bolton.archimate.model.IArchimatePackage#getDiagramModelConnection_Text()
	 * @model default=""
	 * @generated
	 */
    String getText();

    /**
	 * Sets the value of the '{@link uk.ac.bolton.archimate.model.IDiagramModelConnection#getText <em>Text</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Text</em>' attribute.
	 * @see #getText()
	 * @generated
	 */
    void setText(String value);

    /**
	 * Returns the value of the '<em><b>Text Position</b></em>' attribute.
	 * The default value is <code>"1"</code>.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Text Position</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Text Position</em>' attribute.
	 * @see #setTextPosition(int)
	 * @see uk.ac.bolton.archimate.model.IArchimatePackage#getDiagramModelConnection_TextPosition()
	 * @model default="1"
	 * @generated
	 */
    int getTextPosition();

    /**
	 * Sets the value of the '{@link uk.ac.bolton.archimate.model.IDiagramModelConnection#getTextPosition <em>Text Position</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Text Position</em>' attribute.
	 * @see #getTextPosition()
	 * @generated
	 */
    void setTextPosition(int value);

    /**
	 * Returns the value of the '<em><b>Source</b></em>' reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Source</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Source</em>' reference.
	 * @see #setSource(IDiagramModelObject)
	 * @see uk.ac.bolton.archimate.model.IArchimatePackage#getDiagramModelConnection_Source()
	 * @model resolveProxies="false"
	 * @generated
	 */
    IDiagramModelObject getSource();

    /**
	 * Sets the value of the '{@link uk.ac.bolton.archimate.model.IDiagramModelConnection#getSource <em>Source</em>}' reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source</em>' reference.
	 * @see #getSource()
	 * @generated
	 */
    void setSource(IDiagramModelObject value);

    /**
	 * Returns the value of the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Target</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Target</em>' reference.
	 * @see #setTarget(IDiagramModelObject)
	 * @see uk.ac.bolton.archimate.model.IArchimatePackage#getDiagramModelConnection_Target()
	 * @model resolveProxies="false"
	 * @generated
	 */
    IDiagramModelObject getTarget();

    /**
	 * Sets the value of the '{@link uk.ac.bolton.archimate.model.IDiagramModelConnection#getTarget <em>Target</em>}' reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target</em>' reference.
	 * @see #getTarget()
	 * @generated
	 */
    void setTarget(IDiagramModelObject value);

    /**
	 * Returns the value of the '<em><b>Bendpoints</b></em>' containment reference list.
	 * The list contents are of type {@link uk.ac.bolton.archimate.model.IDiagramModelBendpoint}.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Bendpoints</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Bendpoints</em>' containment reference list.
	 * @see uk.ac.bolton.archimate.model.IArchimatePackage#getDiagramModelConnection_Bendpoints()
	 * @model containment="true"
	 * @generated
	 */
    EList<IDiagramModelBendpoint> getBendpoints();

    /**
	 * Returns the value of the '<em><b>Line Width</b></em>' attribute.
	 * The default value is <code>"1"</code>.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Line Width</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Line Width</em>' attribute.
	 * @see #setLineWidth(int)
	 * @see uk.ac.bolton.archimate.model.IArchimatePackage#getDiagramModelConnection_LineWidth()
	 * @model default="1"
	 * @generated
	 */
    int getLineWidth();

    /**
	 * Sets the value of the '{@link uk.ac.bolton.archimate.model.IDiagramModelConnection#getLineWidth <em>Line Width</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Line Width</em>' attribute.
	 * @see #getLineWidth()
	 * @generated
	 */
    void setLineWidth(int value);

    /**
	 * Returns the value of the '<em><b>Line Color</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Line Color</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Line Color</em>' attribute.
	 * @see #setLineColor(String)
	 * @see uk.ac.bolton.archimate.model.IArchimatePackage#getDiagramModelConnection_LineColor()
	 * @model
	 * @generated
	 */
    String getLineColor();

    /**
	 * Sets the value of the '{@link uk.ac.bolton.archimate.model.IDiagramModelConnection#getLineColor <em>Line Color</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Line Color</em>' attribute.
	 * @see #getLineColor()
	 * @generated
	 */
    void setLineColor(String value);

    /**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(String)
	 * @see uk.ac.bolton.archimate.model.IArchimatePackage#getDiagramModelConnection_Type()
	 * @model
	 * @generated
	 */
    String getType();

    /**
	 * Sets the value of the '{@link uk.ac.bolton.archimate.model.IDiagramModelConnection#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
    void setType(String value);

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
    void disconnect();

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
    void reconnect();

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
    void connect(IDiagramModelObject source, IDiagramModelObject target);
} // IDiagramModelConnection
