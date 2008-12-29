package org.impalaframework.jmx.bootstrap;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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

import junit.framework.TestCase;

import org.impalaframework.config.StaticPropertiesPropertySource;

public class JMXContextLocationResolverTest extends TestCase {
	private JMXContextLocationResolver resolver;
	private Properties properties;
	private StaticPropertiesPropertySource propertySource;
	private List<String> contextLocations;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		resolver = new JMXContextLocationResolver();
		propertySource = new StaticPropertiesPropertySource();
		properties = new Properties();
		propertySource.setProperties(properties);
		contextLocations = new ArrayList<String>();
	}

	public void testDefaultExposeJmxOperations() {
		resolver.addJmxOperations(contextLocations, propertySource);
		assertLocations("impala-jmx-boot");
	}

	public void testExposeJmxOperations() {
		properties.setProperty("exposeJmxOperations", "false");
		resolver.addJmxOperations(contextLocations, propertySource);
		assertLocations();
	}
	
	public void testMx4jPresent() throws Exception {
		assertTrue(resolver.isMX4JPresent());
	}
	
	public void testDefaultAddMx4jAdaptor() throws Exception {
		resolver.addMx4jAdaptorContext(contextLocations, propertySource);
		assertLocations();
	}
	
	public void testAddMx4jAdaptorNoJmx() throws Exception {
		properties.setProperty("exposeMx4jAdaptor", "true");
		resolver.addMx4jAdaptorContext(contextLocations, propertySource);
		assertLocations();
	}
	
	public void testAddMx4jAdaptor() throws Exception {
		contextLocations.add("META-INF/impala-jmx-bootstrap.xml");
		properties.setProperty("exposeMx4jAdaptor", "true");
		resolver.addMx4jAdaptorContext(contextLocations, propertySource);
		assertLocations("META-INF/impala-jmx-bootstrap.xml", "META-INF/impala-jmx-adaptor-bootstrap.xml");
	}
	
	public void testAddMx4jAdaptorLibrariesNotPresent() throws Exception {
		contextLocations.add("META-INF/impala-jmx-bootstrap.xml");
		properties.setProperty("exposeMx4jAdaptor", "true");
		
		resolver = new JMXContextLocationResolver() {

			@Override
			boolean isMX4JPresent() {
				return false;
			}
			
		};
		
		resolver.addMx4jAdaptorContext(contextLocations, propertySource);
		assertLocations("META-INF/impala-jmx-bootstrap.xml");
	}
	
	private void assertLocations(String... locations) {
		assertEquals(locations.length, contextLocations.size());
		for (int i = 0; i < locations.length; i++) {
			String actualLocation = contextLocations.get(i);
			String expectedLocation = locations[i];
			assertTrue(actualLocation.contains(expectedLocation));
		}
	}
}
