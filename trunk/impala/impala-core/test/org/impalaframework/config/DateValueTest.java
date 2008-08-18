package org.impalaframework.config;

import java.sql.Date;
import java.util.Properties;

import junit.framework.TestCase;

public class DateValueTest extends TestCase {

	private Properties properties;
	private DatePropertyValue dateValue;
	private Date defaultValue;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		StaticPropertiesPropertySource source = new StaticPropertiesPropertySource();

		properties = new Properties();
		source.setProperties(properties);

		dateValue = new DatePropertyValue();
		dateValue.setName("intProperty");
		dateValue.setPropertiesSource(source);
		dateValue.setPattern("yyyy-MM-dd");
		
		defaultValue = Date.valueOf("1999-12-12");
	}

	public void testNoValueSet() {
		assertEquals(null, dateValue.getValue());
		dateValue.setDefaultValue(defaultValue);
		assertEquals(Date.valueOf("1999-12-12"), dateValue.getValue());

		properties.setProperty("intProperty", "1999-11-11");
		Date expected = Date.valueOf("1999-11-11");
		Date actual = new Date(dateValue.getValue().getTime());
		assertEquals(expected.getTime(), actual.getTime());
	}
}
