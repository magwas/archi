package uk.ac.bolton.archimate.persistence;

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.impl.ArchimateModel;

public abstract class ArchimateRepository {
	ResourceSet resourceSet = new ResourceSetImpl();
	/*
	 * Open or create the repo referred to by the uri
	 */
	static ArchimateRepository createOrOpenRepo(URI uri) {
		if(uri.isFile()) {
			return new LocalRepository(uri);
		} else {
			//we assume that if it is not a file, then it is CDO
			return new CDORepository(uri);
		}
	}
	
	/*
	 * Adds a model to the repository
	 */
	abstract void addModel(IArchimateModel model);
	
	/*
	 * opens a model in the repository
	 */
	abstract IArchimateModel open(String modelname);
	
	/*
	 * lists the models in the repository
	 */
	abstract List<String> dir();
	
	/*
	 * Saves the contents of the repository
	 */
	abstract void save();
	
	/*
	 * clones a model from the repository into another repository (in same name)
	 */
	abstract void clone(ArchimateRepository repo);
}
