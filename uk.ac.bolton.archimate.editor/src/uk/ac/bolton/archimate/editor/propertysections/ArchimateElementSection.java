/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.propertysections;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IFilter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import uk.ac.bolton.archimate.editor.diagram.editparts.IArchimateEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.connections.IArchimateConnectionEditPart;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IArchimatePackage;


/**
 * Property Section for an Archimate Element
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateElementSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "uk.ac.bolton.archimate.help.elementPropertySection";
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter implements IFilter {
        @Override
        public boolean select(Object object) {
            return object instanceof IArchimateElement || object instanceof IArchimateEditPart 
                    || object instanceof IArchimateConnectionEditPart;
        }
    }

    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(final Notification msg) {
            Display.getDefault().asyncExec(new Runnable() {
            	public void run() {

            Object feature = msg.getFeature();
            // Element Name event (Undo/Redo and here)
            if(feature == IArchimatePackage.Literals.NAMEABLE__NAME) {
                refreshNameField();
                fPage.labelProviderChanged(null); // Update Main label
            }
            // Element Documentation event (Undo/Redo and here)
            else if(feature == IArchimatePackage.Literals.DOCUMENTABLE__DOCUMENTATION) {
                refreshDocumentationField();
            }
            	}
            });         	
        }
    };
    
    private IArchimateElement fArchimateElement;

    private PropertySectionTextControl fTextName;
    private PropertySectionTextControl fTextDocumentation;
    
    @Override
    protected void createControls(Composite parent) {
        fTextName = createNameControl(parent, "Add a name for this element here");
        fTextDocumentation = createDocumentationControl(parent, "Add documentation relating to this element here");

        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }

    @Override
    protected void setElement(Object element) {
        // IArchimateElement
        if(element instanceof IArchimateElement) {
            fArchimateElement = (IArchimateElement)element;
        }
        // IArchimateElement in a GEF Edit Part
        else if(element instanceof IAdaptable) {
            fArchimateElement = (IArchimateElement)((IAdaptable)element).getAdapter(IArchimateElement.class);
        }
        else {
            System.err.println("ArchimateElementSection wants to display for " + element);
        }
        
        refreshControls();
    }
    
    protected void refreshControls() {
        refreshNameField();
        refreshDocumentationField();
    }
    
    protected void refreshNameField() {
        if(fIsExecutingCommand) {
            return; 
        }
        fTextName.refresh(fArchimateElement);
    }
    
    protected void refreshDocumentationField() {
        if(fIsExecutingCommand) {
            return; 
        }
        fTextDocumentation.refresh(fArchimateElement);
    }

    @Override
    protected Adapter getECoreAdapter() {
        return eAdapter;
    }
    
    @Override
    protected EObject getEObject() {
        return fArchimateElement;
    }

    @Override
    public boolean shouldUseExtraSpace() {
        return true;
    }
}
