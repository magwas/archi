/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.persistence.impl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchListener;
import org.eclipse.ui.PlatformUI;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import uk.ac.bolton.archimate.compatibility.CompatibilityHandlerException;
import uk.ac.bolton.archimate.compatibility.IncompatibleModelException;
import uk.ac.bolton.archimate.compatibility.LaterModelVersionException;
import uk.ac.bolton.archimate.compatibility.ModelCompatibility;
import uk.ac.bolton.archimate.editor.ArchimateEditorPlugin;
import uk.ac.bolton.archimate.editor.Logger;
import uk.ac.bolton.archimate.model.FolderType;
import uk.ac.bolton.archimate.model.IArchimateFactory;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IIdentifier;
import uk.ac.bolton.archimate.model.ModelVersion;
import uk.ac.bolton.archimate.model.util.ArchimateResourceFactory;
import uk.ac.bolton.archimate.persistence.IArchiveManager;
import uk.ac.bolton.archimate.persistence.IEditorModelManager;
import uk.ac.bolton.jdom.JDOMUtils;


/**
 * Editor Model Manager.<p>
 * <p>
 * Acts as an adapter to the Archimate Models passing on notifications to listeners
 * so that clients only have to register here once rather than for each model.<p>
 * Also can pass on arbitrary PropertyChangeEvents to registered listeners.<br>
 * Also manages CommandStacks for models.<br>
 * Also handles persistence of models.
 * 
 * @author Phillip Beauvoir
 */
public class EditorModelManager
implements IEditorModelManager {
    
    /**
     * Listener list
     */
    private PropertyChangeSupport fListeners = new PropertyChangeSupport(this);
    
    /**
     * Models Open
     */
    private List<IArchimateModel> fModels;
    
    /**
     * Which model in which resource?
     */
    
    private Map<IArchimateModel,Resource> fRepoContents = new HashMap<IArchimateModel,Resource>();
    
    /**
     * Backing File
     */
    private File backingFile = new File(ArchimateEditorPlugin.INSTANCE.getUserDataFolder(), "models.xml"); //$NON-NLS-1$
    
    /**
     * Listen to the App closing so we can ask to save
     */
    private IWorkbenchListener workBenchListener = new IWorkbenchListener() {
        public void postShutdown(IWorkbench workbench) {
        }

        public boolean preShutdown(IWorkbench  workbench, boolean forced) {
            // Handle modified models
            for(IArchimateModel model : getModels()) {
                if(isModelDirty(model)) {
                    try {
                        boolean result = askSaveModel(model);
                        if(!result) {
                            return false;
                        }
                    }
                    catch(IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            
            return true;
        }
    };
    
    public EditorModelManager() {
        PlatformUI.getWorkbench().addWorkbenchListener(workBenchListener);
    }
    
    @Override
    public List<IArchimateModel> getModels() {
        if(fModels == null) {
            fModels = new ArrayList<IArchimateModel>();
            
            try {
                loadState();
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }
        
        return fModels;
    }

    @Override
    public IArchimateModel createNewModel() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setName("(new model)");
        model.setDefaults();
        
        // Add one default diagram
        IDiagramModel diagramModel = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        diagramModel.setName("Default View");
        model.getFolder(FolderType.DIAGRAMS).getElements().add(diagramModel);
        
        // Add to Models
        getModels().add(model);
        
        // New Command Stack
        createNewCommandStack(model);
        
        // New Archive Manager
        createNewArchiveManager(model);
        
        firePropertyChange(this, PROPERTY_MODEL_CREATED, null, model);
        model.eAdapters().add(new ECoreAdapter());
        return model;
    }
    
    @Override
    public IArchimateModel openModel(URI file) {
        if(file == null || (file.isFile() && ! new File(file.toFileString()).exists()) || isModelLoaded(file)) {
        	System.out.println("not opening. isfile="+file.isFile()+
        			",loaded="+isModelLoaded(file));
        	return null;
        }
        
        IArchimateModel model = loadModel(file);
        if(model != null) {
            // Open Views of newly opened model if set in Preferences
            if(Preferences.doOpenDiagramsOnLoad()) {
                for(IDiagramModel dm : model.getDiagramModels()) {
                    EditorManager.openDiagramEditor(dm);
                }
            }
            
            firePropertyChange(this, PROPERTY_MODEL_OPENED, null, model);
        }
        
        return model;
    }
    
    @Override
    public void openModel(IArchimateModel model) {
        // Add to Models
        getModels().add(model);
        
        // New Command Stack
        createNewCommandStack(model);
        
        // New Archive Manager
        createNewArchiveManager(model);
        
        model.eAdapters().add(new ECoreAdapter());

        firePropertyChange(this, PROPERTY_MODEL_OPENED, null, model);
    }
    
    @Override
    public IArchimateModel loadModel(URI uri) {
/* FIXME -> persistence
    	if(uri == null) {
        	System.out.println("loadModel null");
            return null;
        }
        // Ascertain if this is an archive file
        boolean useArchiveFormat = IArchiveManager.FACTORY.isArchiveFile(uri);
        
        // Create the Resource
        ResourceSet resourceSet = ArchimateResourceFactory.createResourceSet();
        Resource resource = resourceSet.createResource(useArchiveFormat ?
                                                       IArchiveManager.FACTORY.createArchiveModelURI(file) :
                                                       URI.createFileURI(file.getAbsolutePath()));

        // Load the model file
        try {
            resource.load(null);
        }
        catch(IOException ex) {
            // Error occured loading model. Was it a disaster?
        	//System.out.println("exception:"+ex);
        	//ex.printStackTrace();
            try {
                ModelCompatibility.checkErrors(resource);
            }
            // Incompatible, don't load it
            catch(IncompatibleModelException ex1) {
                MessageDialog.openError(Display.getCurrent().getActiveShell(),
                        "Error opening model",
                        "Cannot open '" + file +  "'. " + "This model is incompatible."
                        + "\n" + ex1.getMessage());
                return null;
            }
        }
        
        // Once loaded - Check version number compatibility with user
        try {
            ModelCompatibility.checkVersion(resource);
        }
        catch(LaterModelVersionException ex) {
            boolean answer = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(),
                    "Opening model",
                    "'" + file +  "' is a later version model " + "(" + ex.getVersion() + ")." +
                    " Are you sure you want to continue opening it?");
            if(!answer) {
                return null;
            }
        }
        
        // And then fix any backward compatibility issues
        try {
            ModelCompatibility.fixCompatibility(resource);
        }
        catch(CompatibilityHandlerException ex) {
        }

        IArchimateModel model = (IArchimateModel)resource.getContents().get(0);
        model.setFile(uri);
        model.setDefaults();
        getModels().add(model);
        fRepoContents.put(model,resource);
       	model.eAdapters().add(new ECoreAdapter());
        // New Command Stack
        createNewCommandStack(model);
        
        // New Archive Manager
        createNewArchiveManager(model);
        
        // Initiate all diagram models to be marked as "saved" - this is for the editor view persistence
        markDiagramModelsAsSaved(model);

        // This last
        firePropertyChange(this, PROPERTY_MODEL_LOADED, null, model);
*/
        return null;
    }
    
    @Override
    public boolean closeModel(IArchimateModel model) throws IOException {
        // Check if model needs saving
        if(isModelDirty(model)) {
            boolean result = askSaveModel(model);
            if(!result) {
                return false;
            }
        }
        
        // Close the corresponding GEF editor(s) for this model *FIRST* before removing from model
        EditorManager.closeDiagramEditors(model);
        
        getModels().remove(model);
        fRepoContents.remove(model);
        model.eAdapters().clear();
        firePropertyChange(this, PROPERTY_MODEL_REMOVED, null, model);
        
        // Delete the CommandStack *LAST* because GEF Editor(s) will still reference it!
        deleteCommandStack(model);
        
        // Delete Archive Manager
        deleteArchiveManager(model);

        return true;
    }
    
    /**
     * Show dialog to save modified model
     * @param model
     * @return true if the user chose to save the model, false otherwise
     * @throws IOException 
     */
    private boolean askSaveModel(IArchimateModel model) throws IOException {
        MessageDialog dialog = new MessageDialog(Display.getCurrent().getActiveShell(),
                "Save Model",
                null,
                "' " + model.getName() + "' has been modified. Save changes?",
                MessageDialog.QUESTION,
                new String[] {
                    IDialogConstants.YES_LABEL,
                    IDialogConstants.NO_LABEL,
                    IDialogConstants.CANCEL_LABEL },
                0);
        
        
        int result = dialog.open();
        
        // Save
        if(result == 0) {
            return saveModel(model);
        }
        // Cancel
        else if(result == 2) {
            return false;
        }
        
        return true;
    }

    private void checkForIds(IArchimateModel model) {
    	TreeIterator<EObject> iter = ((EObject)model).eAllContents();
    	while(iter.hasNext()) {
    		EObject o = iter.next();
    		if (o instanceof IIdentifier) {
    			if(null == ((IIdentifier) o).getId()) {
    				System.out.println("Unidentifiered F...lying EObject: "+o);
    			}
    		}
    	}
    }
    @Override
    public boolean saveModel(IArchimateModel model) throws IOException {
    	/* FIXME -> persistence
    	checkForIds(model);
        // First time to save...
        if(model.getFile() == null) {
            URI file = askSaveModel();
            if(file == null) { // cancelled
                return false;
            }
            Resource resource = ArchimateResourceFactory.getOrCreateResource(file);
            resource.getContents().add(model);
            fRepoContents.put(model, resource);
            model.setFile(file);
        }
        
        File file = model.getFile();
        
        // Save backup
        if(file.exists()) {
            FileUtils.copyFile(file, new File(model.getFile().getAbsolutePath() + ".bak"), false);
        }

        // Set model version
        model.setVersion(ModelVersion.VERSION);
        
        // Use Archive Manager to save contents
        IArchiveManager archiveManager = (IArchiveManager)model.getAdapter(IArchiveManager.class);
        archiveManager.saveModel();
        
        // Set CommandStack Save point
        CommandStack stack = (CommandStack)model.getAdapter(CommandStack.class);
        stack.markSaveLocation();
        // Send notification to Tree
        firePropertyChange(model, COMMAND_STACK_CHANGED, true, false);
        
        // Set all diagram models to be marked as "saved" - this is for the editor view persistence
        markDiagramModelsAsSaved(model);
        
        firePropertyChange(this, PROPERTY_MODEL_SAVED, null, model);
*/        
        return true;
    }
    
    @Override
    public boolean saveModelAs(IArchimateModel model) throws IOException {
        URI file = askSaveModel();
        if(file == null) {
            return false;
        }
        model.setFile(file);        
        Resource resource = ArchimateResourceFactory.getOrCreateResource(file);
        resource.getContents().add(model);
        fRepoContents.remove(model);
        fRepoContents.put(model, resource);
        return saveModel(model);
    }
    
    @Override
    public boolean isModelLoaded(URI file) {
        if(file != null) {
            for(IArchimateModel model : getModels()) {
                if(file.equals(model.getFile())) {
                    return true;
                }
            }
        }
        
        return false;
    }

    @Override
    public boolean isModelDirty(IArchimateModel model) {
        if(model == null) {
            return false;
        }
        
        CommandStack stack = (CommandStack)model.getAdapter(CommandStack.class);
        return stack != null && stack.isDirty();
    }

    /**
     * Ask user for file name to save model
     * @return the file or null
     */
    private URI askSaveModel() {
        // On Mac if the app is minimised in the dock Display.getCurrent().getActiveShell() will return null
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        shell.setActive(); // Get focus on Mac
       	TargetSelectionDialog dialog = new TargetSelectionDialog(Display.getCurrent().getActiveShell(),SWT.SAVE);
        URI uri = dialog.open();
        if(uri == null) {
            return null;
        } 
        
        Integer filterindex = dialog.getFilterIndex();
        if((!(null == filterindex ))&&uri.isFile()) {
            // Only Windows adds the extension by default
            if(filterindex == 0 && !uri.toFileString().endsWith(ARCHIMATE_FILE_EXTENSION)) {
                uri = URI.createFileURI(uri.toFileString()+ ARCHIMATE_FILE_EXTENSION);
            } else if(filterindex == 1 && !uri.toFileString().endsWith(".xml")) {
            	uri = URI.createFileURI(uri.toFileString()+ ".xml");
            }
        }
        
        // Make sure we don't already have it open
        for(IArchimateModel m : getModels()) {
            if(uri.equals(m.getFile())) {
                MessageDialog.openWarning(shell, "Save Model", "' " + uri +
                        "' is already already open. Please choose another file name.");
                return null;
            }
        }
        
        // Make sure the file does not already exist
        if(uri.isFile() && new File(uri.toFileString()).exists()) {
            boolean result = MessageDialog.openQuestion(shell, "Save Model", "'" + uri +
                    "' already exists. Are you sure you want to overwrite it?");
            if(!result) {
                return null;
            }
        }
        
        return uri;
    }
    
    /**
     * Create a new ComandStack for the Model
     * @param model
     */
    private void createNewCommandStack(final IArchimateModel model) {
        CommandStack cmdStack = new CommandStack();
        
        // Forward on CommandStack Event to Tree
        cmdStack.addCommandStackListener(new CommandStackListener() {
            public void commandStackChanged(EventObject event) {
                // Send notification to Tree
                firePropertyChange(model, COMMAND_STACK_CHANGED, false, true);
            }
        });
        
        // Animate Commands
        AnimationUtil.registerCommandStack(cmdStack);
        
        model.setAdapter(CommandStack.class, cmdStack);
    }
    
    /**
     * Remove a CommandStack
     * @param model
     */
    private void deleteCommandStack(IArchimateModel model) {
        CommandStack stack = (CommandStack)model.getAdapter(CommandStack.class);
        if(stack != null) {
            stack.dispose();
        }
    }
    
    /**
     * Set all diagram models in a model to be marked as "saved" - this for the editor view persistence
     */
    private void markDiagramModelsAsSaved(IArchimateModel model) {
        for(IDiagramModel dm : model.getDiagramModels()) {
            dm.setAdapter(ADAPTER_PROPERTY_MODEL_SAVED, true);
        }
    }
    
    /**
     * Create a new ArchiveManager for the model
     */
    private IArchiveManager createNewArchiveManager(IArchimateModel model) {
        IArchiveManager archiveManager = IArchiveManager.FACTORY.createArchiveManager(model);
        model.setAdapter(IArchiveManager.class, archiveManager);
        
        // Load images now
        try {
            archiveManager.loadImages();
        }
        catch(IOException ex) {
            Logger.logError("Could not load images", ex);
            ex.printStackTrace();
        }
        
        return archiveManager;
    }
    
    /**
     * Remove the model's ArchiveManager
     */
    private void deleteArchiveManager(IArchimateModel model) {
        IArchiveManager archiveManager = (IArchiveManager)model.getAdapter(IArchiveManager.class);
        if(archiveManager != null) {
            archiveManager.dispose();
        }
    }

    //========================== Persist backing file  ==========================

    public void saveState() throws IOException {
        Document doc = new Document();
        Element rootElement = new Element("models");
        doc.setRootElement(rootElement);
        for(IArchimateModel model : getModels()) {
            URI file = model.getFile(); // has been saved
            if(file != null) {
                Element modelElement = new Element("model");
                if(file.hasAbsolutePath()) {
                	modelElement.setAttribute("file", file.toString());
                } else {
                	modelElement.setAttribute("file",file.toFileString());
                }
                rootElement.addContent(modelElement);
            }
        }
        JDOMUtils.write2XMLFile(doc, backingFile);
    }
    
    private void loadState() throws IOException, JDOMException {
        if(backingFile.exists()) {
            Document doc = JDOMUtils.readXMLFile(backingFile);
            if(doc.hasRootElement()) {
                Element rootElement = doc.getRootElement();
                for(Object e : rootElement.getChildren("model")) {
                    Element modelElement = (Element)e;
                    String filePath = modelElement.getAttributeValue("file");
                    if(filePath != null) {
                        loadModel(URI.createURI(filePath));
                    }
                }
            }
        }
    }
    
    //========================== Model Listener events  ==========================

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        fListeners.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        fListeners.removePropertyChangeListener(listener);
    }
    
    public void firePropertyChange(Object source, String prop, Object oldValue, Object newValue) {
        fListeners.firePropertyChange(new PropertyChangeEvent(source, prop, oldValue, newValue));
    }
    
    // ======================= ECore Adapter =========================================
    
    /**
     * Adapter listener class.
     * Forwards on messages so that listeners don't have to adapt to ECore objects
     */
    private class ECoreAdapter extends EContentAdapter {
        @Override
        public void notifyChanged(final Notification msg) {
            super.notifyChanged(msg);
            Display.getDefault().asyncExec(new Runnable() {
            	public void run() {
                    // Forward on to listeners...
                    firePropertyChange(this, PROPERTY_ECORE_EVENT, null, msg);
            	}
            	});
        }
    }
}
