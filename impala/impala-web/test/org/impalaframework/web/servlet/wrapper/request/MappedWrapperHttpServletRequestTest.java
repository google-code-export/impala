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

package org.impalaframework.web.servlet.wrapper.request;

import static org.easymock.EasyMock.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.impalaframework.web.servlet.wrapper.RequestModuleMapping;
import org.impalaframework.web.servlet.wrapper.request.MappedHttpServletRequest;
import org.impalaframework.web.servlet.wrapper.session.IdentityHttpSessionWrapper;


import junit.framework.TestCase;

public class MappedWrapperHttpServletRequestTest extends TestCase {
    
    private HttpServletRequest request;
    private ServletContext servletContext;
    private IdentityHttpSessionWrapper httpSessionWrapper;
    private String applicationId;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        request = createMock(HttpServletRequest.class);
        servletContext = createMock(ServletContext.class);
        httpSessionWrapper = new IdentityHttpSessionWrapper();
        applicationId = "applicationId";
    }

    public void testWithServletPath() {
        
        expectConstruct();
        
        replay(request, servletContext);
        
        MappedHttpServletRequest wrapper = new MappedHttpServletRequest(servletContext, request, httpSessionWrapper, new RequestModuleMapping("/sp", "module", "/servletPath"), applicationId);
        assertEquals("/servletPath", wrapper.getServletPath());
        assertEquals("/extra/path/info", wrapper.getPathInfo());
        
        verify(request, servletContext);
    }
    
    public void testNoServletPath() {
        
        //note no expected calls in constructor
        
        expect(request.getServletPath()).andReturn("/sp");
        expect(request.getPathInfo()).andReturn("/pi");
        expect(request.getPathTranslated()).andReturn("/pt");
        
        replay(request, servletContext);
        
        MappedHttpServletRequest wrapper = new MappedHttpServletRequest(servletContext, request, httpSessionWrapper, null, applicationId);
        assertEquals("/sp", wrapper.getServletPath());
        assertEquals("/pi", wrapper.getPathInfo());
        assertEquals("/pt", wrapper.getPathTranslated());
        
        verify(request, servletContext);
    }
    
    public void testUnexpectedServletPath() {

        expectConstruct();
        
        expect(request.getServletPath()).andReturn("/sp");
        expect(request.getPathInfo()).andReturn("/pi");
        expect(request.getPathTranslated()).andReturn("/pt");
        
        replay(request, servletContext);
        
        MappedHttpServletRequest wrapper = new MappedHttpServletRequest(servletContext, request, httpSessionWrapper, new RequestModuleMapping("/sp", "module", "/unexpectedServletPath"), applicationId);
        assertEquals("/sp", wrapper.getServletPath());
        assertEquals("/pi", wrapper.getPathInfo());
        assertEquals("/pt", wrapper.getPathTranslated());
        
        verify(request, servletContext);
    }
    
    public void testWithGetPathTranslated() {
        
        expectConstruct();
        
        expect(servletContext.getRealPath("/extra/path/info")).andReturn("/realpath");
        
        replay(request, servletContext);
        
        MappedHttpServletRequest wrapper = new MappedHttpServletRequest(servletContext, request, httpSessionWrapper, new RequestModuleMapping("/sp", "module", "/servletPath"), applicationId);
        assertEquals("/realpath", wrapper.getPathTranslated());
        
        verify(request, servletContext);
    }

    private void expectConstruct() {
        expect(request.getContextPath()).andReturn("/app");
        expect(request.getRequestURI()).andReturn("/app/servletPath/extra/path/info");
    }

}
