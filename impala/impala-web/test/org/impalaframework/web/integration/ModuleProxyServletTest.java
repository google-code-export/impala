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

import java.util.HashMap;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

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
        servlet.init(new IntegrationServletConfig(new HashMap<String, String>(), servletContext, "proxyServlet"));
    }
    
    @SuppressWarnings("unchecked")
    public void testDoServiceWithModule() throws Exception {
        
        expect(request.getRequestURI()).andStubReturn("/app/mymodule/resource.htm");
        expectGetInvoker("mymodule", new ModuleHttpServiceInvoker(new HashMap<String, List<Filter>>(), ObjectMapUtils.newMap("*", delegateServlet)));
        delegateServlet.service(request, response);
        
        replayMocks();
        
        servlet.doService(request, response, servletContext);

        verifyMocks();
    }
    
    public void testDoServiceNoModule() throws Exception {

        expect(request.getRequestURI()).andStubReturn("/app/mymodule/resource.htm");
        expectGetInvoker("mymodule", null);
        
        replayMocks();
        
        servlet.doService(request, response, servletContext);

        verifyMocks();
    }
    
    public void testDoServiceWithDuffPath() throws Exception {
        
        expect(request.getRequestURI()).andStubReturn("/duff");
        replayMocks();
        
        servlet.doService(request, response, servletContext);

        verifyMocks();
    }
    
    public void testWithDifferentMapper() throws Exception {

        final HashMap<String, String> initParameters = new HashMap<String, String>();
        initParameters.put(WebConstants.REQUEST_MODULE_MAPPER_CLASS_NAME, TestMapper.class.getName());
        
        servlet.init(new IntegrationServletConfig(initParameters, servletContext, "proxyServlet"));
        
        //this method will eb called on TestMapper
        expect(request.getParameter("moduleName")).andReturn("alternativemodule");
        
        replayMocks();
        
        assertEquals(new RequestModuleMapping("/alternativemodule", "alternativemodule", null), servlet.getModuleMapping(request));

        verifyMocks();
    }

    private void verifyMocks() {
        verify(request);
        verify(response);
        verify(servletContext);
        verify(delegateServlet);
    }

    private void replayMocks() {
        replay(request);
        replay(response);
        replay(servletContext);
        replay(delegateServlet);
    }

    private void expectGetInvoker(String path, Object o) {
        expect(servletContext.getAttribute(ModuleHttpServiceInvoker.class.getName()+"."+path)).andReturn(o);
    }

}
