package net.java.impala.testrun.spring;

import org.springframework.util.ClassUtils;

import net.java.impala.classloader.TestContextResourceHelper;
import net.java.impala.spring.util.DefaultApplicationContextLoader;
import net.java.impala.util.PathUtils;

public class TestApplicationContextLoader extends DefaultApplicationContextLoader {

	public TestApplicationContextLoader(TestContextResourceHelper resourceHelper) {
		super(resourceHelper);
	}

	public ClassLoader getTestClassLoader(ClassLoader parentClassLoader, Class testClass) {
		TestContextResourceHelper contextResourceHelper = (TestContextResourceHelper) getContextResourceHelper();
		return contextResourceHelper.getTestClassLoader(parentClassLoader, testClass.getName());
	}
	
	public ClassLoader newParentClassLoader() {
		ClassLoader contextClassLoader = ClassUtils.getDefaultClassLoader();
		return getContextResourceHelper().getParentClassLoader(contextClassLoader, PathUtils.getCurrentDirectoryName());
	}

}
