package net.java.impala.testrun.spring;

import net.java.impala.classloader.TestContextResourceHelper;
import net.java.impala.spring.util.DefaultApplicationContextLoader;

public class TestApplicationContextLoader extends DefaultApplicationContextLoader {

	public TestApplicationContextLoader(TestContextResourceHelper resourceHelper) {
		super(resourceHelper);
	}

	public ClassLoader getTestClassLoader(ClassLoader parentClassLoader, Class testClass) {
		TestContextResourceHelper contextResourceHelper = (TestContextResourceHelper) getContextResourceHelper();
		return contextResourceHelper.getTestClassLoader(parentClassLoader, testClass.getName());
	}
	
}
