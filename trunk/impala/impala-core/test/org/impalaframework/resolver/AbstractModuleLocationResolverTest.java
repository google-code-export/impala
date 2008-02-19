package org.impalaframework.resolver;

import java.util.List;


import junit.framework.TestCase;

import org.impalaframework.exception.ConfigurationException;
import org.springframework.core.io.Resource;

public class AbstractModuleLocationResolverTest extends TestCase {

	private AbstractModuleLocationResolver resolver;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.clearProperty(LocationConstants.WORKSPACE_ROOT_PROPERTY);
		resolver = new TestModuleLocationResolver();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		System.clearProperty(LocationConstants.WORKSPACE_ROOT_PROPERTY);
	}
	
	public final void testGetRootDirectoryPath() {
		try {
			resolver.getRootDirectoryPath();
		}
		catch (ConfigurationException e) {
			assertEquals("Unable to determine application's root directory. Has the property 'workspace.root' been set?", e.getMessage());
		}
	}
	
	public void testGetRootDirectoryPathPresent() throws Exception {
		System.setProperty(LocationConstants.WORKSPACE_ROOT_PROPERTY, System.getProperty("java.io.tmpdir"));
		String rootDirectoryPath = resolver.getRootDirectoryPath();
		assertNotNull(rootDirectoryPath);
	}

}

class TestModuleLocationResolver extends AbstractModuleLocationResolver {

	@Override
	protected String getWorkspaceRoot() {
		return System.getProperty(LocationConstants.WORKSPACE_ROOT_PROPERTY);
	}

	public List<Resource> getApplicationModuleClassLocations(String moduleName) {
		return null;
	}

	public List<Resource> getModuleTestClassLocations(String moduleName) {
		return null;
	}

	public List<String> getRootProjects() {
		return null;
	}

}