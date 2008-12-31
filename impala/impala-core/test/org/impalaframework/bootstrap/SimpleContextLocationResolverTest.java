package org.impalaframework.bootstrap;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

import org.impalaframework.config.StaticPropertiesPropertySource;
import org.impalaframework.exception.ConfigurationException;

public class SimpleContextLocationResolverTest extends TestCase {

	private SimpleContextLocationResolver resolver;
	private Properties properties;
	private StaticPropertiesPropertySource propertySource;
	private List<String> contextLocations;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		resolver = new SimpleContextLocationResolver();
		propertySource = new StaticPropertiesPropertySource();
		properties = new Properties();
		propertySource.setProperties(properties);
		contextLocations = new ArrayList<String>();
	}

	public void testDefaultExplicitlySetLocations() {
		assertFalse(resolver.explicitlySetLocations(contextLocations, propertySource));
		assertLocations();
	}
	
	public void testExplicitlySetLocations() {
		properties.setProperty("allLocations", "impala-location1,impala-location2 impala-location3");
		assertTrue(resolver.explicitlySetLocations(contextLocations, propertySource));
		assertLocations("location1", "location2", "location3");
	}
	
	public void testSetBootstrapLocations() {
		properties.setProperty("bootstrapLocations", "impala-location1,impala-location2 impala-location3");
		assertTrue(resolver.explicitlySetLocations(contextLocations, propertySource));
		assertLocations("location1", "location2", "location3");
	}
	
	public void testAddContextLocations() {
		properties.setProperty("allLocations", "impala-location1,impala-location2 impala-location3");
		assertTrue(resolver.addContextLocations(contextLocations, propertySource));
		assertLocations("location1", "location2", "location3");
	}

	public void testDefaultExplicitlyAddLocations() {
		resolver.explicitlyAddLocations(contextLocations, propertySource);
		assertLocations();
	}
	
	public void testExplicitlyAddLocations() {
		properties.setProperty("extraLocations", "location1,location2 location3");
		resolver.explicitlyAddLocations(contextLocations, propertySource);
		assertLocations("META-INF/impala-location1.xml", "location2", "location3");
	}

	public void testDefaultAddParentClassLoaderFirst() {
		properties.setProperty("parentClassLoaderFirst", "false");
		resolver.addParentClassLoaderFirst(contextLocations, propertySource);
		assertLocations();
	}

	public void testAddParentClassLoaderFirst() {
		resolver.addParentClassLoaderFirst(contextLocations, propertySource);
		assertLocations("impala-parent-loader");
	}
	
	public void testAddDefaultClassLoaderType() {
		resolver.addModuleType(contextLocations, propertySource);
		assertLocations("impala-graph");
	}

	public void testAddHierarchicalClassLoaderType() {
		properties.setProperty("moduleType", "hierarchical");
		resolver.addModuleType(contextLocations, propertySource);
		assertLocations();
	}

	public void testAddGraphClassLoaderType() {
		properties.setProperty("moduleType", "graph");
		resolver.addModuleType(contextLocations, propertySource);
		assertLocations("impala-graph");
	}

	public void testAddSharedClassLoaderType() {
		properties.setProperty("moduleType", "shared");
		resolver.addModuleType(contextLocations, propertySource);
		assertLocations("impala-shared-loader");
	}

	public void testAddInvalidClassLoaderType() {
		properties.setProperty("moduleType", "invalid");
		try {
			resolver.addModuleType(contextLocations, propertySource);
		} catch (ConfigurationException e) {
			assertEquals("Invalid value for property 'moduleType': invalid", e.getMessage());
		}
	}
	
	public void testAddJmxLocations() throws Exception {
		properties.setProperty("exposeJmxOperations", "true");
		resolver.maybeAddJmxLocations(contextLocations, propertySource);
		//note: the next line can be uncommented when running test individually (not as part of overall suite) 
		//assertLocations();
	}
	
	private void assertLocations(String... locations) {
		assertEquals(locations.length, contextLocations.size());
		for (int i = 0; i < locations.length; i++) {
			String actualLocation = contextLocations.get(i);
			String expectedLocation = locations[i];
			assertTrue(actualLocation.contains(expectedLocation));
			assertTrue(actualLocation.contains("impala"));
		}
	}
}
