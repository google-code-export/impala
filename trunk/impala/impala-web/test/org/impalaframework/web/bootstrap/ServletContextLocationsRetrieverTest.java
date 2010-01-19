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
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.impalaframework.bootstrap.ConfigurationSettings;
import org.impalaframework.bootstrap.ContextLocationResolver;
import org.impalaframework.config.CompositePropertySource;
import org.impalaframework.config.PropertiesHolder;
import org.impalaframework.config.PropertySource;
import org.impalaframework.config.PropertySourceHolder;
import org.impalaframework.config.SimplePropertiesLoader;
import org.impalaframework.config.StaticPropertiesPropertySource;
import org.impalaframework.config.SystemPropertiesPropertySource;
import org.impalaframework.constants.LocationConstants;
import org.impalaframework.web.config.ContextPathAwareSystemPropertySource;
import org.impalaframework.web.config.ServletContextPropertiesLoader;
import org.impalaframework.web.config.ServletContextPropertySource;

public class ServletContextLocationsRetrieverTest extends TestCase {

    private ServletContextLocationsRetriever resolver;

    private ServletContext servletContext;

    private SimplePropertiesLoader propertiesLoader;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        servletContext = createMock(ServletContext.class);
        propertiesLoader = new ServletContextPropertiesLoader(servletContext, "impala.properties");
        resolver = new ServletContextLocationsRetriever(servletContext, createMock(ContextLocationResolver.class), propertiesLoader);
        PropertiesHolder.getInstance().clearProperties();
        PropertySourceHolder.getInstance().clearPropertySource();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();       
        PropertiesHolder.getInstance().clearProperties();
        PropertySourceHolder.getInstance().clearPropertySource();
    }
    
    public void testGetPropertySources() throws Exception {
        final List<PropertySource> propertySources = resolver.getPropertySources(new Properties());
        assertEquals(4, propertySources.size());
        assertTrue(propertySources.get(0) instanceof ContextPathAwareSystemPropertySource);
        assertTrue(propertySources.get(1) instanceof SystemPropertiesPropertySource);
        assertTrue(propertySources.get(2) instanceof StaticPropertiesPropertySource);
        assertTrue(propertySources.get(3) instanceof ServletContextPropertySource);
    }
    
    public void testAddLocations() throws Exception {
        final ContextLocationResolver resolverDelegate = createMock(ContextLocationResolver.class);
        final PropertySource source = createMock(PropertySource.class);
        
        ServletContextLocationsRetriever resolver = new ServletContextLocationsRetriever(servletContext, resolverDelegate, propertiesLoader) {

            @Override
            protected Properties getProperties() {
                return new Properties();
            }

            @Override
            protected List<PropertySource> getPropertySources(Properties properties) {
                return Collections.singletonList(source);
            }
        };
        
        expect(resolverDelegate.addContextLocations(isA(ConfigurationSettings.class), isA(CompositePropertySource.class))).andReturn(false);
        
        replay(resolverDelegate, source);
        
        resolver.getContextLocations();
        
        verify(resolverDelegate, source);
    }

    public final void testDefaultGetProperties() {
        expect(servletContext.getMajorVersion()).andReturn(0);
        expect(servletContext.getInitParameter(LocationConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM)).andReturn(null);

        replay(servletContext);

        Properties properties = resolver.getProperties();
        assertFalse(properties.isEmpty());
        assertNotNull(properties.getProperty("bootstrapLocations"));
        
        //check that the PropertiesHolder is populated
        assertNotNull(PropertiesHolder.getInstance().getProperties());

        verify(servletContext);
    }

    public final void testGetPropertiesWithResourceNotFound() {
        expect(servletContext.getMajorVersion()).andReturn(0);
        propertiesLoader = new ServletContextPropertiesLoader(servletContext, "notfound.properties");
        resolver = new ServletContextLocationsRetriever(servletContext, createMock(ContextLocationResolver.class), propertiesLoader);
        expect(servletContext.getInitParameter(LocationConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM)).andReturn(null);

        replay(servletContext);
        
        Properties properties = resolver.getProperties();
        assertTrue(properties.isEmpty());
        
        verify(servletContext);
    }

    public final void testGetPropertiesLocationViaInitParameter() {
        expect(servletContext.getMajorVersion()).andReturn(0);
        expect(servletContext.getInitParameter(LocationConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM)).andReturn("org/impalaframework/web/module/locations.properties");

        replay(servletContext);

        Properties properties = resolver.getProperties();
        assertFalse(properties.isEmpty());
        assertNotNull(properties.getProperty("bootstrapLocations"));
        
        verify(servletContext);
    }

    public final void testGetPropertiesLocationViaSystemProperty() {
        expect(servletContext.getMajorVersion()).andReturn(0);
        System.setProperty(LocationConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM, "org/impalaframework/web/module/locations.properties");
        try {
            replay(servletContext);
            
            Properties properties = resolver.getProperties();
            assertFalse(properties.isEmpty());
            assertNotNull(properties.getProperty("bootstrapLocations"));
            
            verify(servletContext);
        }
        finally {
            System.clearProperty(LocationConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM);
        }
    }

    public final void testGetPropertiesLocationViaSystemPropertyNotFound() {
        expect(servletContext.getMajorVersion()).andReturn(0);
        System.setProperty(LocationConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM, "a location which does not exist");
        try {
            replay(servletContext);

            Properties properties = resolver.getProperties();
            assertTrue(properties.isEmpty());
            
            verify(servletContext);
        }
        finally {
            System.clearProperty(LocationConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM);
        }
    }

}
