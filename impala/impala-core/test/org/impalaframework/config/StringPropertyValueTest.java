package org.impalaframework.config;

import junit.framework.TestCase;

public class StringPropertyValueTest extends TestCase {
	public void testDefaultValue() throws Exception {
		StaticPropertiesPropertySource source = new StaticPropertiesPropertySource();

		StringPropertyValue value = new StringPropertyValue();
		value.setPropertiesSource(source);
		value.setName("property1");
		value.setDefaultValue("myDefault");

		assertEquals("myDefault", value.getRawValue());
	}
}
