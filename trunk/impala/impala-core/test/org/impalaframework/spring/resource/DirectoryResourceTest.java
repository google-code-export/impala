package org.impalaframework.spring.resource;

import java.io.File;

import org.springframework.core.io.FileSystemResource;

import junit.framework.TestCase;

public class DirectoryResourceTest extends TestCase {

	public void testDirectoryResource() throws Exception {
		FileSystemResource resource = new DirectoryResource("../impala-core/src/");
		assertTrue(resource.getURL().getFile().endsWith("/impala-core/src/"));
	}
	
	public void testNotDirectory() throws Exception {

		File file = new File("../impala-repository/test/junit-3.8.1.jar");
		try {
			new DirectoryResource(file);
		}
		catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			assertTrue(e.getMessage().contains("junit-3.8.1.jar' is not a directory"));
		}
		
		try {
			new DirectoryResource(file.getAbsolutePath());
		}
		catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			assertTrue(e.getMessage().contains("junit-3.8.1.jar' is not a directory"));
		}
	}

}
