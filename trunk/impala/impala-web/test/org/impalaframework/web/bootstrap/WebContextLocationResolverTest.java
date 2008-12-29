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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

import org.impalaframework.config.StaticPropertiesPropertySource;

public class WebContextLocationResolverTest extends TestCase {

	private WebContextLocationResolver resolver;
	private Properties properties;
	private StaticPropertiesPropertySource propertySource;
	private List<String> contextLocations;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		resolver = new WebContextLocationResolver();
		propertySource = new StaticPropertiesPropertySource();
		properties = new Properties();
		propertySource.setProperties(properties);
		contextLocations = new ArrayList<String>();
	}

	public void testAddDefaultContextLocations() {
		resolver.addDefaultLocations(contextLocations);
		assertLocations("impala-bootstrap.xml", "impala-web-bootstrap.xml");
	}
	
	public void testDefaultJarModuleLocation() throws Exception {
		//default is to deploy as jar modules
		resolver.addJarModuleLocation(contextLocations, propertySource);
		assertLocations("impala-web-jar-module-bootstrap.xml");
	}
	
	public void testJarModuleLocation() throws Exception {
		properties.setProperty("embeddedMode", "true");
		resolver.addJarModuleLocation(contextLocations, propertySource);
		assertLocations();
	}
	
	public void testDefaultWebMultiModuleLocation() throws Exception {
		//default is to deploy as jar modules
		resolver.addWebMultiModuleLocation(contextLocations, propertySource);
		assertLocations();
	}
	
	public void testWebMultiModuleLocation() throws Exception {
		properties.setProperty("webMultiModule", "true");
		resolver.addWebMultiModuleLocation(contextLocations, propertySource);
		assertLocations("web-moduleaware.xml");
	}
	
	private void assertLocations(String... locations) {
		assertEquals(locations.length, contextLocations.size());
		System.out.println(contextLocations);
		for (int i = 0; i < locations.length; i++) {
			String actualLocation = contextLocations.get(i);
			String expectedLocation = locations[i];
			assertTrue(actualLocation.contains(expectedLocation));
		}
	}
}
