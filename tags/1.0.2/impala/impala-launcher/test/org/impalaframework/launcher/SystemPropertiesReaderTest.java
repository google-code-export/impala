package org.impalaframework.launcher;

import junit.framework.TestCase;

public class SystemPropertiesReaderTest extends TestCase {

	private SystemPropertiesReader reader;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.clearProperty("prop1");
		System.clearProperty("prop2");
		System.clearProperty(SystemPropertiesReader.SYSPROP_RESOURCE_NAME);
		reader = new SystemPropertiesReader(this.getClass().getClassLoader());
	}
	
	public void testNotSet() throws Exception {
		reader.readSystemProperties();
		assertNull(System.getProperty("prop1"));
	}
	
	public void testPropertyResourceSet() throws Exception {
		System.setProperty(SystemPropertiesReader.SYSPROP_RESOURCE_NAME, "propsfile.properties");
		reader.readSystemProperties();
		assertEquals("value1", System.getProperty("prop1"));
		assertEquals("value2", System.getProperty("prop2"));
	}
	
	public void testOverrides() throws Exception {
		System.setProperty("prop1", "overridden value");
		System.setProperty(SystemPropertiesReader.SYSPROP_RESOURCE_NAME, "propsfile.properties");
		reader.readSystemProperties();
		assertEquals("overridden value", System.getProperty("prop1"));
		assertEquals("value2", System.getProperty("prop2"));
	}
	
	public void testNonPropertyResourceSet() throws Exception {
		System.setProperty(SystemPropertiesReader.SYSPROP_RESOURCE_NAME, "textfile.txt");
		reader.readSystemProperties();
		assertNull(System.getProperty("prop1"));
	}
	
	public void testMissingResourceSet() throws Exception {
		System.setProperty(SystemPropertiesReader.SYSPROP_RESOURCE_NAME, "missingresource.properties");
		reader.readSystemProperties();
		assertNull(System.getProperty("prop1"));
	}
}
