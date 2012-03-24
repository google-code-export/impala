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

package org.impalaframework.web.spring.integration;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.module.spi.FrameworkLockHolder;
import org.impalaframework.web.WebConstants;
import org.impalaframework.web.integration.IntegrationServletConfig;
import org.impalaframework.web.spring.helper.FrameworkServletContextCreator;
import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;

public class ExternalFrameworkIntegrationServletTest extends TestCase {

    private ExternalFrameworkIntegrationServlet servlet;
    private ServletContext servletContext;
    private WebApplicationContext applicationContext;
    private HttpServlet delegateServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FrameworkServletContextCreator creator;
    private ModuleManagementFacade moduleManagementFacade;
    private FrameworkLockHolder frameworkLockHolder;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        servlet = new ExternalFrameworkIntegrationServlet(){
            private static final long serialVersionUID = 1L;

            @Override
            protected void publishContext(WebApplicationContext wac) {}

            @Override
            protected void initFrameworkServlet() throws ServletException, BeansException {}            
        };
        
        servletContext = createMock(ServletContext.class);
        applicationContext = createMock(WebApplicationContext.class);
        delegateServlet = createMock(HttpServlet.class);
        creator = createMock(FrameworkServletContextCreator.class);
        servlet.setFrameworkContextCreator(creator);
        
        request = createMock(HttpServletRequest.class);
        response = createMock(HttpServletResponse.class);
        moduleManagementFacade = createMock(ModuleManagementFacade.class);
        frameworkLockHolder = createMock(FrameworkLockHolder.class);
    }

    public void testService() throws ServletException, IOException {
        
        final String attributeName = WebConstants.IMPALA_FACTORY_ATTRIBUTE;
        expect(servletContext.getAttribute(attributeName)).andReturn(moduleManagementFacade);
        expect(moduleManagementFacade.getFrameworkLockHolder()).andReturn(frameworkLockHolder);
        frameworkLockHolder.writeLock();
        
        servletContext.log(isA(String.class));
        expect(creator.createWebApplicationContext()).andReturn(applicationContext);
        expect(applicationContext.getClassLoader()).andReturn(null);
        expect(applicationContext.getBean("myServletBeanName")).andReturn(delegateServlet);     
        frameworkLockHolder.writeUnlock();
        
        frameworkLockHolder.readLock();
        delegateServlet.service(request, response);
        frameworkLockHolder.readUnlock();
        
        replayMocks();
        HashMap<String, String> initParameters = new HashMap<String, String>();
        initParameters.put("delegateServletBeanName", "myServletBeanName");
        servlet.init(new IntegrationServletConfig(
                initParameters, servletContext, "myservlet"));
        servlet.doService(request, response);
        verifyMocks();
    }

    private void verifyMocks() {
        verify(servletContext);
        verify(applicationContext);
        verify(delegateServlet);
        verify(creator);
        verify(moduleManagementFacade);
        verify(frameworkLockHolder);
    }

    private void replayMocks() {
        replay(servletContext);
        replay(applicationContext);
        replay(delegateServlet);
        replay(creator);
        replay(moduleManagementFacade);
        replay(frameworkLockHolder);
    }
}
