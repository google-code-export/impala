package net.java.impala.spring.beanset;

import java.util.Properties;

import org.springframework.beans.FatalBeanException;

import junit.framework.TestCase;

public class BeanSetPropertiesReaderTest extends TestCase {

	public void testReadModuleSpec() {
		Properties spec = new BeanSetPropertiesReader()
				.readModuleSpec("null: set1, set2; mock: set3, duff");
		assertEquals("applicationContext-set1-null.xml", spec.getProperty("set1"));
		assertEquals("applicationContext-set2-null.xml", spec.getProperty("set2"));
		assertEquals("applicationContext-set3-mock.xml", spec.getProperty("set3"));
	}

	public void testReadAll() {
		Properties spec = new BeanSetPropertiesReader().readModuleSpec("null: all_beans");
		assertEquals("applicationContext-set1-null.xml", spec.getProperty("set1"));
		assertEquals("applicationContext-set2-null.xml", spec.getProperty("set2"));
	}

	public void testMissingColonIndex() {
		try {
			new BeanSetPropertiesReader().readModuleSpec("null: set1, set2; mock set3");
			fail("Expected missing colon index failure");
		}
		catch (FatalBeanException e) {
			assertEquals(
					"Invalid beanset specification. Missing ':' from string ' mock set3' in 'null: set1, set2; mock set3'",
					e.getMessage());
		}
	}

	public void testNothingAtEnd() {
		Properties spec = new BeanSetPropertiesReader().readModuleSpec("null:, set1; mock:");
		assertEquals("applicationContext-set1-null.xml", spec.getProperty("set1"));
	}

}
