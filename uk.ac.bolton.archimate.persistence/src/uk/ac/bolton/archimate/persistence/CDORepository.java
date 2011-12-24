package uk.ac.bolton.archimate.persistence;

import java.util.List;

import org.eclipse.emf.cdo.net4j.CDOSession;
import org.eclipse.emf.cdo.transaction.CDOTransaction;
import org.eclipse.emf.cdo.util.CommitException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.net4j.util.container.IPluginContainer;

import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.impl.ArchimateModel;

public class CDORepository extends ArchimateRepository {

	private CDOSession session;
	private CDOTransaction transaction;
	URI uri;
	
	public CDORepository(URI uri) {
		this.uri=uri;
		connect();
	}

	private void connect() {
		session = (CDOSession)IPluginContainer.INSTANCE.getElement("org.eclipse.emf.cdo.sessions", "cdo",uri.toString());
	    transaction = session.openTransaction(resourceSet);
	}
	@Override
	void addModel(IArchimateModel model) {
		// TODO Auto-generated method stub		
	}

	@Override
	IArchimateModel open(String modelname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	List<String> dir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	void save() {
		try {
			transaction.commit();
		} catch (CommitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	void clone(ArchimateRepository repo) {
		// TODO Auto-generated method stub
		
	}

}
