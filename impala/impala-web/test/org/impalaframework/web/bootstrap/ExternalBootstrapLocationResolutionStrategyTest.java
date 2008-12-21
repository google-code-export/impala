/*
 * Copyright 2007-2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.web.bootstrap;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.module.ModuleDefinitionSource;
import org.impalaframework.web.WebConstants;
import org.impalaframework.web.loader.BaseImpalaContextLoader;
import org.impalaframework.web.module.WebModuleUtils;
import org.springframework.core.io.DefaultResourceLoader;

public class ExternalBootstrapLocationResolutionStrategyTest extends TestCase {

	private ExternalBootstrapLocationResolutionStrategy strategy;

	private ServletContext servletContext;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		strategy = new ExternalBootstrapLocationResolutionStrategy();
		servletContext = createMock(ServletContext.class);
	}

	public final void testDefaultBootstrapProperties() {
		expect(servletContext.getInitParameter(WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM)).andReturn(null);

		replay(servletContext);

		String[] locations = strategy.getBootstrapContextLocations(servletContext);
		assertTrue(locations.length > 0);
		// check that we have the same locations as the superclass
		List<String> list = Arrays.asList(locations);
		assertTrue(list.contains("web-jar-module-bootstrap"));

		verify(servletContext);
	}

	public final void testLocationsNeitherSet() {
		strategy.setDefaultBootstrapResource("notfound.properties");
		expect(servletContext.getInitParameter(WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM)).andReturn(null);

		replay(servletContext);

		String[] locations = strategy.getBootstrapContextLocations(servletContext);
		assertTrue(locations.length > 0);
		// check that we have the same locations as the superclass
		assertTrue(Arrays.equals(locations, new DummyContextLoader().getBootstrapContextLocations(servletContext)));

		verify(servletContext);
	}

	public final void testLocationsSetViaServletContext() {
		expect(servletContext.getInitParameter(WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM)).andReturn(
				"./locations.properties");

		replay(servletContext);

		String resourceName = WebModuleUtils.getLocationsResourceName(servletContext, WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM);
		assertEquals("./locations.properties", resourceName);
		verify(servletContext);
	}

	public final void testLocationsSetViaSystemProperty() {
		System.setProperty(WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM, "./sysprop.location");
		try {
			replay(servletContext);
			String resourceName = WebModuleUtils.getLocationsResourceName(servletContext, WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM);
			assertEquals("./sysprop.location", resourceName);
			verify(servletContext);
		}
		finally {
			System.clearProperty(WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM);
		}
	}

	public final void testLocationsSetGetProperties() {
		System.setProperty(WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM,
				"org/impalaframework/web/module/locations.properties");
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

			assertTrue(Arrays.equals(locations, new DummyContextLoader().getBootstrapContextLocations(servletContext)));
			verify(servletContext);
		}
		finally {
			System.clearProperty(WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM);
		}
	}

	public final void testLocationsWithPropertyNotFound() {
		System.setProperty(WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM,
				"org/impalaframework/web/module/unspecified_locations.properties");
		try {
			replay(servletContext);

			try {
				strategy.getBootstrapContextLocations(servletContext);
				fail();
			}
			catch (ConfigurationException e) {
				assertEquals("Bootstrap location resource 'class path resource [org/impalaframework/web/module/unspecified_locations.properties]' does not contain property 'bootstrapLocations'", e.getMessage());
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
	
	class DummyContextLoader extends BaseImpalaContextLoader {

		@Override
		public ModuleDefinitionSource getModuleDefinitionSource(
				ServletContext servletContext, ModuleManagementFacade factory) {
			return null;
		}
		 
	}

}
