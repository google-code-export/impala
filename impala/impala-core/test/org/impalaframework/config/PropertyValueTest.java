package org.impalaframework.config;

import java.util.Properties;

import junit.framework.TestCase;

public class PropertyValueTest extends TestCase {

	private Properties properties;
	private IntPropertyValue intValue;
	private LongPropertyValue longValue;
	private FloatPropertyValue floatValue;
	private DoublePropertyValue doubleValue;
	private BooleanPropertyValue booleanValue;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		StaticPropertiesPropertySource source = new StaticPropertiesPropertySource();

		properties = new Properties();
		source.setProperties(properties);

		intValue = new IntPropertyValue();
		intValue.setName("intProperty");
		intValue.setPropertiesSource(source);
		
		longValue = new LongPropertyValue();
		longValue.setName("longProperty");
		longValue.setPropertiesSource(source);
		
		floatValue = new FloatPropertyValue();
		floatValue.setName("floatProperty");
		floatValue.setPropertiesSource(source);
		
		doubleValue = new DoublePropertyValue();
		doubleValue.setName("doubleProperty");
		doubleValue.setPropertiesSource(source);
		
		booleanValue = new BooleanPropertyValue();
		booleanValue.setName("booleanProperty");
		booleanValue.setPropertiesSource(source);
	}

	public void testNoValueSet() {
		assertEquals(0, intValue.getValue());
		intValue.setDefaultValue(2);
		assertEquals(2, intValue.getValue());

		assertEquals(0L, longValue.getValue());
		longValue.setDefaultValue(2L);
		assertEquals(2L, longValue.getValue());
		
		assertEquals(0.0F, floatValue.getValue());
		floatValue.setDefaultValue(2);
		assertEquals(2.0F, floatValue.getValue());
		
		assertEquals(0.0, doubleValue.getValue());
		doubleValue.setDefaultValue(2);
		assertEquals(2.0, doubleValue.getValue());
	}
	
	public void testIntSetValue() {
		intValue.setDefaultValue(2);
		properties.setProperty("intProperty", "1");
		assertEquals(1, intValue.getValue());

		properties.setProperty("intProperty", "3");
		assertEquals(3, intValue.getValue());
		
		properties.clear();
		assertEquals(2, intValue.getValue());
	}
	
	public void testIntInvalid() {
		intValue.setDefaultValue(2);
		properties.setProperty("intProperty", "1");
		assertEquals(1, intValue.getValue());

		properties.setProperty("intProperty", "invalid");
		assertEquals(1, intValue.getValue());
		
		properties.clear();
		assertEquals(2, intValue.getValue());
	}
	
	public void testLongSetValue() {
		longValue.setDefaultValue(2);
		properties.setProperty("longProperty", "1");
		assertEquals(1, longValue.getValue());

		properties.setProperty("longProperty", "3");
		assertEquals(3, longValue.getValue());
		
		properties.clear();
		assertEquals(2, longValue.getValue());
	}
	
	public void testLongInvalid() {
		longValue.setDefaultValue(2);
		properties.setProperty("longProperty", "1");
		assertEquals(1, longValue.getValue());

		properties.setProperty("longProperty", "invalid");
		assertEquals(1, longValue.getValue());
		
		properties.clear();
		assertEquals(2, longValue.getValue());
	}
	
	public void testFloatSetValue() {
		floatValue.setDefaultValue(2);
		properties.setProperty("floatProperty", "1");
		assertEquals(1.0F, floatValue.getValue());

		properties.setProperty("floatProperty", "3");
		assertEquals(3.0F, floatValue.getValue());
		
		properties.clear();
		assertEquals(2.0F, floatValue.getValue());
	}
	
	public void testFloatInvalid() {
		floatValue.setDefaultValue(2);
		properties.setProperty("floatProperty", "1");
		assertEquals(1.0F, floatValue.getValue());

		properties.setProperty("floatProperty", "invalid");
		assertEquals(1.0F, floatValue.getValue());
		
		properties.clear();
		assertEquals(2.0F, floatValue.getValue());
	}
	
	public void testDoubleSetValue() {
		doubleValue.setDefaultValue(2);
		properties.setProperty("doubleProperty", "1");
		assertEquals(1.0, doubleValue.getValue());

		properties.setProperty("doubleProperty", "3");
		assertEquals(3.0, doubleValue.getValue());
		
		properties.clear();
		assertEquals(2.0, doubleValue.getValue());
	}
	
	public void testDoubleInvalid() {
		doubleValue.setDefaultValue(2);
		properties.setProperty("doubleProperty", "1");
		assertEquals(1.0, doubleValue.getValue());

		properties.setProperty("doubleProperty", "invalid");
		assertEquals(1.0, doubleValue.getValue());
		
		properties.clear();
		assertEquals(2.0, doubleValue.getValue());
	}
	
	public void testbooleanSetValue() {
		booleanValue.setDefaultValue(true);
		properties.setProperty("booleanProperty", "true");
		assertEquals(true, booleanValue.getValue());

		properties.setProperty("booleanProperty", "false");
		assertEquals(false, booleanValue.getValue());
		
		properties.clear();
		assertEquals(true, booleanValue.getValue());
	}
	
	public void testbooleanInvalid() {
		booleanValue.setDefaultValue(true);
		properties.setProperty("booleanProperty", "false");
		assertEquals(false, booleanValue.getValue());

		properties.setProperty("booleanProperty", "invalid");
		assertEquals(false, booleanValue.getValue());
		
		properties.clear();
		assertEquals(true, booleanValue.getValue());
	}
}
