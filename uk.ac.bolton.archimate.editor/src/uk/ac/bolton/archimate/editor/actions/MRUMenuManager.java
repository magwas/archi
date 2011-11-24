/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.actions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;

import uk.ac.bolton.archimate.editor.model.IEditorModelManager;
import uk.ac.bolton.archimate.editor.preferences.IPreferenceConstants;
import uk.ac.bolton.archimate.editor.preferences.Preferences;
import uk.ac.bolton.archimate.editor.utils.StringUtils;
import uk.ac.bolton.archimate.model.IArchimateModel;


/**
 * MRU Files Menu Manager
 * 
 * @author Phillip Beauvoir
 */
public class MRUMenuManager extends MenuManager implements PropertyChangeListener {
    
    private static final String MRU_PREFS_KEY = "MRU";
    
    private List<URI> fMRU_List = new ArrayList<URI>();
    
    private IWorkbenchWindow fWindow;
    
    private int MAX;
    
    public MRUMenuManager(IWorkbenchWindow window) {
        super("Open Recent", "open_recent_menu");
        
        fWindow = window;
        
        MAX = getCurrentMRUMax();
        
        loadList();
        
        createMenuItems();
        
        IEditorModelManager.INSTANCE.addPropertyChangeListener(this);
        
        // Changed max
        Preferences.STORE.addPropertyChangeListener(new IPropertyChangeListener() {
            @Override
            public void propertyChange(org.eclipse.jface.util.PropertyChangeEvent event) {
                if(IPreferenceConstants.MRU_MAX.equals(event.getProperty())) {
                    MAX = getCurrentMRUMax();
                }
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(IEditorModelManager.PROPERTY_MODEL_OPENED == evt.getPropertyName() || 
                                        IEditorModelManager.PROPERTY_MODEL_SAVED == evt.getPropertyName()) {
            
            IArchimateModel model = (IArchimateModel)evt.getNewValue();
            if(model != null && model.getFile() != null && !isTempFile(model.getFile()) && isExistingFile(model.getFile())) {
                addToList(model.getFile());
                createMenuItems();
            } /*else {
            	System.out.println("not added to list:"+model);
            	System.out.println("not added to list:"+model.getFile());
            }*/
        }
    }
    
    private boolean isExistingFile(URI file) {
    	if(!file.isFile()) {//FIXME we treat CDO uris as always existing
    		return true;
    	}
    	return (file.isFile() && new File(file.toFileString()).exists());
	}

	/**
     * Don't show temp files
     */
    private boolean isTempFile(URI uri) {
        return uri != null && 
        		uri.isFile() &&
        	uri.toFileString().startsWith("~");
    }
    
    private void addToList(URI uri) {
        if(fMRU_List.contains(uri)) {
            fMRU_List.remove(uri);
            fMRU_List.add(0, uri);
        }
        else {
            fMRU_List.add(0, uri);
            while(fMRU_List.size() > MAX) {
                fMRU_List.remove(fMRU_List.size() - 1);
            }
        }
    }
    
    private void createMenuItems() {
        removeAll();
        
        for(URI file : fMRU_List) {
            add(new RecentFileAction(file));
        }
        
        add(new Separator());
        MRU_ClearAction clearAction = new MRU_ClearAction();
        clearAction.setEnabled(!fMRU_List.isEmpty());
        add(clearAction);
    }
    
    private void clearAll() {
        fMRU_List.clear();
        createMenuItems();
    }
    
    private void loadList() {
        for(int i = 0; i < MAX; i++) {
            String path = Preferences.STORE.getString(MRU_PREFS_KEY + i);
            if(StringUtils.isSet(path)) {
            	//System.out.println("adding to the list:"+path);
                URI file = URI.createURI(path);
                fMRU_List.add(file);
            }
        }
    }
    
    private int getCurrentMRUMax() {
        int max = Preferences.STORE.getInt(IPreferenceConstants.MRU_MAX);
        if(max < 3 || max > 15) {
            max = 6;
        }
        return max;
    }
    
    private void saveList() {
        // Clear
        for(int i = 0; i < 50; i++) {
            Preferences.STORE.setValue(MRU_PREFS_KEY + i, "");
        }
        
        // Save
        for(int i = 0; i < fMRU_List.size(); i++) {
        	//System.out.println("saving list item:"+fMRU_List.get(i).toString());
            Preferences.STORE.setValue(MRU_PREFS_KEY + i, fMRU_List.get(i).toString());
        }
    }
    
    @Override
    public void dispose() {
        super.dispose();
        IEditorModelManager.INSTANCE.removePropertyChangeListener(this);
        saveList();
    }
    
    private static String getShortPath(URI uri2) {
        String path = uri2.toString();
        final int maxLength = 38;
        if(path.length() > maxLength) {
            path = path.substring(0, maxLength - 3);
            path += "...";
        }
        return path;
    }
    
    private class RecentFileAction extends Action {
        URI uri;
        
        RecentFileAction(URI uri2) {
            this.uri = uri2;
            setText(getShortPath(uri2));
        }
        
        @Override
        public void run() {
            if(!IEditorModelManager.INSTANCE.isModelLoaded(uri)) {
                BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
                    public void run() {
                    	IArchimateModel openedmodel;
                        openedmodel = IEditorModelManager.INSTANCE.openModel(uri);
                        if (null == openedmodel) {//FIXME is the cleanup correct this way?
                            MessageDialog.openInformation(fWindow.getShell(),
                                    "Open File",
                                    "'" + uri + "' cannot be found");
                            
                            fMRU_List.remove(uri);
                            createMenuItems();                        	
                        }
                    }
                });
            }
        }
    }
    
    private class MRU_ClearAction extends Action {
        MRU_ClearAction() {
            setText("Clear Menu");
        }
        
        @Override
        public void run() {
            clearAll();
        }
    }

}
