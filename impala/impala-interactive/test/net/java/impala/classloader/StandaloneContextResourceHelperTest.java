package net.java.impala.classloader;

import junit.framework.TestCase;

public class StandaloneContextResourceHelperTest extends TestCase {

	public void testGetContextResourceHelper() {
		assertTrue(new StandaloneContextResourceHelperFactory().getContextResourceHelper() instanceof DefaultContextResourceHelper);
	}

}
