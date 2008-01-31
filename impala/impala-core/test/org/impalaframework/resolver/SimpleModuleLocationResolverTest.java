package org.impalaframework.resolver;

import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

public class SimpleModuleLocationResolverTest extends TestCase {

	private SimpleModuleLocationResolver resolver;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		resolver = new SimpleModuleLocationResolver();
		resolver.setModuleClassDirectory("bin");
		resolver.setModuleTestDirectory("testbin");
		resolver.setRootProjectString("project1, project2");
		resolver.setWorkspaceRoot("../");
	}

	public final void testClassLocations() throws IOException {
		List<Resource> classLocations = resolver.getApplicationModuleClassLocations("impala-interactive");
		System.out.println(classLocations);
		assertEquals(1, classLocations.size());
		Resource location = classLocations.get(0);
		assertTrue(location.exists());
		assertEquals("bin", location.getFilename());
		String absolutePath = location.getFile().getAbsolutePath();
		assertTrue(StringUtils.cleanPath(absolutePath).contains("impala-interactive/bin"));
	}

	public final void testTestClassLocations() throws IOException {
		List<Resource> classLocations = resolver.getModuleTestClassLocations("impala-interactive");
		System.out.println(classLocations);
		assertEquals(1, classLocations.size());
		Resource location = classLocations.get(0);
		assertEquals("testbin", location.getFilename());
		String absolutePath = location.getFile().getAbsolutePath();
		assertTrue(StringUtils.cleanPath(absolutePath).contains("impala-interactive/testbin"));
	}
	
	public void testname() throws Exception {
		List<String> rootProjects = resolver.getRootProjects();
		assertEquals(2, rootProjects.size());
		assertEquals("project1", rootProjects.get(0));
		assertEquals("project2", rootProjects.get(1));
	}

}
