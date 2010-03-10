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

package org.impalaframework.bootstrap;

import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

import org.impalaframework.config.StaticPropertiesPropertySource;
import org.impalaframework.exception.ConfigurationException;

public class SimpleContextLocationResolverTest extends TestCase {

    private SimpleContextLocationResolver resolver;
    private Properties properties;
    private StaticPropertiesPropertySource propertySource;
    private ConfigurationSettings configSettings;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        resolver = new SimpleContextLocationResolver();
        propertySource = new StaticPropertiesPropertySource();
        properties = new Properties();
        propertySource.setProperties(properties);
        configSettings = new ConfigurationSettings();
    }

    public void testAddNoProxyProperties() {
        resolver.addProxyProperties(configSettings, propertySource);
        assertLocations();
    }

    public void testAddProxyProperties() {
        properties.setProperty("proxy.allow.no.service", "true");
        resolver.addProxyProperties(configSettings, propertySource);
        assertLocations();
    }

    public void testDefaultExplicitlySetLocations() {
        assertFalse(resolver.explicitlySetLocations(configSettings, propertySource));
        assertLocations();
    }
    
    public void testExplicitlySetLocations() {
        properties.setProperty("all.locations", "impala-location1,impala-location2 impala-location3");
        assertTrue(resolver.explicitlySetLocations(configSettings, propertySource));
        assertLocations("location1", "location2", "location3");
    }
    
    public void testAddContextLocations() {
        properties.setProperty("all.locations", "impala-location1,impala-location2 impala-location3");
        assertTrue(resolver.addContextLocations(configSettings, propertySource));
        assertLocations("location1", "location2", "location3");
    }

    public void testDefaultExplicitlyAddLocations() {
        resolver.explicitlyAddLocations(configSettings, propertySource);
        assertLocations();
    }
    
    public void testExplicitlyAddLocations() {
        properties.setProperty("extra.locations", "location1,location2 location3");
        resolver.explicitlyAddLocations(configSettings, propertySource);
        assertLocations("META-INF/impala-location1.xml", "location2", "location3");
    }
    
    public void testAddDefaultClassLoaderType() {
        resolver.addModuleType(configSettings, propertySource);
        assertLocations("impala-graph");
    }

    public void testAddHierarchicalClassLoaderType() {
        properties.setProperty("classloader.type", "hierarchical");
        resolver.addModuleType(configSettings, propertySource);
        assertLocations();
    }

    public void testAddGraphClassLoaderType() {
        properties.setProperty("classloader.type", "graph");
        resolver.addModuleType(configSettings, propertySource);
        assertLocations("impala-graph");
    }

    public void testAddSharedClassLoaderType() {
        properties.setProperty("classloader.type", "shared");
        resolver.addModuleType(configSettings, propertySource);
        assertLocations("impala-shared-loader");
    }

    public void testAddInvalidClassLoaderType() {
        properties.setProperty("classloader.type", "invalid");
        try {
            resolver.addModuleType(configSettings, propertySource);
        } catch (ConfigurationException e) {
            assertEquals("Invalid value for property 'classloader.type': invalid", e.getMessage());
        }
    }
    
    public void testAddJmxLocations() throws Exception {
        properties.setProperty("expose.jmx.operations", "true");
        resolver.maybeAddJmxLocations(configSettings, propertySource);
        //note: the next line can be uncommented when running test individually (not as part of overall suite) 
        //assertLocations();
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
