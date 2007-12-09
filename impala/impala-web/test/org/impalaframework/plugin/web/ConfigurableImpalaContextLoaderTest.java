package org.impalaframework.plugin.web;

import java.util.Arrays;

import javax.servlet.ServletContext;

import static org.easymock.EasyMock.*;

import junit.framework.TestCase;

public class ConfigurableImpalaContextLoaderTest extends TestCase {

	private ConfigurableImpalaContextLoader contextLoader;

	private ServletContext servletContext;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		contextLoader = new ConfigurableImpalaContextLoader();
		servletContext = createMock(ServletContext.class);
	}

	public final void testLocationsNeitherSet() {
		expect(servletContext.getInitParameter(WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM)).andReturn(null);

		replay(servletContext);

		String[] locations = contextLoader.getBootstrapContextLocations(servletContext);
		assertTrue(locations.length > 0);
		// check that we have the same locations as the superclass
		assertTrue(Arrays.equals(locations, new WebXmlBasedImpalaContextLoader().getBootstrapContextLocations(servletContext)));

		verify(servletContext);
	}

	public final void testLocationsSetViaServletContext() {
		expect(servletContext.getInitParameter(WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM)).andReturn(
				"./locations.properties");

		replay(servletContext);

		String resourceName = WebPluginUtils.getLocationsResourceName(servletContext, WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM);
		assertEquals("./locations.properties", resourceName);
		verify(servletContext);
	}

	public final void testLocationsSetViaSystemProperty() {
		System.setProperty(WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM, "./sysprop.location");
		try {
			replay(servletContext);
			String resourceName = WebPluginUtils.getLocationsResourceName(servletContext, WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM);
			assertEquals("./sysprop.location", resourceName);
			verify(servletContext);
		}
		finally {
			System.clearProperty(WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM);
		}
	}

	public final void testLocationsSetGetProperties() {
		System.setProperty(WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM,
				"org/impalaframework/plugin/web/locations.properties");
		try {
			replay(servletContext);
			String[] locations = contextLoader.getBootstrapContextLocations(servletContext);
			System.out.println(Arrays.toString(locations));

			assertEquals(2, locations.length);
			assertEquals("location1.xml", locations[0]);
			assertEquals("location2.xml", locations[1]);
			verify(servletContext);
		}
		finally {
			System.clearProperty(WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM);
		}
	}

	public final void testLocationsSetGetPropertiesNotFound() {
		System.setProperty(WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM, "a location which does not exist");
		try {
			replay(servletContext);
			String[] locations = contextLoader.getBootstrapContextLocations(servletContext);
			System.out.println(Arrays.toString(locations));

			assertTrue(Arrays.equals(locations, new WebXmlBasedImpalaContextLoader().getBootstrapContextLocations(servletContext)));
			verify(servletContext);
		}
		finally {
			System.clearProperty(WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM);
		}
	}

	public final void testLocationsWithPropertyNotFound() {
		System.setProperty(WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM,
				"org/impalaframework/plugin/web/unspecified_locations.properties");
		try {
			replay(servletContext);

			try {
				contextLoader.getBootstrapContextLocations(servletContext);
				fail();
			}
			catch (IllegalStateException e) {
				assertEquals("Bootstrap location resource 'class path resource [org/impalaframework/plugin/web/unspecified_locations.properties]' does not contain property 'bootstrapLocations'", e.getMessage());
			}
			verify(servletContext);
		}
		finally {
			System.clearProperty(WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM);
		}
	}

	public final void testPluginsSetGetProperties() {
		System.setProperty(WebConstants.BOOTSTRAP_PLUGINS_RESOURCE_PARAM,
				"org/impalaframework/plugin/web/locations.properties");
		try {
			replay(servletContext);
			String pluginDefinition = contextLoader.getPluginDefinitionString(servletContext);
			assertEquals("plugin1,plugin2", pluginDefinition);
			verify(servletContext);

		}
		finally {
			System.clearProperty(WebConstants.BOOTSTRAP_PLUGINS_RESOURCE_PARAM);
		}
	}

	public final void testPluginsSetGetPropertiesNotFound() {
		System.setProperty(WebConstants.BOOTSTRAP_PLUGINS_RESOURCE_PARAM, "a location which does not exist");
		try {
			expect(servletContext.getInitParameter("pluginNames")).andReturn("a value");
			replay(servletContext);
			String definition = contextLoader.getPluginDefinitionString(servletContext);

			assertEquals("a value", definition);
			verify(servletContext);

		}
		finally {
			System.clearProperty(WebConstants.BOOTSTRAP_PLUGINS_RESOURCE_PARAM);
		}
	}

	public final void testPluginsWithPropertyNotFound() {
		System.setProperty(WebConstants.BOOTSTRAP_PLUGINS_RESOURCE_PARAM,
				"org/impalaframework/plugin/web/unspecified_locations.properties");
		try {
			replay(servletContext);

			try {
				contextLoader.getPluginDefinitionString(servletContext);
				fail();
			}
			catch (IllegalStateException e) {
				assertEquals("Bootstrap location resource 'class path resource [org/impalaframework/plugin/web/unspecified_locations.properties]' does not contain property 'pluginNames'" +
						"", e.getMessage());
			}
			verify(servletContext);
		}
		finally {
			System.clearProperty(WebConstants.BOOTSTRAP_PLUGINS_RESOURCE_PARAM);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public final void testParentLocationsSetGetProperties() {
		System.setProperty(WebConstants.BOOTSTRAP_PLUGINS_RESOURCE_PARAM,
				"org/impalaframework/plugin/web/locations.properties");
		try {
			replay(servletContext);
			String[] parentLocations = contextLoader.getParentLocations(servletContext);
			System.out.println(Arrays.toString(parentLocations));
			assertTrue(Arrays.equals(new String[]{"parent1", "parent2"}, parentLocations));
			verify(servletContext);

		}
		finally {
			System.clearProperty(WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM);
		}
	}

	public final void testParentLocationsGetPropertiesNotFound() {
		System.setProperty(WebConstants.BOOTSTRAP_PLUGINS_RESOURCE_PARAM, "a location which does not exist");
		try {
			expect(servletContext.getInitParameter("contextConfigLocation")).andReturn("location1 location2");
			replay(servletContext);
			String[] parentLocations = contextLoader.getParentLocations(servletContext);

			System.out.println(Arrays.toString(parentLocations));
			assertTrue(Arrays.equals(new String[]{"location1", "location2"}, parentLocations));
			verify(servletContext);

		}
		finally {
			System.clearProperty(WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM);
		}
	}

	public final void testParentLocationsPropertyNotFound() {
		System.setProperty(WebConstants.BOOTSTRAP_PLUGINS_RESOURCE_PARAM,
				"org/impalaframework/plugin/web/unspecified_locations.properties");
		try {
			replay(servletContext);

			try {
				contextLoader.getParentLocations(servletContext);
				fail();
			}
			catch (IllegalStateException e) {
				assertEquals("Bootstrap location resource 'class path resource [org/impalaframework/plugin/web/unspecified_locations.properties]' does not contain property 'parentLocations'" +
						"", e.getMessage());
			}
			verify(servletContext);
		}
		finally {
			System.clearProperty(WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM);
		}
	}	
	
	
}
