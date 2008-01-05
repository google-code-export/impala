package org.impalaframework.classloader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;


public class CompositeClassLoaderTest extends TestCase {
	
	@SuppressWarnings("unchecked")
	public void testInvalidCompositeConstructor() {
		try {
			new CompositeClassLoader(null);
			fail(IllegalArgumentException.class.getName());
		}
		catch (IllegalArgumentException e) {
		}

		try {
			new CompositeClassLoader(Collections.EMPTY_LIST);
			fail(IllegalArgumentException.class.getName());
		}
		catch (IllegalArgumentException e) {
		}
	}

	public void testNotTheSameParent() {

		// load the classes individually using the customClassLoader.
		CustomClassLoader location1Loader = ClassLoaderTestUtils.getLoader("files/classlocation1");
		File file = new File("files/classlocation2");
		CustomClassLoader location2Loader = new CustomClassLoader(location1Loader, new File[] { file });

		List<FileSystemClassLoader> list = new ArrayList<FileSystemClassLoader>();
		list.add(location1Loader);
		list.add(location2Loader);

		try {
			new CompositeClassLoader(list);
			fail(IllegalArgumentException.class.getName());
		}
		catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			assertTrue(e.getMessage().startsWith("All class loaders must share from the same parent."));
		}
	}

	public void testGetResources() throws Exception {
		CustomClassLoader location1Loader = ClassLoaderTestUtils.getLoader("../impala-core/files/classlocation1");
		String result1 = ClassLoaderTestUtils.readResource(location1Loader, "location1resource.txt");
		assertEquals("Location1resource text", result1);

		CustomClassLoader location2Loader = ClassLoaderTestUtils.getLoader("../impala-core/files/classlocation2");
		String result2 = ClassLoaderTestUtils.readResource(location2Loader, "location2resource.txt");
		assertEquals("Location2resource text", result2);
		
		List<FileSystemClassLoader> list = new ArrayList<FileSystemClassLoader>();
		list.add(location1Loader);
		list.add(location2Loader);
		
		CompositeClassLoader c = new CompositeClassLoader(list);
		assertEquals("Location1resource text", ClassLoaderTestUtils.readResource(c, "location1resource.txt"));
		assertEquals("Location2resource text", ClassLoaderTestUtils.readResource(c, "location2resource.txt"));
		assertEquals("Shared in location 1", ClassLoaderTestUtils.readResource(c, "resourcewithsharedname.txt"));
		//this is in classlocation1 as well as in the default class path
		assertEquals("log4j in location 1", ClassLoaderTestUtils.readResource(c, "log4j.properties"));
		assertNotNull(ClassLoaderTestUtils.readResource(c, "beanset.properties"));
	}
	
	public void testLoadClassesIndividually() throws Exception {

		// load the classes individually using the customClassLoader.
		CustomClassLoader location1Loader = ClassLoaderTestUtils.getLoader("../impala-core/files/classlocation1");
		loadAndVerify(location1Loader, "ClassLocation1Class");

		CustomClassLoader location2Loader = ClassLoaderTestUtils.getLoader("../impala-core/files/classlocation2");
		loadAndVerify(location2Loader, "ClassLocation2Class");

		List<FileSystemClassLoader> list = new ArrayList<FileSystemClassLoader>();
		list.add(location1Loader);
		list.add(location2Loader);
		CompositeClassLoader c = new CompositeClassLoader(list);

		// should be able to get either of the previously loaded classes
		loadAndVerify(c, "ClassLocation1Class");
		loadAndVerify(c, "ClassLocation2Class");
	}

	public void testLoadClassComposite() throws Exception {
		CustomClassLoader location1Loader = ClassLoaderTestUtils.getLoader("../impala-core/files/classlocation1");
		CustomClassLoader location2Loader = ClassLoaderTestUtils.getLoader("../impala-core/files/classlocation2");

		List<FileSystemClassLoader> list = new ArrayList<FileSystemClassLoader>();
		list.add(location1Loader);
		list.add(location2Loader);
		CompositeClassLoader c = new CompositeClassLoader(list);

		// Loaded here for the first time. Should be able to get either of the
		// classes
		loadAndVerify(c, "ClassLocation1Class");
		loadAndVerify(c, "ClassLocation2Class");
	}
	
	public void testAddClassLoader() throws Exception {
		CustomClassLoader location1Loader = ClassLoaderTestUtils.getLoader("../impala-core/files/classlocation1");
		CustomClassLoader location2Loader = ClassLoaderTestUtils.getLoader("../impala-core/files/classlocation2");

		List<FileSystemClassLoader> list = new ArrayList<FileSystemClassLoader>();
		list.add(location1Loader);
		CompositeClassLoader c = new CompositeClassLoader(list);
		loadAndVerify(c, "ClassLocation1Class");
		
		c.addClassLoader(location2Loader);

		// load the second set
		loadAndVerify(c, "ClassLocation2Class");
		
		assertTrue(c.removeClassLoader(location1Loader));
		
		try {
			loadAndVerify(c, "ClassLocation1Class");
			fail(ClassNotFoundException.class.getName());
		}
		catch (ClassNotFoundException e) {
		}
		
	}

	static void loadAndVerify(ClassLoader location1Loader, String className) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		Class<?> cl = location1Loader.loadClass(className);
		Object o = cl.newInstance();
		assertTrue(o instanceof CompositeInterface);
		CompositeInterface c = (CompositeInterface) o;
		assertNotNull(c.method());
	}

}
