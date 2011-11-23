/**
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.views.tree;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.ui.IMemento;

import uk.ac.bolton.archimate.editor.model.IEditorModelManager;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IArchimateModelElement;
import uk.ac.bolton.archimate.model.IIdentifier;
import uk.ac.bolton.archimate.model.util.ArchimateModelUtils;


/**
 * Helper class to manage persistence of Tree state
 * 
 * @author Phillip Beauvoir
 */
public class TreeStateHelper {
    
    public static TreeStateHelper INSTANCE = new TreeStateHelper();
    
    private static final String ELEMENT_SEP_CHAR = " ";
    
    private static final String MEMENTO_EXPANDED = "expanded";
    private static final String MEMENTO_MODEL = "model";
    private static final String MEMENTO_FILE = "file";
    private static final String MEMENTO_ELEMENTS = "elements";
    
    // Expanded tree elements or element ids for the session
    private List<Object> fExpandedElements = new ArrayList<Object>();
    
    private TreeViewer fTreeViewer;
    
    private class FileMap {
        File file;
        String[] elements;
    }

    /**
     * Set the Memento on Application Open
     * @param memento
     */
    void setMemento(IMemento memento) {
        if(memento == null) {
            return;
        }
        
        // Store expanded elements as ids now, as the tree has not been created yet
        for(IMemento expandedMem : memento.getChildren(MEMENTO_EXPANDED)) {
            for(IMemento elementMem : expandedMem.getChildren(MEMENTO_MODEL)) {
                String file = elementMem.getString(MEMENTO_FILE);
                String elements = elementMem.getString(MEMENTO_ELEMENTS);
                if(file != null && elements != null) {
                    FileMap fm = new FileMap();
                    fm.file = new File(file);
                    fm.elements = elements.split(ELEMENT_SEP_CHAR);
                    fExpandedElements.add(fm);
                }
            }
        }
    }

    /**
     * Restore expanded elements on TreeView creation
     */
    void restoreExpandedTreeElements(TreeViewer viewer) {
        fTreeViewer = viewer;
        addDisposeListener();
        
        if(fExpandedElements.isEmpty()) {
            return;
        }
        
        for(Object o : fExpandedElements) {
            // Actual object
            if(o instanceof IArchimateModelElement) {
                fTreeViewer.expandToLevel(o, 1);
            }
            
            // String ids
            if(o instanceof FileMap) {
                try {
                    File file = ((FileMap)o).file;
                    String[] elements = ((FileMap)o).elements;
                    for(IArchimateModel model : IEditorModelManager.INSTANCE.getModels()) {
                        if(file.equals(model.getFile())) {
                            for(String id : elements) {
                                EObject element = ArchimateModelUtils.getObjectByID(model, id);
                                if(element != null) {
                                    fTreeViewer.expandToLevel(element, 1);
                                }
                            }
                            break; // found model
                        }
                    }
                }
                catch(Exception ex) {
                    // We don't want to fail just for some stupid string operation
                    ex.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Store expanded tree elements if View is closed
     */
    private void addDisposeListener() {
        fTreeViewer.getTree().addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                fExpandedElements.clear();
                for(Object element : fTreeViewer.getVisibleExpandedElements()) {
                    fExpandedElements.add(element);
                }
            }
        });
    }
    
    /**
     * Save expanded state of tree elements on Application close
     * @param memento
     */
    void saveStateOnApplicationClose(IMemento memento) {
        Hashtable<URI, String> map = new Hashtable<URI, String>();
        
        IMemento expandedMem = memento.createChild(MEMENTO_EXPANDED);

        for(Object element : fTreeViewer.getVisibleExpandedElements()) {
            if(element instanceof IIdentifier && element instanceof IArchimateModelElement) {
                // Only store if saved in a file
                URI file = ((IArchimateModelElement)element).getArchimateModel().getFile();
                if(file != null) {
                    String id = ((IIdentifier)element).getId();
                    String string = map.get(file);
                    if(string == null) {
                        string = id;
                    }
                    else {
                        string += ELEMENT_SEP_CHAR + id;
                    }
                    map.put(file, string);
                }
            }
        }
        
        for(URI file : map.keySet()) {
            IMemento elementMem = expandedMem.createChild(MEMENTO_MODEL);
            elementMem.putString(MEMENTO_FILE, file.toString());
            elementMem.putString(MEMENTO_ELEMENTS, map.get(file));
        }
    }
}
