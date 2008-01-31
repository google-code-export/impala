package org.impalaframework.resolver;

import java.util.List;

import junit.framework.TestCase;

import org.springframework.core.io.Resource;

public class SimpleJarModuleLocationResolverTest extends TestCase {

	private SimpleJarModuleLocationResolver jarResolver;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		jarResolver = new SimpleJarModuleLocationResolver();

		jarResolver.setModuleClassDirectory("bin");
		jarResolver.setModuleTestDirectory("bin");
		jarResolver.setRootProjectString("impala-core");
		jarResolver.setWorkspaceRoot("../impala-core/files");
	}
	
	public void testGetApplicationModuleClassLocations() throws Exception {
		System.setProperty(LocationConstants.WORKSPACE_ROOT_PROPERTY, "../impala-core/files");
		jarResolver.init();
		List<Resource> locations = jarResolver.getApplicationModuleClassLocations("MyTestClass");
		assertEquals(1, locations.size());
		assertNotNull(locations.get(0).getInputStream());
	}

}
