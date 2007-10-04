package net.java.impala.util;

import java.util.Properties;

import net.java.impala.exception.ExecutionException;

import org.springframework.core.io.ClassPathResource;

import junit.framework.TestCase;

public class PropertyUtilsTest extends TestCase {
	public void testLoadProperties() throws Exception {
		ClassPathResource resource = new ClassPathResource("beanset/beansets.properties");
		Properties props = PropertyUtils.loadProperties(resource);
		assertNotNull(props.getProperty("bean2and3"));
	}

	public void testLoadDuffProperties() throws Exception {
		try {
			ClassPathResource resource = new ClassPathResource("propertiesthatdontexist");
			PropertyUtils.loadProperties(resource);
		}
		catch (ExecutionException e) {
			assertEquals("Unable to load properties file class path resource [propertiesthatdontexist]", e.getMessage());
		}
	}
}
