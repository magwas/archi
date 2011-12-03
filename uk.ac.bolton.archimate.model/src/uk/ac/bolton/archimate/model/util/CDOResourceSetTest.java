package uk.ac.bolton.archimate.model.util;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Test;
public class CDOResourceSetTest {

	private Resource getOrCreateResource(URI uri) {
		return CDOResourceSet.getResourceSet().getOrCreateResource(uri);
	}
	@Test(expected=Error.class)
	public void testnocdo() {
		getOrCreateResource(URI.createURI("cd://127.0.0.1:3126/repo1/hello"));
	}
	@Test(expected=Error.class)
	public void testnopath() {
		getOrCreateResource(URI.createURI("cdo://127.0.0.1:3126"));
	}
	@Test
	public void testcorrect() {
		getOrCreateResource(URI.createURI("cdo://127.0.0.1:3126/repo1/hello"));
	}
	@Test
	public void testnoport() {
		getOrCreateResource(URI.createURI("cdo://127.0.0.1/repo1/hello"));
	}
	@Test
	public void testnohost() {
		getOrCreateResource(URI.createURI("cdo:///repo1/hello"));
	}

}
