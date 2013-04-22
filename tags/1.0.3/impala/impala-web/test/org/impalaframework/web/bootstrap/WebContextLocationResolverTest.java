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

package org.impalaframework.web.bootstrap;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import junit.framework.TestCase;

import org.impalaframework.bootstrap.ConfigurationSettings;
import org.impalaframework.config.PropertyValue;
import org.impalaframework.config.StaticPropertiesPropertySource;

public class WebContextLocationResolverTest extends TestCase {

    private WebContextLocationResolver resolver;
    private Properties properties;
    private StaticPropertiesPropertySource propertySource;
    private ConfigurationSettings configSettings;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        resolver = new WebContextLocationResolver();
        propertySource = new StaticPropertiesPropertySource();
        properties = new Properties();
        propertySource.setProperties(properties);
        configSettings = new ConfigurationSettings();
    }
    
    public void testExplicitlySetLocations() {
        properties.setProperty("all.locations", "impala-location1,impala-location2 impala-location3");
        assertTrue(resolver.addContextLocations(configSettings, propertySource));
        assertLocations("location1", "location2", "location3");
    }
    
    public void testNotExplicitlySetLocations() {
        assertFalse(resolver.addContextLocations(configSettings, propertySource));
    }
    
    public void testSetProperties() {
        resolver.addContextLocations(configSettings, propertySource);
        final Map<String, PropertyValue> propertyValues = configSettings.getPropertyValues();
        assertTrue(propertyValues.containsKey(WebBootstrapProperties.AUTO_RELOAD_MODULES));
        assertTrue(propertyValues.containsKey(WebBootstrapProperties.EMBEDDED_MODE));
        assertTrue(propertyValues.containsKey(WebBootstrapProperties.PARTITIONED_SERVLET_CONTEXT));
        assertTrue(propertyValues.containsKey(WebBootstrapProperties.PRESERVE_SESSION_ON_RELOAD_FAILURE));
        assertTrue(propertyValues.containsKey(WebBootstrapProperties.SESSION_MODULE_PROTECTION));
        assertFalse(propertyValues.containsKey(WebBootstrapProperties.AUTO_RELOAD_CHECK_DELAY));
        assertFalse(propertyValues.containsKey(WebBootstrapProperties.AUTO_RELOAD_CHECK_INTERVAL));
        assertFalse(propertyValues.containsKey(WebBootstrapProperties.USE_TOUCH_FILE));
        assertFalse(propertyValues.containsKey(WebBootstrapProperties.TOUCH_FILE));
    }
    
    public void testAutoreloadProps() throws Exception {
        properties.setProperty("auto.reload.modules", "true");
        resolver.addContextLocations(configSettings, propertySource);
        final Map<String, PropertyValue> propertyValues = configSettings.getPropertyValues();
        assertTrue(propertyValues.containsKey(WebBootstrapProperties.AUTO_RELOAD_CHECK_DELAY));
        assertTrue(propertyValues.containsKey(WebBootstrapProperties.AUTO_RELOAD_CHECK_INTERVAL));
        assertTrue(propertyValues.containsKey(WebBootstrapProperties.USE_TOUCH_FILE));
        assertTrue(propertyValues.containsKey(WebBootstrapProperties.TOUCH_FILE));
    }

    public void testAddDefaultContextLocations() {
        resolver.addDefaultLocations(configSettings);
        assertLocations("impala-bootstrap.xml", "impala-web-bootstrap.xml");
    }
    
    public void testDefaultJarModuleLocation() throws Exception {
        //default is to deploy as jar modules
        resolver.addJarModuleLocation(configSettings, propertySource);
        assertLocations("impala-web-jar-module-bootstrap.xml");
    }
    
    public void testEnableJmx() throws Exception {
        //default is false
        resolver.addJmxOperations(configSettings, propertySource);
        assertLocations();
    }
    
    public void testEnableJmxDefault() throws Exception {
        //default is to deploy as jar modules
        properties.setProperty("enable.web.jmx.operations", "true");
        resolver.addJmxOperations(configSettings, propertySource);
        assertLocations("impala-web-jmx-bootstrap.xml");
    }
    
    public void testJarModuleLocation() throws Exception {
        properties.setProperty("embedded.mode", "true");
        resolver.addJarModuleLocation(configSettings, propertySource);
        assertLocations();
    }
    
    public void testDefaultAutoReloadLocation() throws Exception {
        resolver.addAutoReloadListener(configSettings, propertySource);
        assertLocations();
    }
    
    public void testAutoReloadLocation() throws Exception {
        properties.setProperty("auto.reload.modules", "true");
        resolver.addAutoReloadListener(configSettings, propertySource);
        assertLocations("web-listener-bootstrap.xml");
    }
    
    public void testDefaultPathMapper() throws Exception {
        resolver.addPathModuleMapper(configSettings, propertySource);
        assertLocations();
    }
    
    public void testPathMaper() throws Exception {
        properties.setProperty("module.prefix.mapping.enabled", "true");
        resolver.addPathModuleMapper(configSettings, propertySource);
        assertLocations("web-path-mapper");assertTrue(configSettings.getPropertyValues().containsKey(WebBootstrapProperties.MODULE_PREFIX_MAPPING_ENABLED));
        assertTrue(configSettings.getPropertyValues().containsKey(WebBootstrapProperties.WEB_MODULE_PREFIX));
    }
    
    private void assertLocations(String... locations) {
        final List<String> configLocations = configSettings.getContextLocations();
        assertEquals(locations.length, configLocations.size());
        System.out.println(configSettings);
        for (int i = 0; i < locations.length; i++) {
            String actualLocation = configLocations.get(i);
            String expectedLocation = locations[i];
            assertTrue(actualLocation.contains(expectedLocation));
            assertTrue(actualLocation.contains("impala"));
        }
    }
}
