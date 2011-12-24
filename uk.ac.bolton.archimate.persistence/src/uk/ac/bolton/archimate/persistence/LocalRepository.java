package uk.ac.bolton.archimate.persistence;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import uk.ac.bolton.archimate.editor.Logger;
import uk.ac.bolton.archimate.editor.model.impl.ModelVersionChecker;
import uk.ac.bolton.archimate.editor.model.impl.ModelVersionChecker.IncompatibleModelVersionException;
import uk.ac.bolton.archimate.editor.model.impl.ModelVersionChecker.LaterModelVersionException;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.impl.ArchimateModel;
import uk.ac.bolton.archimate.model.util.ArchimateResource;
import uk.ac.bolton.archimate.model.util.ArchimateResourceFactory;

public class LocalRepository extends ArchimateRepository {

	private File repodir;
    private Map<Object, Object> options;

	public LocalRepository(URI uri) {
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new ArchimateResourceFactory());
		repodir = new File(uri.toFileString());
		if( (repodir.isDirectory()) || ((!repodir.exists()) && repodir.mkdirs())) {
			//we have the repo dir now.
			
		} else {
			//FIXME error branch
		}
	}

	@Override
	void addModel(IArchimateModel model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	IArchimateModel open(String modelname) {
		File resourcepath=new File(repodir,modelname);
		URI uri = URI.createFileURI(resourcepath.getAbsolutePath());
		Resource resource = createResource(resourcepath);
        try {
            resource.load(null);
        }
        catch(IOException ex) {
            // Error occured loading model. Was it a disaster?
        	//System.out.println("exception:"+ex);
        	//ex.printStackTrace();
            try {
                ModelVersionChecker.checkErrors(resource);
            }
            // Incompatible
            catch(IncompatibleModelVersionException exception) {
                Logger.logError("Error opening model", exception);
                MessageDialog.openError(Display.getCurrent().getActiveShell(),
                        "Error opening model",
                        "Cannot open '" + uri +  "'. This version is incompatible. Please update to the latest version of Archi.");
                return null;
            }
            // Wrong file type
            catch(Exception ex2) {
            	System.out.println("exception:"+ex2);
            	ex2.printStackTrace();
                MessageDialog.openError(Display.getCurrent().getActiveShell(),
                        "Error opening model",
                        "Cannot open '" + uri +  "'.");
                return null;
            }
        }
        
        // Once loaded - Check version compatibility
        try {
            ModelVersionChecker.checkVersion(resource);
        }
        catch(LaterModelVersionException exception) {
            boolean answer = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(),
                    "Opening model",
                    "'" + uri +  "' is a later version model. Are you sure you want to continue opening it?");
            if(!answer) {
                return null;
            }
        }

        IArchimateModel model = (IArchimateModel)resource.getContents().get(0);
        model.setFile(uri);
        model.setDefaults();
        return model;
	}

	@Override
	List<String> dir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	void clone(ArchimateRepository repo) {
		// TODO Auto-generated method stub
		
	}
	
    private Resource createResource(File file) {
     	ArchimateResource result = new ArchimateResource(URI.createFileURI(file.getAbsolutePath()));
        result.getDefaultLoadOptions().putAll(getOptions());
        result.getDefaultSaveOptions().putAll(getOptions());
        return result;    		
    }
    /**
     * Add customisation to serialsiation
     * See Chapter 15.3.5 of the EMF book, 2nd edition
     * @return
     */
    private Map<Object, Object> getOptions() {
        if(options == null) {
            options = new HashMap<Object, Object>();
            
            // UTF-8
            options.put(XMLResource.OPTION_ENCODING, "UTF-8");
            
            // A Map to map Ecore features and classes to elememt names and types
            ExtendedMetaData ext = new BasicExtendedMetaData(ExtendedMetaData.ANNOTATION_URI, 
                    EPackage.Registry.INSTANCE, new HashMap<EModelElement, EAnnotation>()) {
                
                @Override
                public EClassifier getType(EPackage ePackage, String name) {
                    /*
                     * Backwards compatibility for the old "DiagramModel" type
                     */
                    if("DiagramModel".equals(name)) {
                        return IArchimatePackage.Literals.ARCHIMATE_DIAGRAM_MODEL;
                    }
                    return super.getType(ePackage, name);
                }
            };

            // Prevents the root-level element from being namespace qualified. 
            //ext.setQualified(IArchimatePackage.eINSTANCE, false);

            // "ArchimateModel" becomes "model"
            ext.setName(IArchimatePackage.Literals.ARCHIMATE_MODEL, "model");
            
            // The "folders" list element becomes "folder"
            ext.setFeatureKind(IArchimatePackage.Literals.FOLDER_CONTAINER__FOLDERS, ExtendedMetaData.ELEMENT_FEATURE); // have to do this explicitly
            ext.setName(IArchimatePackage.Literals.FOLDER_CONTAINER__FOLDERS, "folder");

            // The "elements" list element becomes "element"
            ext.setFeatureKind(IArchimatePackage.Literals.FOLDER__ELEMENTS, ExtendedMetaData.ELEMENT_FEATURE); // have to do this explicitly
            ext.setName(IArchimatePackage.Literals.FOLDER__ELEMENTS, "element");
            
            // The "children" list element becomes "child"
            ext.setFeatureKind(IArchimatePackage.Literals.DIAGRAM_MODEL_CONTAINER__CHILDREN, ExtendedMetaData.ELEMENT_FEATURE); // have to do this explicitly
            ext.setName(IArchimatePackage.Literals.DIAGRAM_MODEL_CONTAINER__CHILDREN, "child");

            // The "properties" list element becomes "property"
            ext.setFeatureKind(IArchimatePackage.Literals.PROPERTIES__PROPERTIES, ExtendedMetaData.ELEMENT_FEATURE); // have to do this explicitly
            ext.setName(IArchimatePackage.Literals.PROPERTIES__PROPERTIES, "property");
            
            // The "sourceConnections" list element becomes "sourceConnection"
            ext.setFeatureKind(IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__SOURCE_CONNECTIONS, ExtendedMetaData.ELEMENT_FEATURE); // have to do this explicitly
            ext.setName(IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__SOURCE_CONNECTIONS, "sourceConnection");
            
            // The "bendpoints" list element becomes "bendpoint"
            ext.setFeatureKind(IArchimatePackage.Literals.DIAGRAM_MODEL_CONNECTION__BENDPOINTS, ExtendedMetaData.ELEMENT_FEATURE); // have to do this explicitly
            ext.setName(IArchimatePackage.Literals.DIAGRAM_MODEL_CONNECTION__BENDPOINTS, "bendpoint");

            // The "DiagramModelArchimateConnection" type becomes "Connection"
            ext.setName(IArchimatePackage.Literals.DIAGRAM_MODEL_ARCHIMATE_CONNECTION, "Connection");

            // The "DiagramModelArchimateObject" type becomes "DiagramObject"
            ext.setName(IArchimatePackage.Literals.DIAGRAM_MODEL_ARCHIMATE_OBJECT, "DiagramObject");

            // The "documentation" attribute becomes an element
            ext.setFeatureKind(IArchimatePackage.Literals.DOCUMENTABLE__DOCUMENTATION, ExtendedMetaData.ELEMENT_FEATURE);
            
            // The "DiagramModelNote" type becomes "Note"
            ext.setName(IArchimatePackage.Literals.DIAGRAM_MODEL_NOTE, "Note");

            // The "DiagramModelGroup" type becomes "Group"
            ext.setName(IArchimatePackage.Literals.DIAGRAM_MODEL_GROUP, "Group");

            // The TextContent "content" attribute becomes an element
            ext.setFeatureKind(IArchimatePackage.Literals.TEXT_CONTENT__CONTENT, ExtendedMetaData.ELEMENT_FEATURE); 
            
            // The DiagramModelReference "referencedModel" becomes "model"
            ext.setFeatureKind(IArchimatePackage.Literals.DIAGRAM_MODEL_REFERENCE__REFERENCED_MODEL, ExtendedMetaData.ATTRIBUTE_FEATURE); // have to do this explicitly
            ext.setName(IArchimatePackage.Literals.DIAGRAM_MODEL_REFERENCE__REFERENCED_MODEL, "model");
            
            /*
             * Alternative method, but no good for saving as the element will also be saved as "DiagramModel"
             * DiagramModel is now abstract so:
             * DiagramModel becomes "AbstractDiagramModel" and
             * ArchimateDiagramModel is now "DiagramModel" for backwards compatibility
             */
            //ext.setName(IArchimatePackage.Literals.DIAGRAM_MODEL, "AbstractDiagramModel");
            //ext.setName(IArchimatePackage.Literals.ARCHIMATE_DIAGRAM_MODEL, "DiagramModel");
            
            options.put(XMLResource.OPTION_EXTENDED_META_DATA, ext);
        }

        return options;
    }

}
