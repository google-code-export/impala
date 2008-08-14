package org.impalaframework.config;

import java.util.Properties;

import junit.framework.TestCase;

public class IntPropertyValueTest extends TestCase {

	private Properties properties;
	private IntPropertyValue value;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		StaticPropertiesPropertySource source = new StaticPropertiesPropertySource();

		properties = new Properties();
		source.setProperties(properties);

		value = new IntPropertyValue();
		value.setName("property1");
		value.setPropertiesSource(source);
	}

	public void testNoValueSet() {
		//properties.setProperty("property1", "value1");
		assertEquals(0, value.getValue());
		
		value.setDefaultValue(2);
		assertEquals(2, value.getValue());
	}
	
	public void testSetValue() {
		value.setDefaultValue(2);
		properties.setProperty("property1", "1");
		assertEquals(1, value.getValue());

		properties.setProperty("property1", "3");
		assertEquals(3, value.getValue());
		
		properties.clear();
		assertEquals(2, value.getValue());
	}
	
	public void testInvalid() {
		value.setDefaultValue(2);
		properties.setProperty("property1", "1");
		assertEquals(1, value.getValue());

		properties.setProperty("property1", "invalid");
		assertEquals(1, value.getValue());
		
		properties.clear();
		assertEquals(2, value.getValue());
	}
}
