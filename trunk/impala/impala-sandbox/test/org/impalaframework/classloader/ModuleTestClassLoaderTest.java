package org.impalaframework.classloader;

import java.io.File;
import java.util.Map;

import junit.framework.TestCase;

public class ModuleTestClassLoaderTest extends TestCase {

	public void testLoadClassString() throws Exception {
		ModuleTestClassLoader tcl = new ModuleTestClassLoader(new File[] {new File("../impala-interactive/bin")}, "org.impalaframework.testrun.AJUnitTest");

		// check that this class loader loads the named class
		Class<?> cls = Class.forName("org.impalaframework.testrun.AJUnitTest", false, tcl);
		assertSame(cls.getClassLoader(), tcl);

		Map<String, Class<?>> loadedClasses = tcl.getLoadedClasses();
		assertEquals(1, loadedClasses.size());
		assertNotNull(loadedClasses.get("org.impalaframework.testrun.AJUnitTest"));

		// but not a String
		cls = Class.forName("java.lang.String", false, tcl);
		assertNotSame(cls.getClassLoader(), tcl);

		//here it's different from TestClassLoader - it attempts to load any class first from the
		//current project using this class loader
		cls = Class.forName("org.impalaframework.testrun.BaseTest", false, tcl);
		assertSame(cls.getClassLoader(), tcl);
		assertEquals(2, loadedClasses.size());
		assertNotNull(loadedClasses.get("org.impalaframework.testrun.AJUnitTest"));
	}

}
