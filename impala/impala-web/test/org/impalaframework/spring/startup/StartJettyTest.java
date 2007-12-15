package org.impalaframework.spring.startup;

import java.io.File;

import junit.framework.TestCase;

public class StartJettyTest extends TestCase {

	public void testInvalidPort() {
		try {
			StartJetty.getPort("notanumber");
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Invalid port: notanumber", e.getMessage());
		}
	}
	
	public void testValidPort() {
		assertEquals(1000, StartJetty.getPort("1000"));
	}
	
	public void testFileNotExist() {
		try {
			StartJetty.getContextLocation("a_file_does_not_exist");
			fail();
		}
		catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			assertTrue(e.getMessage().contains("Invalid context directory"));
		}
	}
	
	public void testValidPath() {
		File contextLocation = StartJetty.getContextLocation(System.getProperty("java.io.tmpdir"));
		assertTrue(contextLocation.exists());
	}
	
	public void testContext() {
		assertEquals("/path",StartJetty.getContextPath("path"));
		assertEquals("/path",StartJetty.getContextPath("/path"));
	}

}
