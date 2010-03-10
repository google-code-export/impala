/*
 * Copyright 2007-2010 the original author or authors.
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

import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

import org.impalaframework.bootstrap.ConfigurationSettings;
import org.impalaframework.config.PropertySource;
import org.impalaframework.config.StaticPropertiesPropertySource;

public class TestContextLocationResolverTest extends TestCase {

    private TestContextLocationResolver resolver;
    private Properties properties;
    private StaticPropertiesPropertySource propertySource;
    private ConfigurationSettings configSettings;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        resolver = new TestContextLocationResolver() {

            @Override
            protected void maybeAddJmxLocations(ConfigurationSettings configLocations, PropertySource propertySource) {
            }
            
        };
        propertySource = new StaticPropertiesPropertySource();
        properties = new Properties();
        propertySource.setProperties(properties);
        configSettings = new ConfigurationSettings();
    }

    public void testAddContextLocations() {
        resolver.addCustomLocations(configSettings, propertySource);
        assertLocations("impala-bootstrap.xml", "impala-graph", "impala-test-bootstrap.xml");
    }
    
    private void assertLocations(String... locations) {
        final List<String> configLocations = configSettings.getContextLocations();
        assertEquals(locations.length, configLocations.size());
        for (int i = 0; i < locations.length; i++) {
            String actualLocation = configLocations.get(i);
            String expectedLocation = locations[i];
            assertTrue(actualLocation.contains(expectedLocation));
            assertTrue(actualLocation.contains("impala"));
        }
    }
}
