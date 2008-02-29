package org.impalaframework.classloader;

import java.io.File;
import java.net.URL;

import org.impalaframework.util.URLUtils;
import org.springframework.util.ClassUtils;

import junit.framework.TestCase;

public class ParentClassLoaderFactoryTest extends TestCase {

	private ParentClassLoaderFactory factory;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		factory = new ParentClassLoaderFactory();
	}
	
	public final void testNewClassLoaderClassLoaderFileArray() {
		ClassLoader newClassLoader = factory.newClassLoader(ClassUtils.getDefaultClassLoader(), new File[]{ new File(System.getProperty("java.io.tmpdir"))});
		assertTrue(newClassLoader instanceof ParentClassLoader);
		System.out.println(newClassLoader.toString());
	}

	public final void testNewClassLoaderClassLoaderURLArray() {
		File file = new File(System.getProperty("java.io.tmpdir"));
		File[] files = new File[]{ file};
		URL[] createUrls = URLUtils.createUrls(files);
		ClassLoader newClassLoader = factory.newClassLoader(ClassUtils.getDefaultClassLoader(), createUrls);
		assertTrue(newClassLoader instanceof ParentClassLoader);
		System.out.println(newClassLoader.toString());
	}

}
