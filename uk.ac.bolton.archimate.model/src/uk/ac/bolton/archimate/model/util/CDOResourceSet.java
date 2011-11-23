package uk.ac.bolton.archimate.model.util;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;


import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.impl.ArchimatePackage;

import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.net4j.CDONet4jUtil;
import org.eclipse.emf.cdo.net4j.CDOSessionConfiguration;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.transaction.CDOTransaction;

import org.eclipse.net4j.FactoriesProtocolProvider;
import org.eclipse.net4j.Net4jUtil;
import org.eclipse.net4j.buffer.IBufferProvider;
import org.eclipse.net4j.protocol.IProtocolProvider;
import org.eclipse.net4j.tcp.ITCPSelector;
import org.eclipse.net4j.util.lifecycle.Lifecycle;
import org.eclipse.net4j.util.lifecycle.LifecycleUtil;
import org.eclipse.net4j.util.om.OMPlatform;
import org.eclipse.net4j.util.om.log.PrintLogHandler;
import org.eclipse.net4j.util.om.trace.PrintTraceHandler;
import org.eclipse.emf.common.util.URI;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;



public class CDOResourceSet extends ArchimateResourceFactory implements Resource.Factory {
    // Enable logging and tracing

	static void initCDO () {
	    OMPlatform.INSTANCE.setDebugging(false);
	    OMPlatform.INSTANCE.addLogHandler(PrintLogHandler.CONSOLE);
	    OMPlatform.INSTANCE.addTraceHandler(PrintTraceHandler.CONSOLE);

	    // Prepare receiveExecutor
	    final ThreadGroup threadGroup = new ThreadGroup("net4j"); //$NON-NLS-1$
	    receiveExecutor = Executors.newCachedThreadPool(new ThreadFactory()
	    {
	      public Thread newThread(Runnable r)
	      {
	        Thread thread = new Thread(threadGroup, r);
	        thread.setDaemon(true);
	        return thread;
	      }
	    });

	    // Prepare bufferProvider
	   bufferProvider = Net4jUtil.createBufferPool();
	    LifecycleUtil.activate(bufferProvider);

	    protocolProvider = new FactoriesProtocolProvider(
	        new org.eclipse.emf.internal.cdo.net4j.protocol.CDOClientProtocolFactory());

	    // Prepare selector
	    selector = new org.eclipse.net4j.internal.tcp.TCPSelector();
	    ((Lifecycle) selector).activate();
	}

	private static IBufferProvider bufferProvider;
	private static ExecutorService receiveExecutor;
	private static IProtocolProvider protocolProvider;
	private static ITCPSelector selector;

	private CDOTransaction transaction;
	private CDOSession session;
	private Lifecycle connector;
	
	private void prepareConnector(String host,Integer port, String reponame) {
	    // Prepare connector
	    org.eclipse.net4j.internal.tcp.TCPClientConnector connector = new org.eclipse.net4j.internal.tcp.TCPClientConnector();
	    connector.getConfig().setBufferProvider(bufferProvider);
	    connector.getConfig().setReceiveExecutor(receiveExecutor);
	    connector.getConfig().setProtocolProvider(protocolProvider);
	    connector.getConfig().setNegotiator(null);
	    connector.setSelector(selector);
	    connector.setHost(host);
	    connector.setPort(port);
	    connector.activate();

	    // Create configuration
	    CDOSessionConfiguration configuration = CDONet4jUtil.createSessionConfiguration();
	    configuration.setConnector(connector);
	    configuration.setRepositoryName(reponame);

	    // Open session
	    session = configuration.openSession();
	    session.getPackageRegistry().putEPackage(ArchimatePackage.eINSTANCE);

	    // Open transaction
	    transaction = session.openTransaction();

	}
	
	public CDOResource getOrCreateResource(String path) {
		
	    // Get or create resource
	    CDOResource resource = transaction.getOrCreateResource("/path/to/my/resource"); //$NON-NLS-1$

	    // Work with the resource and commit the transaction
	    IArchimateModel object = null;//FIXME
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

	public static CDOResource getOrCreateResourceByURI(URI uri) {
		// url for CDO is cdo:<reponame>/</path/to/resource>
		System.out.println("uri="+uri);
		if(!uri.scheme().equals("cdo")) {
			throw new Error("sceme must be cdo");
		}
		
		String host = uri.host();
		if (host == null || host.equals("")) {
			host = "localhost";
		}
		String port = uri.port();
		if (port == null) {
			port = "2036";
		}
		String path = uri.path();
		if(path == null || path.equals("")) {
			throw new Error("CDO resource path is empty");
		}
		System.out.println("scheme="+uri.scheme()+", host="+host+", port="+port+", path="+path);
		return null;
	}

	@Override
	public Resource createResource(URI uri) {
		// TODO Auto-generated method stub
		return null;
	}
}
