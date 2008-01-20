package org.impalaframework.testrun;

import junit.framework.TestCase;

import org.impalaframework.resolver.PropertyModuleLocationResolver;

public class ManualInteractiveTestRunnerTest extends TestCase {
	
	public void setUp() {
		System.setProperty(PropertyModuleLocationResolver.ROOT_PROJECTS_PROPERTY, "impala");
	}

	public final void testExecute() {
		new InteractiveTestRunner().start(Test1.class);
	}

	
}
