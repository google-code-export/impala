package org.impalaframework.resolver;

import java.util.List;

import org.impalaframework.exception.ConfigurationException;
import org.springframework.core.io.Resource;

import junit.framework.TestCase;

public class JarModuleLocationResolverTest extends TestCase {

	private JarModuleLocationResolver jarResolver;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		jarResolver = new JarModuleLocationResolver();
		System.clearProperty(LocationConstants.WORKSPACE_ROOT_PROPERTY);
		System.clearProperty(LocationConstants.ROOT_PROJECTS_PROPERTY);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		System.clearProperty(LocationConstants.WORKSPACE_ROOT_PROPERTY);
		System.clearProperty(LocationConstants.ROOT_PROJECTS_PROPERTY);
	}
	
	public final void testGetRootDirectory() {
		try {
			jarResolver.getRootDirectory();
			fail();
		}
		catch (ConfigurationException e) {
			assertEquals("Workspace root not set. Have you set the property 'workspace.root'?", e.getMessage());
		}
	}
	
	public void testGetApplicationModuleClassLocations() throws Exception {
		System.setProperty(LocationConstants.WORKSPACE_ROOT_PROPERTY, "../impala-core/files");
		jarResolver.init();
		List<Resource> locations = jarResolver.getApplicationModuleClassLocations("MyTestClass");
		assertEquals(1, locations.size());
		assertNotNull(locations.get(0).getInputStream());
	}

}
