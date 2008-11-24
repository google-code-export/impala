package org.impalaframework.module.definition;

import junit.framework.TestCase;

import org.impalaframework.constants.LocationConstants;

public class RootModuleDefinitionUtilsTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.clearProperty(LocationConstants.ROOT_PROJECT_PROPERTY);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		System.clearProperty(LocationConstants.ROOT_PROJECT_PROPERTY);
	}
	
	public void testGetProjectNameList() {
		System.setProperty(LocationConstants.ROOT_PROJECT_PROPERTY, "rootProject");
		assertEquals("rootProject", RootModuleDefinitionUtils.getRootProject());
	}

}
