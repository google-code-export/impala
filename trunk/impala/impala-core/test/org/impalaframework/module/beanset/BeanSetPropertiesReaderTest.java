package org.impalaframework.module.beanset;

import java.util.Properties;

import junit.framework.TestCase;

import org.impalaframework.exception.ConfigurationException;
import org.springframework.util.ClassUtils;

public class BeanSetPropertiesReaderTest extends TestCase {

	public void testReadModuleDefinition() {
		Properties definition = new BeanSetPropertiesReader()
				.readBeanSetDefinition(ClassUtils.getDefaultClassLoader(), "null: set1, set2; mock: set3, duff");
		assertEquals("applicationContext-set1-null.xml", definition.getProperty("set1"));
		assertEquals("applicationContext-set2-null.xml", definition.getProperty("set2"));
		assertEquals("applicationContext-set3-mock.xml", definition.getProperty("set3"));
	}

	public void testReadAll() {
		Properties definition = new BeanSetPropertiesReader().readBeanSetDefinition(ClassUtils.getDefaultClassLoader(), "null: all_beans");
		assertEquals("applicationContext-set1-null.xml", definition.getProperty("set1"));
		assertEquals("applicationContext-set2-null.xml", definition.getProperty("set2"));
		assertEquals("applicationContext-set3-null.xml", definition.getProperty("set3"));
	}

	public void testMissingColonIndex() {
		try {
			new BeanSetPropertiesReader().readBeanSetDefinition(ClassUtils.getDefaultClassLoader(), "null: set1, set2; mock set3");
			fail("Expected missing colon index failure");
		}
		catch (ConfigurationException e) {
			assertEquals(
					"Invalid beanset definition. Missing ':' from string ' mock set3' in 'null: set1, set2; mock set3'",
					e.getMessage());
		}
	}

	public void testNothingAtEnd() {
		Properties definition = new BeanSetPropertiesReader().readBeanSetDefinition(ClassUtils.getDefaultClassLoader(), "null:, set1; mock:");
		assertEquals("applicationContext-set1-null.xml", definition.getProperty("set1"));
	}

}
