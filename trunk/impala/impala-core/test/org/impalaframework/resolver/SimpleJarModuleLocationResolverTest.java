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
		jarResolver.setRootProjectString("impala-core");
		jarResolver.setWorkspaceRoot("../impala-core/files");
		System.setProperty(LocationConstants.WORKSPACE_ROOT_PROPERTY, "../impala-core/files");
		jarResolver.init();
	}
	
	public void testGetApplicationModuleClassLocations() throws Exception {
		List<Resource> locations = jarResolver.getApplicationModuleClassLocations("MyTestClass");
		assertEquals(1, locations.size());
		assertNotNull(locations.get(0).getInputStream());
		assertEquals("MyTestClass.jar", locations.get(0).getFilename());
	}
	
	public void testWithApplicationVersion() throws Exception {
		jarResolver.setApplicationVersion("1.0");
		List<Resource> locations = jarResolver.getApplicationModuleClassLocations("MyTestClass");
		assertEquals(1, locations.size());
		assertEquals("MyTestClass-1.0.jar", locations.get(0).getFilename());
	}

}
