package org.impalaframework.command.interactive;

import org.impalaframework.resolver.LocationConstants;

import junit.framework.TestCase;

public class InteractiveCommandUtilsTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.clearProperty(LocationConstants.ROOT_PROJECTS_PROPERTY);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		System.clearProperty(LocationConstants.ROOT_PROJECTS_PROPERTY);
	}
	
	public final void testIsRootProject() {
		System.setProperty(LocationConstants.ROOT_PROJECTS_PROPERTY, "project1, project2");
		assertTrue(InteractiveCommandUtils.isRootProject("project1"));
		assertTrue(InteractiveCommandUtils.isRootProject("project2"));
		assertFalse(InteractiveCommandUtils.isRootProject("project3"));
	}

}
