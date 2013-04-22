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

package org.impalaframework.web.spring.loader;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.impalaframework.constants.LocationConstants;
import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.web.WebConstants;
import org.impalaframework.web.module.source.InternalWebXmlModuleDefinitionSource;
import org.impalaframework.web.servlet.invocation.ModuleHttpServiceInvoker;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.LiveBeansView;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.context.support.ServletContextScope;
import org.springframework.web.context.support.XmlWebApplicationContext;

public class ContextLoaderIntegrationTest extends TestCase {

    private ServletContext servletContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        servletContext = createMock(ServletContext.class);
        System.clearProperty(LocationConstants.BOOTSTRAP_LOCATIONS_PROPERTY_PARAM);
        System.clearProperty(LocationConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM);
        System.clearProperty(LocationConstants.BOOTSTRAP_MODULES_RESOURCE_PARAM);
    }

   
    public void testLoadBootstrap() throws Exception {

    	expect(servletContext.getInitParameter("contextId")).andReturn("id");
    	expect(servletContext.getInitParameter("locatorFactorySelector")).andReturn(null);
    	expect(servletContext.getInitParameter("parentContextKey")).andReturn(null);
    	
        expect(servletContext.getInitParameter("contextClass")).andReturn(null);
        expect(servletContext.getInitParameter("contextConfigLocation")).andReturn(null);
        expect(servletContext.getInitParameter("contextConfigLocation")).andReturn(null);
        expect(servletContext.getResource("/WEB-INF/applicationContext.xml")).andReturn(null);
        
        expectSpring32Support();
        
        replay(servletContext);

        ExternalModuleContextLoader loader = new ExternalModuleContextLoader() {

            @Override
            public String[] getBootstrapContextLocations(ServletContext servletContext) {
                String[] locations = new String[] { 
                        "META-INF/impala-bootstrap.xml",
                        "META-INF/impala-web-bootstrap.xml"};
                return locations;
            }
            
        };

        final GenericWebApplicationContext parent = newContext(false);
        WebApplicationContext context = loader.createWebApplicationContext(servletContext, parent);
        
        assertNotNull(context);
        assertTrue(context instanceof XmlWebApplicationContext);
        
        verify(servletContext);
    }
       
    public void testXmlBasedContextLoader() throws Exception {
        
        final String[] locations = new String[] { 
                "META-INF/impala-bootstrap.xml",
                "META-INF/impala-web-bootstrap.xml",
                "META-INF/impala-web-jmx-bootstrap.xml",
                "META-INF/impala-web-listener-bootstrap.xml"};

        expectSpring32Support();
        
        expect(servletContext.getMajorVersion()).andReturn(0);
        expect(servletContext.getInitParameter(LocationConstants.BOOTSTRAP_MODULES_RESOURCE_PARAM)).andReturn("xmlspec/xmlspec.xml");
        servletContext.setAttribute(eq(WebConstants.IMPALA_FACTORY_ATTRIBUTE), isA(ModuleManagementFacade.class));      
        servletContext.setAttribute(eq(WebConstants.MODULE_DEFINITION_SOURCE_ATTRIBUTE), isA(InternalWebXmlModuleDefinitionSource.class));
        expect(servletContext.getAttribute("org.springframework.web.context.WebApplicationContext.ROOT")).andReturn(newContext(true));
        servletContext.setAttribute(eq("application__module_impala-core:wrapped_servlet_context"), isA(ServletContext.class));
        servletContext.setAttribute(eq("application__module_impala-core:org.springframework.web.context.WebApplicationContext.ROOT"), isA(WebApplicationContext.class));  
        servletContext.setAttribute(eq("org.impalaframework.web.servlet.invocation.ModuleHttpServiceInvoker.impala-core"), isA(ModuleHttpServiceInvoker.class));
        
        replay(servletContext);
        
        ExternalModuleContextLoader loader = new ExternalModuleContextLoader() {
        
            public String[] getBootstrapContextLocations(ServletContext servletContext) {
                return locations;
            }
            
        };
        final GenericWebApplicationContext parent = newContext(true);
        
        ConfigurableApplicationContext context = loader.initImpalaApplicationContext(servletContext, parent);
        
        assertNotNull(context);
        assertTrue(context instanceof GenericWebApplicationContext);
        verify(servletContext);
    }


    private GenericWebApplicationContext newContext(boolean refresh) {
        final GenericWebApplicationContext value = new GenericWebApplicationContext();
        if (refresh) {
        	value.refresh();
        }
        return value;
    }
       
    public void testInitImpalaContext() throws Exception {
        
        final String[] locations = new String[] { 
                "META-INF/impala-bootstrap.xml",
                "META-INF/impala-web-bootstrap.xml"};

        expectSpring32Support();
        
        expect(servletContext.getMajorVersion()).andReturn(0);
        expect(servletContext.getInitParameter(LocationConstants.BOOTSTRAP_MODULES_RESOURCE_PARAM)).andReturn("xmlspec/xmlspec.xml");
        servletContext.setAttribute(eq(WebConstants.IMPALA_FACTORY_ATTRIBUTE), isA(ModuleManagementFacade.class));      
        servletContext.setAttribute(eq(WebConstants.MODULE_DEFINITION_SOURCE_ATTRIBUTE), isA(InternalWebXmlModuleDefinitionSource.class));
        expect(servletContext.getAttribute("org.springframework.web.context.WebApplicationContext.ROOT")).andReturn(newContext(true));
        servletContext.setAttribute(eq("application__module_impala-core:wrapped_servlet_context"), isA(ServletContext.class));
        servletContext.setAttribute(eq("application__module_impala-core:org.springframework.web.context.WebApplicationContext.ROOT"), isA(WebApplicationContext.class));  
        servletContext.setAttribute(eq("org.impalaframework.web.servlet.invocation.ModuleHttpServiceInvoker.impala-core"), isA(ModuleHttpServiceInvoker.class));
        
        replay(servletContext);
        
        ExternalModuleContextLoader loader = new ExternalModuleContextLoader() {
        
            public String[] getBootstrapContextLocations(ServletContext servletContext) {
                return locations;
            }
            
        };
        final GenericWebApplicationContext parent = newContext(true);
        
        ConfigurableApplicationContext context = loader.initImpalaApplicationContext(servletContext, parent);
        
        assertNotNull(context);
        assertTrue(context instanceof GenericWebApplicationContext);
        verify(servletContext);
    }


	private void expectSpring32Support() {
		servletContext.setAttribute(isA(String.class), isA(ServletContextScope.class));
		expectLastCall().asStub();
        expect(servletContext.getInitParameterNames()).andStubReturn(Collections.enumeration(Collections.emptySet()));
        expect(servletContext.getAttributeNames()).andStubReturn(Collections.enumeration(Collections.emptySet()));
        expect(servletContext.getInitParameter(LiveBeansView.MBEAN_DOMAIN_PROPERTY_NAME)).andStubReturn(null);
        expect(servletContext.getInitParameter(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME)).andStubReturn(null);
        expect(servletContext.getInitParameter(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME)).andStubReturn(null);
	}
}
