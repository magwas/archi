/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.templates.impl.wizard;

import java.io.File;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;

import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.editor.ui.ImageFactory;
import uk.ac.bolton.archimate.editor.ui.UIUtils;
import uk.ac.bolton.archimate.editor.utils.StringUtils;
import uk.ac.bolton.archimate.model.FolderType;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.templates.impl.model.ArchimateTemplateManager;
import uk.ac.bolton.archimate.templates.model.TemplateManager;
import uk.ac.bolton.archimate.templates.wizard.ModelViewsTreeViewer;
import uk.ac.bolton.archimate.templates.wizard.TemplateUtils;


/**
 * Save Archimate Model As Template Wizard Page
 * 
 * @author Phillip Beauvoir
 */
public class SaveArchimateModelAsTemplateWizardPage extends WizardPage {

    public static String HELPID = "uk.ac.bolton.archimate.help.SaveArchimateModelAsTemplateWizardPage"; //$NON-NLS-1$

    private IArchimateModel fModel;

    private Text fFileTextField;
    private Text fNameTextField;
    private Text fDescriptionTextField;
    private ModelViewsTreeViewer fModelViewsTreeViewer;
    private Label fPreviewLabel;
    private Button fButtonIncludeThumbs;

    private TemplateManager fTemplateManager;
    
    static File CURRENT_FOLDER = new File(System.getProperty("user.home"));
    
    public SaveArchimateModelAsTemplateWizardPage(IArchimateModel model, TemplateManager templateManager) {
        super("SaveModelAsTemplateWizardPage");
        setTitle("Save Model As Template");
        setDescription("Provide the Template's file location, name, description and key thumbnail.");
        setImageDescriptor(IArchimateImages.ImageFactory.getImageDescriptor(ImageFactory.ECLIPSE_IMAGE_NEW_WIZARD));
        fModel = model;
        fTemplateManager = templateManager;
    }

    @Override
    public void createControl(Composite parent) {
        GridData gd;
        Label label;
        
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout());
        setControl(container);
        
        PlatformUI.getWorkbench().getHelpSystem().setHelp(container, HELPID);
        
        Group fileComposite = new Group(container, SWT.NULL);
        fileComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        GridLayout layout = new GridLayout(3, false);
        fileComposite.setLayout(layout);
        
        label = new Label(fileComposite, SWT.NULL);
        label.setText("File:");
        
        fFileTextField = new Text(fileComposite, SWT.BORDER | SWT.SINGLE);
        fFileTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        File newFile = new File(CURRENT_FOLDER, "New Template" + ArchimateTemplateManager.ARCHIMATE_TEMPLATE_FILE_EXTENSION);
        fFileTextField.setText(newFile.getPath());
        // Single text control so strip CRLFs
        UIUtils.conformSingleTextControl(fFileTextField);
        fFileTextField.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                validateFields();
            }
        });
        
        Button fileButton = new Button(fileComposite, SWT.PUSH);
        fileButton.setText("Choose...");
        fileButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                File file = chooseFile();
                if(file != null) {
                    fFileTextField.setText(file.getPath());
                    CURRENT_FOLDER = file.getParentFile();
                }
            }
        });
        
        Group fieldGroup = new Group(container, SWT.NULL);
        fieldGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        layout = new GridLayout(2, false);
        fieldGroup.setLayout(layout);
        
        label = new Label(fieldGroup, SWT.NULL);
        label.setText("Name:");

        fNameTextField = new Text(fieldGroup, SWT.BORDER | SWT.SINGLE);
        fNameTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        if(StringUtils.isSet(fModel.getName())) {
            fNameTextField.setText(fModel.getName());
        }
        // Single text control so strip CRLFs
        UIUtils.conformSingleTextControl(fNameTextField);
        fNameTextField.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                validateFields();
            }
        });
        
        label = new Label(fieldGroup, SWT.NULL);
        label.setText("Description:");
        gd = new GridData(SWT.NULL, SWT.TOP, false, false);
        label.setLayoutData(gd);
        
        fDescriptionTextField = new Text(fieldGroup, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = 120;
        fDescriptionTextField.setLayoutData(gd);
        if(StringUtils.isSet(fModel.getDocumentation())) {
            fDescriptionTextField.setText(fModel.getDocumentation());
        }
        
        // Thumbnails
        boolean thumbsEnabled = !fModel.getDiagramModels().isEmpty();
        
        Group thumbsGroup = new Group(container, SWT.NULL);
        thumbsGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        layout = new GridLayout();
        thumbsGroup.setLayout(layout);
        
        fButtonIncludeThumbs = new Button(thumbsGroup, SWT.CHECK);
        fButtonIncludeThumbs.setText("Include thumbnails");
        fButtonIncludeThumbs.setSelection(thumbsEnabled);
        fButtonIncludeThumbs.setEnabled(thumbsEnabled);
        fButtonIncludeThumbs.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                fModelViewsTreeViewer.getControl().setEnabled(fButtonIncludeThumbs.getSelection());
                fPreviewLabel.setEnabled(fButtonIncludeThumbs.getSelection());
            }
        });
        
        label = new Label(thumbsGroup, SWT.NULL);
        label.setText("Key thumbnail:");
        label.setEnabled(thumbsEnabled);

        Composite thumbContainer = new Composite(thumbsGroup, SWT.NULL);
        thumbContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
        layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        thumbContainer.setLayout(layout);
        
        fModelViewsTreeViewer = new ModelViewsTreeViewer(thumbContainer, SWT.NONE);
        fModelViewsTreeViewer.setInput(fModel.getFolder(FolderType.DIAGRAMS));
        gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = 120;
        //gd.widthHint = 140;
        fModelViewsTreeViewer.getControl().setLayoutData(gd);
        fModelViewsTreeViewer.getControl().setEnabled(thumbsEnabled);
        
        fPreviewLabel = new Label(thumbContainer, SWT.BORDER);
        gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = 120;
        gd.widthHint = 150;
        fPreviewLabel.setLayoutData(gd);
        
        // Dispose of the image here not in the main dispose() method because if the help system is showing then 
        // the TrayDialog is resized and this label is asked to relayout.
        fPreviewLabel.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                disposePreviewImage();
            }
        });
        
        fModelViewsTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                disposePreviewImage();

                Object o = ((IStructuredSelection)event.getSelection()).getFirstElement();
                if(o instanceof IDiagramModel) {
                    TemplateUtils.createThumbnailPreviewImage((IDiagramModel)o, fPreviewLabel);
                }
                else {
                    fPreviewLabel.setImage(null);
                }
            }
        });
        
        // Select first Template item on tree (on a thread so that thumbnail preview is right size)
        fModelViewsTreeViewer.expandAll();
        Display.getCurrent().asyncExec(new Runnable() {
            @Override
            public void run() {
                for(TreeItem item : fModelViewsTreeViewer.getTree().getItems()) {
                    Object o = item.getData();
                    if(o instanceof IDiagramModel) {
                        fModelViewsTreeViewer.setSelection(new StructuredSelection(o));
                        break;
                    }
                }
            }
        });
        
        validateFields();
    }
    
    /**
     * @return The File for the template
     */
    public String getFileName() {
        return fFileTextField.getText();
    }

    /**
     * @return The Name for the template
     */
    public String getTemplateName() {
        return fNameTextField.getText();
    }
    
    /**
     * @return The Name for the template
     */
    public String getTemplateDescription() {
        return fDescriptionTextField.getText();
    }
    
    public boolean includeThumbnails() {
        return fButtonIncludeThumbs.getSelection();
    }
    
    /**
     * @return The Selected Diagram Model for the key thumbnail
     */
    public IDiagramModel getSelectedDiagramModel() {
        Object o = ((IStructuredSelection)fModelViewsTreeViewer.getSelection()).getFirstElement();
        if(o instanceof IDiagramModel) {
            return (IDiagramModel)o;
        }
        return null;
    }
    
    private File chooseFile() {
        FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
        dialog.setText("Choose a file name");
        dialog.setFilterExtensions(new String[] { "*" + fTemplateManager.getTemplateFileExtension(), "*.*" } );
        String path = dialog.open();
        if(path != null) {
            // Only Windows adds the extension by default
            if(dialog.getFilterIndex() == 0 && !path.endsWith(ArchimateTemplateManager.ARCHIMATE_TEMPLATE_FILE_EXTENSION)) {
                path += ArchimateTemplateManager.ARCHIMATE_TEMPLATE_FILE_EXTENSION;
            }
            return new File(path);
        }
        return null;
    }
    
    private void validateFields() {
        String fileName = getFileName();
        if(!StringUtils.isSetAfterTrim(fileName)) {
            updateStatus("Provide a file name");
            return;
        }
        
        String name = getTemplateName();
        if(!StringUtils.isSetAfterTrim(name)) {
            updateStatus("Provide a template name");
            return;
        }
        
        updateStatus(null);
    }

    /**
     * Update the page status
     */
    private void updateStatus(String message) {
        setErrorMessage(message);
        setPageComplete(message == null);
    }
    
    private void disposePreviewImage() {
        if(fPreviewLabel != null && fPreviewLabel.getImage() != null && !fPreviewLabel.getImage().isDisposed()) {
            fPreviewLabel.getImage().dispose();
        }
    }
}
