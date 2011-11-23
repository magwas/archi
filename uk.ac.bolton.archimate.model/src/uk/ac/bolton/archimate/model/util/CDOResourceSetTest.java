package uk.ac.bolton.archimate.model.util;

import org.eclipse.emf.common.util.URI;
import org.junit.Test;

public class CDOResourceSetTest extends CDOResourceSet {

	@Test(expected=Error.class)
	public void testnocdo() {
		getOrCreateResourceByURI(URI.createURI("cd://127.0.0.1:3126/hello"));
	}
	@Test(expected=Error.class)
	public void testnopath() {
		getOrCreateResourceByURI(URI.createURI("cdo://127.0.0.1:3126"));
	}
	@Test
	public void testcorrect() {
		getOrCreateResourceByURI(URI.createURI("cdo://127.0.0.1:3126/hello"));
	}
	@Test
	public void testnoport() {
		getOrCreateResourceByURI(URI.createURI("cdo://127.0.0.1/hello"));
	}
	@Test
	public void testnohost() {
		getOrCreateResourceByURI(URI.createURI("cdo:///hello"));
	}

}
