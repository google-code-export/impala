package org.impalaframework.plugin.web;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Arrays;

import javax.servlet.ServletContext;

import org.springframework.core.io.DefaultResourceLoader;

import junit.framework.TestCase;

public class ExternalBootstrapLocationResolutionStrategyTest extends TestCase {

	private ExternalBootstrapLocationResolutionStrategy strategy;

	private ServletContext servletContext;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		strategy = new ExternalBootstrapLocationResolutionStrategy();
		servletContext = createMock(ServletContext.class);
	}

	public final void testLocationsNeitherSet() {
		expect(servletContext.getInitParameter(WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM)).andReturn(null);

		replay(servletContext);

		String[] locations = strategy.getBootstrapContextLocations(servletContext);
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
			String[] locations = strategy.getBootstrapContextLocations(servletContext);
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
			String[] locations = strategy.getBootstrapContextLocations(servletContext);
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
				strategy.getBootstrapContextLocations(servletContext);
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

	public final void testGetResourceLoader() {
		assertTrue(strategy.getResourceLoader() instanceof DefaultResourceLoader);
	}

}
