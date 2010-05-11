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

package org.impalaframework.web.integration;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.module.spi.FrameworkLockHolder;
import org.impalaframework.util.ObjectMapUtils;
import org.impalaframework.web.WebConstants;
import org.impalaframework.web.servlet.invocation.ModuleHttpServiceInvoker;
import org.impalaframework.web.servlet.wrapper.RequestModuleMapping;

public class ModuleProxyServletTest extends TestCase {

    private ModuleProxyServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ServletContext servletContext;
    private HttpServlet delegateServlet;
    private ModuleManagementFacade moduleManagementFacade;
    private FrameworkLockHolder frameworkLockHolder;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        servlet = new ModuleProxyServlet() {

            private static final long serialVersionUID = 1L;

            @Override
            protected HttpServletRequest wrappedRequest(
                    HttpServletRequest request, ServletContext servletContext, RequestModuleMapping moduleMapping, String applicationId) {
                return request;
            }
            
        };
        
        request = createMock(HttpServletRequest.class);
        response = createMock(HttpServletResponse.class);
        servletContext = createMock(ServletContext.class);
        delegateServlet = createMock(HttpServlet.class);
        moduleManagementFacade = createMock(ModuleManagementFacade.class);
        frameworkLockHolder = createMock(FrameworkLockHolder.class);
    }    
    
    public void testDoFilterLocking() throws Exception {
        
        servlet = new ModuleProxyServlet(){

            private static final long serialVersionUID = 1L;

            @Override
            protected void doService(HttpServletRequest request,
                    HttpServletResponse response, 
                    ServletContext context)
                    throws ServletException, IOException {
            }
        };
        
        expectInit();
        
        frameworkLockHolder.readLock();
        frameworkLockHolder.readUnlock();
        
        replayMocks();
        
        initServlet();
        servlet.service(request, response);
        
        verifyMocks();
    }
    
    @SuppressWarnings("unchecked")
    public void testDoServiceWithModule() throws Exception {
        
        expectInit();
        
        expect(request.getRequestURI()).andStubReturn("/app/mymodule/resource.htm");
        expectGetInvoker("mymodule", new ModuleHttpServiceInvoker(new HashMap<String, List<Filter>>(), ObjectMapUtils.newMap("*", delegateServlet)));
        delegateServlet.service(request, response);
        
        replayMocks();

        initServlet();
        servlet.doService(request, response, servletContext);

        verifyMocks();
    }
    
    public void testDoServiceNoModule() throws Exception {

        expectInit();
        
        expect(request.getRequestURI()).andStubReturn("/app/mymodule/resource.htm");
        expectGetInvoker("mymodule", null);
        
        replayMocks();

        initServlet();
        servlet.doService(request, response, servletContext);

        verifyMocks();
    }
    
    public void testDoServiceWithDuffPath() throws Exception {

        expectInit();
        
        expect(request.getRequestURI()).andStubReturn("/duff");
        replayMocks();

        initServlet();
        servlet.doService(request, response, servletContext);

        verifyMocks();
    }
    
    public void testWithDifferentMapper() throws Exception {

        expectInit();
        
        final HashMap<String, String> initParameters = new HashMap<String, String>();
        initParameters.put(WebConstants.REQUEST_MODULE_MAPPER_CLASS_NAME, TestMapper.class.getName());
        
        //this method will eb called on TestMapper
        expect(request.getParameter("moduleName")).andReturn("alternativemodule");
        
        replayMocks();
        
        servlet.init(new IntegrationServletConfig(initParameters, servletContext, "proxyServlet"));
        assertEquals(new RequestModuleMapping("/alternativemodule", "alternativemodule", null, null), servlet.getModuleMapping(request));

        verifyMocks();
    }

    private void initServlet() throws ServletException {
        servlet.init(new IntegrationServletConfig(new HashMap<String, String>(), servletContext, "proxyServlet"));
    }

    private void expectInit() throws ServletException {
        expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(moduleManagementFacade);
        expect(moduleManagementFacade.getFrameworkLockHolder()).andReturn(frameworkLockHolder);
    }

    private void verifyMocks() {
        verify(request);
        verify(response);
        verify(servletContext);
        verify(delegateServlet);
        verify(moduleManagementFacade);
        verify(frameworkLockHolder);
    }

    private void replayMocks() {
        replay(request);
        replay(response);
        replay(servletContext);
        replay(delegateServlet);
        replay(moduleManagementFacade);
        replay(frameworkLockHolder);
    }

    private void expectGetInvoker(String path, Object o) {
        expect(servletContext.getAttribute(ModuleHttpServiceInvoker.class.getName()+"."+path)).andReturn(o);
    }

}
