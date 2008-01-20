package org.impalaframework.testrun;

import junit.framework.TestCase;

import org.impalaframework.resolver.LocationConstants;

public class ManualInteractiveTestRunnerTest extends TestCase {
	
	public void setUp() {
		System.setProperty(LocationConstants.ROOT_PROJECTS_PROPERTY, "impala");
	}

	public final void testExecute() {
		new InteractiveTestRunner().start(Test1.class);
	}

	
}
