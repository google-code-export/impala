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

package org.impalaframework.osgi.bootstrap;

import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

import org.impalaframework.bootstrap.ConfigurationSettings;
import org.impalaframework.config.StaticPropertiesPropertySource;

public class OsgiContextLocationResolverTest extends TestCase {
    
    private OsgiContextLocationResolver resolver;
    private Properties properties;
    private StaticPropertiesPropertySource propertySource;
    private ConfigurationSettings configSettings;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        resolver = new OsgiContextLocationResolver();
        propertySource = new StaticPropertiesPropertySource();
        properties = new Properties();
        propertySource.setProperties(properties);
        configSettings = new ConfigurationSettings();
    }
    
    public void testAddContextLocations() {
        resolver.addContextLocations(configSettings, propertySource);
        final List<String> configLocations = configSettings.getContextLocations();
        assertEquals(2, configLocations.size());
        assertEquals("META-INF/impala-bootstrap.xml", configLocations.get(0));
        assertEquals("META-INF/impala-osgi-bootstrap.xml", configLocations.get(1));
    }

}
