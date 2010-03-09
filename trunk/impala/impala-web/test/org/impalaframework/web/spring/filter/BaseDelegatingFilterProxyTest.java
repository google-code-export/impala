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

package org.impalaframework.web.spring.filter;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.module.application.SimpleApplicationManager;
import org.impalaframework.module.spi.Application;
import org.impalaframework.web.AttributeServletContext;
import org.impalaframework.web.WebConstants;
import org.impalaframework.web.servlet.qualifier.DefaultWebAttributeQualifier;
import org.impalaframework.web.servlet.qualifier.WebAttributeQualifier;
import org.springframework.web.context.WebApplicationContext;

public class BaseDelegatingFilterProxyTest extends TestCase {
    
    private AttributeServletContext servletContext;
    private ModuleManagementFacade facade;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain chain;
    private Filter filter;
    private Application application;
    private WebApplicationContext applicationContext;
    private DefaultWebAttributeQualifier webAttributeQualifier;
    private SimpleApplicationManager applicationManager;
    private TestDelegatingFilter testFilter;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        servletContext = new AttributeServletContext();
        facade = createMock(ModuleManagementFacade.class);
        request = createMock(HttpServletRequest.class);
        response = createMock(HttpServletResponse.class);
        chain = createMock(FilterChain.class);
        filter = createMock(Filter.class);
        application = createMock(Application.class);
        applicationContext = createMock(WebApplicationContext.class);
        webAttributeQualifier = new DefaultWebAttributeQualifier();
        
        applicationManager = new SimpleApplicationManager(){
            @Override
            public Application getCurrentApplication() {
                return application;
            }
        };        
        
        testFilter = new TestDelegatingFilter();
        testFilter.setTargetModuleName("module");
        testFilter.setServletContext(servletContext);
    }

    public void testDoFilter() throws Exception {
        
        servletContext.setAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE, facade);        
        expect(facade.getBean("webAttributeQualifier", WebAttributeQualifier.class)).andReturn(webAttributeQualifier);
        servletContext.setAttribute(webAttributeQualifier.getQualifiedAttributeName(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, "", "module"), applicationContext);
       
        expect(applicationContext.getBean("bean", Filter.class)).andReturn(filter);
        
        expect(facade.getApplicationManager()).andReturn(applicationManager);
        expect(application.getId()).andReturn("");
        filter.doFilter(request, response, chain);
        
        replay(facade, request, response, chain, application, applicationContext, filter);
        
        testFilter.doFilter(request, response, chain);
        
        verify(facade, request, response, chain, application, applicationContext, filter);
    }
    
    public void testNoFacade() throws Exception {
        
        servletContext.setAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE, null);
        
        replay(facade, request, response, chain, application, applicationContext, filter);
        
        try {
            testFilter.doFilter(request, response, chain);
            fail();
        }
        catch (ConfigurationException e) {
        }
        
        verify(facade, request, response, chain, application, applicationContext, filter);
    }
    
    public void testNoApplicationContext() throws Exception {
        
        servletContext.setAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE, facade);
        expect(facade.getBean("webAttributeQualifier", WebAttributeQualifier.class)).andReturn(webAttributeQualifier);        
        expect(facade.getApplicationManager()).andReturn(applicationManager);
        expect(application.getId()).andReturn("");
        
        replay(facade, request, response, chain, application, applicationContext, filter);
        
        try {
            testFilter.doFilter(request, response, chain);
            fail();
        }
        catch (ConfigurationException e) {
            assertEquals("No root web application context associated with module 'module'", e.getMessage());
        }
        
        verify(facade, request, response, chain, application, applicationContext, filter);
    }    
    
    public void testNotFilter() throws Exception {
        
        servletContext.setAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE, facade);        
        expect(facade.getBean("webAttributeQualifier", WebAttributeQualifier.class)).andReturn(webAttributeQualifier);
        servletContext.setAttribute(webAttributeQualifier.getQualifiedAttributeName(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, "", "module"), applicationContext);
       
        expect(applicationContext.getBean("bean", Filter.class)).andReturn(request);
        
        expect(facade.getApplicationManager()).andReturn(applicationManager);
        expect(application.getId()).andReturn("");
        
        replay(facade, request, response, chain, application, applicationContext, filter);
        
        try {
            testFilter.doFilter(request, response, chain);
            fail();
        }
        catch (ConfigurationException e) {
            assertEquals("Delegate bean 'bean' from module 'module' is not an instance of javax.servlet.Filter", e.getMessage());
        }
        
        verify(facade, request, response, chain, application, applicationContext, filter);
    }
    

}

class TestDelegatingFilter extends BaseDelegatingFilterProxy {
    @Override
    protected String getBeanName(ServletRequest request) {
        return "bean";
    }
}
