package uk.ac.bolton.archimate.model.util;


import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;


import uk.ac.bolton.archimate.model.impl.ArchimatePackage;

import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.net4j.CDONet4jUtil;
import org.eclipse.emf.cdo.net4j.CDOSessionConfiguration;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.transaction.CDOTransaction;
import org.eclipse.emf.cdo.view.CDOAdapterPolicy;

import org.eclipse.net4j.FactoriesProtocolProvider;
import org.eclipse.net4j.Net4jUtil;
import org.eclipse.net4j.buffer.IBufferProvider;
import org.eclipse.net4j.connector.IConnector;
import org.eclipse.net4j.protocol.IProtocolProvider;
import org.eclipse.net4j.tcp.ITCPSelector;
import org.eclipse.net4j.tcp.TCPUtil;
import org.eclipse.net4j.util.container.ContainerUtil;
import org.eclipse.net4j.util.container.IManagedContainer;
import org.eclipse.net4j.util.lifecycle.Lifecycle;
import org.eclipse.net4j.util.lifecycle.LifecycleUtil;
import org.eclipse.net4j.util.om.OMPlatform;
import org.eclipse.net4j.util.om.log.PrintLogHandler;
import org.eclipse.net4j.util.om.trace.PrintTraceHandler;
import org.eclipse.emf.common.util.URI;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;



public class CDOResourceSet extends ResourceSetImpl implements ResourceSet {
    private IManagedContainer container;
    static CDOResourceSet INSTANCE=null;

	private CDOResourceSet() {
		super();
	    OMPlatform.INSTANCE.setDebugging(true);
	    OMPlatform.INSTANCE.addLogHandler(PrintLogHandler.CONSOLE);
	    OMPlatform.INSTANCE.addTraceHandler(PrintTraceHandler.CONSOLE);
	    uriResourceMap = new HashMap<URI, Resource>();

	    // Prepare container
	    container = ContainerUtil.createContainer();
	    Net4jUtil.prepareContainer(container); // Register Net4j factories
	    TCPUtil.prepareContainer(container); // Register TCP factories
	    CDONet4jUtil.prepareContainer(container); // Register CDO factories
	    container.activate();
	}

	public static CDOResourceSet getResourceSet() {
		if(null == INSTANCE) {
			INSTANCE = new CDOResourceSet();
		}
		return INSTANCE;
	}
	
	public CDOResource createResource(URI uri) {
		// url for CDO is cdo:<reponame>/</path/to/resource>
		System.out.println("uri="+uri);
		if(!uri.scheme().equals("cdo")) {
			throw new Error("sceme must be cdo");
		}
		
		String host = uri.host();
		if (host == null || host.equals("")) {
			host = "localhost";
		}
		String portstring = uri.port();
		int port;
		if (portstring == null) {
			port = 3126;
		} else {
			port = new Integer(portstring);
		}
		
		if(uri.segmentCount()<2) {
			throw new Error("CDO resource path has no repo name and resource name");
		}
		String reponame = uri.segment(0);
		String resourcepath = "";
		for (String s : uri.segmentsList().subList(1, uri.segmentCount())) {
			resourcepath += "/" + s;
		}
		System.out.println("scheme="+uri.scheme()+", host="+host+", port="+
				port+"reponame="+reponame+" resourcepath="+resourcepath+"segmentslist="+uri.segmentsList());
		
	    // Create connector
	    IConnector connector = TCPUtil.getConnector(container, host + ":" + port); //$NON-NLS-1$

	    // Create configuration
	    CDOSessionConfiguration configuration = CDONet4jUtil.createSessionConfiguration();
	    configuration.setConnector(connector);
	    configuration.setRepositoryName(reponame); //$NON-NLS-1$

	    // Open session
	    CDOSession session = configuration.openSession();
	    session.getPackageRegistry().putEPackage(ArchimatePackage.eINSTANCE);


	    // Open transaction
	    CDOTransaction transaction = session.openTransaction();
	    transaction.options().addChangeSubscriptionPolicy(CDOAdapterPolicy.ALL);
	    CDOResource res = transaction.getOrCreateResource(resourcepath);
	    System.out.println("resource="+res); //org.eclipse.net4j.util.container.FactoryNotFoundException
	    uriResourceMap.put(uri,res);
	    return res;
	}

	public Resource getOrCreateResource(URI uri) {
		if (uriResourceMap.containsKey(uri)) {
			return uriResourceMap.get(uri);
		}
		ResourceSet resourceSet = new CDOResourceSet();
		return resourceSet.createResource(uri);
	}


	/*
	public CDOResource dummy(String path) {
		
	    // Get or create resource
; //$NON-NLS-1$

	    // Work with the resource and commit the transaction
	    IArchimateModel object = null;
	    System.out.println("adding "+object);
	    boolean u = resource.getContents().add(object);
	    System.out.println("returned "+u+"state="+resource.cdoState());
	    transaction.commit();
	    System.out.println("state="+resource.cdoState());
	    EList<Diagnostic> ws = resource.getErrors();
	    for(Diagnostic w: ws) {
	    	System.out.println("WARNING: "+w);
	    }
	    EList<EObject> c = resource.getContents();
	    for(EObject o :c) {
	    	System.out.println("have "+o);
	    }

	    // Cleanup
	    session.close();
	    connector.deactivate();
		return resource;
	}
*/


}
