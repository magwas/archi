/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.views.tree.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;

import uk.ac.bolton.archimate.editor.model.IEditorModelManager;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IArchimateModelElement;
import uk.ac.bolton.archimate.model.IDiagramModelComponent;
import uk.ac.bolton.archimate.model.IFolder;


/**
 * Sort Folder Command.
 * 
 * @author Phillip Beauvoir
 */
public class SortFolderCommand extends Command implements Comparator<EObject>  {
    
    private IFolder fFolder;
    private List<IArchimateModelElement> fList;

    public SortFolderCommand(IFolder folder) {
        setLabel("Sort");
        fFolder = folder;
        
        // Keep a copy of the orginal order
        fList = new ArrayList<IArchimateModelElement>();
        for(IArchimateModelElement o : fFolder.getElements()) {
            fList.add(o);
        }
    }
    
    @Override
    public void execute() {
        IEditorModelManager.INSTANCE.firePropertyChange(this,
                IEditorModelManager.PROPERTY_ECORE_EVENTS_START, false, true);
        
        ECollections.sort(fFolder.getElements(), this);
        
        IEditorModelManager.INSTANCE.firePropertyChange(this,
                IEditorModelManager.PROPERTY_ECORE_EVENTS_END, false, true);
    }
    
    @Override
    public void undo() {
        IEditorModelManager.INSTANCE.firePropertyChange(this,
                IEditorModelManager.PROPERTY_ECORE_EVENTS_START, false, true);

        fFolder.getElements().clear();
        fFolder.getElements().addAll(fList);
        
        IEditorModelManager.INSTANCE.firePropertyChange(this,
                IEditorModelManager.PROPERTY_ECORE_EVENTS_END, false, true);
    }
    
    @Override
    public void dispose() {
        fFolder = null;
        fList = null;
    }

    @Override
    public int compare(EObject o1, EObject o2) {
        String name1 = null, name2 = null;
        
        if(o1 instanceof IDiagramModelComponent && o2 instanceof IDiagramModelComponent) {
            name1 = ((IDiagramModelComponent)o1).getName();
            name2 = ((IDiagramModelComponent)o2).getName();
        }
        else if(o1 instanceof IArchimateElement && o2 instanceof IArchimateElement) {
            name1 = ((IArchimateElement)o1).getName();
            name2 = ((IArchimateElement)o2).getName();
        }
        
        if(name1 == null) {
            name1 = "";
        }
        if(name2 == null) {
            name2 = "";
        }
        
        return name1.compareTo(name2);
    }
}
