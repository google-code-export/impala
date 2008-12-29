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

package org.impalaframework.interactive.bootstrap;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

import org.impalaframework.config.StaticPropertiesPropertySource;

public class TestContextLocationResolverTest extends TestCase {

	private TestContextLocationResolver resolver;
	private Properties properties;
	private StaticPropertiesPropertySource propertySource;
	private List<String> contextLocations;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		resolver = new TestContextLocationResolver();
		propertySource = new StaticPropertiesPropertySource();
		properties = new Properties();
		propertySource.setProperties(properties);
		contextLocations = new ArrayList<String>();
	}

	public void testAddDefaultClassLoaderType() {
		resolver.addContextLocations(contextLocations, propertySource);
		assertLocations("impala-bootstrap.xml", "impala-test-bootstrap.xml");
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
