/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.model;

import java.beans.PropertyChangeListener;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.common.util.URI;

import uk.ac.bolton.archimate.editor.model.impl.EditorModelManager;
import uk.ac.bolton.archimate.model.IArchimateModel;


/**
 * IEditorModelHandler
 * 
 * @author Phillip Beauvoir
 */
public interface IEditorModelManager {
    
    String ARCHIMATE_FILE_EXTENSION = ".archimate";
    String ARCHIMATE_FILE_WILDCARD = "*.archimate";
    
    String PROPERTY_MODEL_CREATED = "IEditorModelManager.model.created";
    String PROPERTY_MODEL_OPENED = "IEditorModelManager.model.opened";
    String PROPERTY_MODEL_LOADED = "IEditorModelManager.model.loaded";
    String PROPERTY_MODEL_REMOVED = "IEditorModelManager.model.removed";
    String PROPERTY_MODEL_SAVED = "IEditorModelManager.model.saved";
    
    String COMMAND_STACK_CHANGED = "IEditorModelManager.model.dirty";

    String PROPERTY_ECORE_EVENT = "IEditorModelManager.ecore.event";
    
    /*
     *  Notification that many ECore events will be fired in succession.
     *  Listeners can choose to then ignore the events and then update when notified of end.
     */
    String PROPERTY_ECORE_EVENTS_START = "IEditorModelManager.ecore.events.start";
    String PROPERTY_ECORE_EVENTS_END = "IEditorModelManager.ecore.events.end";
    
    /*
     * If the user creates a new view and it's open and then the user closes the application without first
     * saving the model, then Eclipse tries to restore it again next time. So we don't persist the state
     * of the diagram if the diagram view is not saved.
     */
    String ADAPTER_PROPERTY_MODEL_SAVED = "saved";
    
    /**
     * The singleton instance of the Editor Model Manager
     */
    IEditorModelManager INSTANCE = new EditorModelManager();
    
    /**
     * @return Models
     */
    List<IArchimateModel> getModels();

    /**
     * @return New Model
     */
    IArchimateModel createNewModel();
    
    /**
     * Open a model by loading it and opening its Views
     * @return The newly opened model or null
     */
    IArchimateModel openModel(URI file);
    
    /**
     * Open an existing model
     * @param model
     */
    void openModel(IArchimateModel model);
    
    /**
     * Load a model
     * @param file
     * @return The newly loaded model or null
     */
    IArchimateModel loadModel(URI file);
    
    /**
     * Close a model
     * @param model
     * @return false if user cancels
     * @throws IOException
     */
    boolean closeModel(IArchimateModel model) throws IOException;
    
    /**
     * Save model asking user for file name if needed
     * @param model
     * @return false if user cancels
     * @throws IOException
     */
    boolean saveModel(IArchimateModel model) throws IOException;
    
    /**
     * Save model as asking user for file name
     * @param model
     * @return false if user cancels
     * @throws IOException
     */
    boolean saveModelAs(IArchimateModel model) throws IOException;
    
    /**
     * Check if the model needs saving
     * @param model
     * @return True if model has been changed and needs saving
     */
    boolean isModelDirty(IArchimateModel model);
    
    /**
     * Save the state of loaded models
     * @throws IOException
     */
    void saveState() throws IOException;
    
    /**
     * @param file
     * @return True if the model backed by file is already loaded
     */
    boolean isModelLoaded(URI file);
    
    /**
     * Add a Property Change Listener
     * @param listener
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Remove a Property Change Listener
     * @param listener
     */
    void removePropertyChangeListener(PropertyChangeListener listener);
    
    /**
     * Fire a Property Change event
     * @param source The object affected
     * @param prop The property that changed
     * @param oldValue Old Value 
     * @param newValue New Value
     */
    void firePropertyChange(Object source, String prop, Object oldValue, Object newValue);

}
