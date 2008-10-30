package org.impalaframework.classloader;

import java.io.File;
import java.net.URL;

import org.impalaframework.exception.InvalidStateException;
import org.springframework.util.ClassUtils;

import junit.framework.TestCase;

public class BaseModuleClassLoaderFactoryTest extends TestCase {
	
	ClassLoader cl1 = new ModuleClassLoader(new File[0]);
	ClassLoader cl2 = new ModuleClassLoader(new URL[0]);
	private BaseModuleClassLoaderFactory factory;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		factory = factory();
	}

	public void testFiless() throws Exception {
		assertSame(cl1, factory.newClassLoader(null, new File[0]));
	}
	
	public void testURLs() throws Exception {
		assertSame(cl2, factory.newClassLoader(null, new URL[0]));
	}
	
	public void testInvalidArg() {
		try {
			factory.newClassLoader(ClassUtils.getDefaultClassLoader(), "invalid");
			fail();
		} catch (InvalidStateException e) {
			assertEquals("'data' must be instance of File[] or URL[]. Actual type: java.lang.String", e.getMessage());
		}
	}

	private BaseModuleClassLoaderFactory factory() {
		BaseModuleClassLoaderFactory factory = new BaseModuleClassLoaderFactory() {

			@Override
			public ClassLoader newClassLoader(ClassLoader parent, File[] files) {
				return cl1;
			}

			@Override
			public ClassLoader newClassLoader(ClassLoader parent, URL[] urls) {
				return cl2;
			} 	
		};
		return factory;
	}

}
