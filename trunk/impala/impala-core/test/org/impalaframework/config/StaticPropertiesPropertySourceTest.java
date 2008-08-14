package org.impalaframework.config;

import java.util.Properties;

import junit.framework.TestCase;

public class StaticPropertiesPropertySourceTest extends TestCase {

	public void testSetProperties() {
		StaticPropertiesPropertySource source = new StaticPropertiesPropertySource();
		
		Properties properties = new Properties();
		properties.setProperty("property1", "value1");
		source.setProperties(properties);
		
		BasePropertyValue value = new BasePropertyValue();
		value.setPropertiesSource(source);
		value.setName("property1");
		
		assertEquals("value1", value.getRawValue());
	}
	
	public void testNullProperties() throws Exception {
		StaticPropertiesPropertySource source = new StaticPropertiesPropertySource();
		assertNull(source.getValue("property1"));
	}
	
	public void testProperties() throws Exception {
		BasePropertyValue value = new BasePropertyValue();
		try {
			value.getRawValue();
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("propertiesSource must be specified", e.getMessage());
		}
		
		StaticPropertiesPropertySource source = new StaticPropertiesPropertySource();
		value.setPropertiesSource(source);
		try {
			value.getRawValue();
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("name must be specified", e.getMessage());
		}
	}

}