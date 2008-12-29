package org.impalaframework.osgi.bootstrap;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

import org.impalaframework.config.StaticPropertiesPropertySource;

public class OsgiContextLocationResolverTest extends TestCase {
	
	private OsgiContextLocationResolver resolver;
	private Properties properties;
	private StaticPropertiesPropertySource propertySource;
	private List<String> contextLocations;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		resolver = new OsgiContextLocationResolver();
		propertySource = new StaticPropertiesPropertySource();
		properties = new Properties();
		propertySource.setProperties(properties);
		contextLocations = new ArrayList<String>();
	}
	
	public void testAddContextLocations() {
		resolver.addContextLocations(contextLocations, propertySource);
		assertEquals(2, contextLocations.size());
		assertEquals("META-INF/impala-bootstrap.xml", contextLocations.get(0));
		assertEquals("META-INF/impala-osgi-bootstrap.xml", contextLocations.get(1));
	}

}
