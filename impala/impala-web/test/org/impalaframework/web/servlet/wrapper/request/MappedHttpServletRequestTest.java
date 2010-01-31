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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import junit.framework.TestCase;

import org.impalaframework.web.servlet.wrapper.CacheableHttpSession;
import org.impalaframework.web.servlet.wrapper.HttpSessionWrapper;
import org.impalaframework.web.servlet.wrapper.RequestModuleMapping;
import org.impalaframework.web.servlet.wrapper.session.IdentityHttpSessionWrapper;

public class MappedHttpServletRequestTest extends TestCase {
    
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
    
    public void testGetWrappedSessionNull() throws Exception {
        
        final HttpSessionWrapper sessionWrapper = createMock(HttpSessionWrapper.class);
        final HttpServletRequest request = createMock(HttpServletRequest.class);
        
        expect(request.getAttribute(MappedHttpServletRequest.class.getName()+".WRAPPED_SESSION")).andReturn(null);
        
        replay(request, sessionWrapper);
        
        MappedHttpServletRequest wrapper = new MappedHttpServletRequest(servletContext, request, sessionWrapper, null, applicationId);

        assertNull(wrapper.getCachedSession());
        
        verify(request, sessionWrapper);       
    }
    
    public void testGetWrappedSession() throws Exception {
        
        final HttpSessionWrapper sessionWrapper = createMock(HttpSessionWrapper.class);
        final HttpServletRequest request = createMock(HttpServletRequest.class);
        final HttpSession session = createMock(HttpSession.class);
        
        expect(request.getAttribute(MappedHttpServletRequest.class.getName()+".WRAPPED_SESSION")).andReturn(session);
        
        replay(request, sessionWrapper);
        
        MappedHttpServletRequest wrapper = new MappedHttpServletRequest(servletContext, request, sessionWrapper, null, applicationId);

        assertNull(wrapper.getCachedSession());
        
        verify(request, sessionWrapper);       
    }
    
    public void testGetWrappedCacheableSessionValid() throws Exception {
        
        final HttpSessionWrapper sessionWrapper = createMock(HttpSessionWrapper.class);
        final HttpServletRequest request = createMock(HttpServletRequest.class);
        final CacheableHttpSession session = createMock(CacheableHttpSession.class);
        
        expect(request.getAttribute(MappedHttpServletRequest.class.getName()+".WRAPPED_SESSION")).andReturn(session);
        expect(session.isValid()).andReturn(true);
        
        replay(request, sessionWrapper, session);
        
        MappedHttpServletRequest wrapper = new MappedHttpServletRequest(servletContext, request, sessionWrapper, null, applicationId);

        assertSame(session, wrapper.getCachedSession());
        
        verify(request, sessionWrapper, session);       
    }
    
    public void testGetWrappedCacheableSessionInvalid() throws Exception {
        
        final HttpSessionWrapper sessionWrapper = createMock(HttpSessionWrapper.class);
        final HttpServletRequest request = createMock(HttpServletRequest.class);
        final CacheableHttpSession session = createMock(CacheableHttpSession.class);
        
        final String attribute = MappedHttpServletRequest.class.getName()+".WRAPPED_SESSION";
        expect(request.getAttribute(attribute)).andReturn(session);
        expect(session.isValid()).andReturn(false);
        request.removeAttribute(attribute);
        
        replay(request, sessionWrapper, session);
        
        MappedHttpServletRequest wrapper = new MappedHttpServletRequest(servletContext, request, sessionWrapper, null, applicationId);

        assertNull(wrapper.getCachedSession());
        
        verify(request, sessionWrapper, session);       
    }
    
    public void testMaybeCacheTrue() throws Exception {

        final CacheableHttpSession session = createMock(CacheableHttpSession.class);
        
        final String attribute = MappedHttpServletRequest.class.getName()+".WRAPPED_SESSION";
        request.setAttribute(attribute, session);
        
        replay(request, session);
        
        MappedHttpServletRequest wrapper = new MappedHttpServletRequest(servletContext, request, httpSessionWrapper, null, applicationId);

        wrapper.maybeCacheSession(session);
        
        verify(request, session);       
    }

    public void testMaybeCacheFalse() throws Exception {

        final HttpSession session = createMock(HttpSession.class);
        
        replay(request, session);
        
        MappedHttpServletRequest wrapper = new MappedHttpServletRequest(servletContext, request, httpSessionWrapper, null, applicationId);

        wrapper.maybeCacheSession(session);
        
        verify(request, session);       
    }
    private void expectConstruct() {
        expect(request.getContextPath()).andReturn("/app");
        expect(request.getRequestURI()).andReturn("/app/servletPath/extra/path/info");
    }

}
