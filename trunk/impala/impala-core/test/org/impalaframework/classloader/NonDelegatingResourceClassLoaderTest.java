package org.impalaframework.classloader;

import junit.framework.TestCase;

public class NonDelegatingResourceClassLoaderTest extends TestCase {

	public final void testGetResourceString() throws Exception {
		URLClassLoader c = ClassLoaderTestUtils.getLoader("files/classlocation1");
		assertEquals("Location1resource text", ClassLoaderTestUtils.readResource(c, "location1resource.txt"));
		assertNotNull(ClassLoaderTestUtils.readResource(c, "beanset.properties"));
		
		NonDelegatingResourceClassLoader nd = new NonDelegatingResourceClassLoader(c);
		assertEquals("Location1resource text", ClassLoaderTestUtils.readResource(nd, "location1resource.txt"));
		assertNull(nd.getResourceAsStream("beanset.properties"));
		
	}
	
}
