package org.impalaframework.config;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import junit.framework.TestCase;

public class ExternalDynamicPropertySourceTest extends TestCase {

	private ExternalDynamicPropertySource source;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		source = new ExternalDynamicPropertySource();
		source.setFileName("reload/reloadable.properties");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		System.clearProperty("property.folder");
	}

	public void testGetLocations() {
		Resource[] locations = source.getLocations();
		assertEquals(1, locations.length);
		assertTrue(locations[0] instanceof ClassPathResource);
	}

	public void testGetLocationsWithNonExistFile() {
		System.setProperty("property.folder", "nonexist");
		Resource[] locations = source.getLocations();
		assertEquals(1, locations.length);
	}

	public void testGetLocationsWithValidFile() {
		System.setProperty("property.folder", "../impala-core/files");
		Resource[] locations = source.getLocations();
		assertEquals(2, locations.length);
		assertTrue(locations[1] instanceof FileSystemResource);
	}

}
