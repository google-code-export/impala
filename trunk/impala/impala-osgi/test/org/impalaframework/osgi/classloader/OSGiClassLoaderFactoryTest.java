package org.impalaframework.osgi.classloader;

import org.easymock.EasyMock;
import org.impalaframework.exception.InvalidStateException;
import org.osgi.framework.Bundle;
import org.springframework.osgi.util.BundleDelegatingClassLoader;
import org.springframework.util.ClassUtils;

import junit.framework.TestCase;

public class OSGiClassLoaderFactoryTest extends TestCase {

	private OsgiClassLoaderFactory factory;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		factory = new OsgiClassLoaderFactory();
	}
	
	public void testNewClassLoaderInvalid() {
		try {
			factory.newClassLoader(ClassUtils.getDefaultClassLoader(), "duffvalue");
			fail();
		} catch (InvalidStateException e) {
			assertEquals("'data' must be instance of Bundle. Actual type: java.lang.String", e.getMessage());
		}
	}
	
	public void testNewClassLoader() throws Exception {
		final ClassLoader classLoader = factory.newClassLoader(ClassUtils.getDefaultClassLoader(), EasyMock.createMock(Bundle.class));
		assertTrue(classLoader instanceof BundleDelegatingClassLoader);
	}

}
