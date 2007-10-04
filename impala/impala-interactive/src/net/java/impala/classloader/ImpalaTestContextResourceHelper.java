package net.java.impala.classloader;

import java.io.File;

import net.java.impala.location.ClassLocationResolver;
import net.java.impala.testrun.TestClassLoader;
import net.java.impala.util.PathUtils;

public class ImpalaTestContextResourceHelper extends DefaultContextResourceHelper implements TestContextResourceHelper {

	public ImpalaTestContextResourceHelper(ClassLocationResolver classLocationResolver) {
		super(classLocationResolver);
	}

	public ClassLoader getTestClassLoader(ClassLoader parentClassLoader, String name) {
		File[] locations = getClassLocationResolver().getTestClassLocations(
				PathUtils.getCurrentDirectoryName());
		
		TestClassLoader cl = new TestClassLoader(parentClassLoader, locations, name);
		return cl;
	}

}