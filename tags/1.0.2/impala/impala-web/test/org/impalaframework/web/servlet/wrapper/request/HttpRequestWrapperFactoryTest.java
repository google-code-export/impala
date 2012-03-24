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

package org.impalaframework.web.servlet.wrapper.request;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.impalaframework.util.ReflectionUtils;
import org.impalaframework.web.servlet.ModuleHttpServletRequest;
import org.impalaframework.web.servlet.qualifier.WebAttributeQualifier;
import org.impalaframework.web.servlet.wrapper.HttpSessionWrapper;
import org.impalaframework.web.servlet.wrapper.RequestModuleMapping;
import org.impalaframework.web.servlet.wrapper.session.IdentityHttpSessionWrapper;
import org.impalaframework.web.servlet.wrapper.session.PartitionedHttpSessionWrapper;

public class HttpRequestWrapperFactoryTest extends TestCase {
    
    private RequestModuleMapping moduleMapping;
    private String applicationId;
    private ServletContext servletContext;
    private WebAttributeQualifier webAttributeQualifier;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        moduleMapping = new RequestModuleMapping("/myModule", "myModule", null, null);
        applicationId = "applicationId";
        
        servletContext = EasyMock.createMock(ServletContext.class);
        webAttributeQualifier = EasyMock.createMock(WebAttributeQualifier.class);
    }

    public void testIdentityWrapper() {
        IdentityHttpRequestWrapper factory = new IdentityHttpRequestWrapper();
        final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        assertSame(request, factory.getWrappedRequest(request, null, null, applicationId));
        
        assertNull(factory.getWrappedRequest(null, null, null, applicationId));
    }
    
    public void testModuleWrapper() {
        PartitionedRequestWrapper factory = new PartitionedRequestWrapper();
        final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        
        factory.setWebAttributeQualifier(webAttributeQualifier);
        
        expectWrappedRequest(request);
        
        replay(webAttributeQualifier, servletContext, request);
        
        HttpServletRequest mappedRequest = factory.getWrappedRequest(request, servletContext, moduleMapping, applicationId);
        assertTrue(mappedRequest instanceof MappedHttpServletRequest);
        MappedHttpServletRequest mr = (MappedHttpServletRequest) mappedRequest;
        assertSame(request, mr.getRequest());
        assertTrue(ReflectionUtils.getFieldValue(mr, "httpSessionWrapper", HttpSessionWrapper.class) instanceof IdentityHttpSessionWrapper);
        
        factory.setEnableModuleSessionProtection(true);
        final HttpServletRequest wrappedRequest = factory.getWrappedRequest(request, servletContext, moduleMapping, applicationId);
        assertTrue(wrappedRequest instanceof MappedHttpServletRequest);
        assertTrue(ReflectionUtils.getFieldValue(wrappedRequest, "httpSessionWrapper", HttpSessionWrapper.class) instanceof PartitionedHttpSessionWrapper);
        
        verify(webAttributeQualifier, servletContext, request);
    }
    
    public void testReuseModuleWrapper() {
        PartitionedRequestWrapper factory = new PartitionedRequestWrapper();
        final ModuleHttpServletRequest request = EasyMock.createMock(ModuleHttpServletRequest.class);
        expect(request.isReuse()).andReturn(true);
        
        replay(request);
        
        assertSame(request, factory.getWrappedRequest(request, servletContext, moduleMapping, applicationId));
        
        verify(request);
    }
    
    public void testNotReuseModuleWrapper() {
        PartitionedRequestWrapper factory = new PartitionedRequestWrapper();
        factory.setWebAttributeQualifier(webAttributeQualifier);
        final ModuleHttpServletRequest request = EasyMock.createMock(ModuleHttpServletRequest.class);
        expect(request.isReuse()).andReturn(false);
        
        expectWrappedRequest(request);
        
        replay(request, webAttributeQualifier);
        
        assertFalse(request == factory.getWrappedRequest(request, servletContext, moduleMapping, applicationId));
    }

    private void expectWrappedRequest(final HttpServletRequest request) {
        expect(webAttributeQualifier.getQualifiedAttributeName("wrapped_servlet_context", applicationId, "myModule")).andReturn("wrapped_context").times(2);
        expect(webAttributeQualifier.getQualifierPrefix(applicationId, "myModule")).andReturn("myprefix").times(2);
        request.setAttribute("org.impalaframework.web.servlet.qualifier.WebAttributeQualifierMODULE_QUALIFIER_PREFIX", "myprefix");
        expectLastCall().times(2);
        expect(servletContext.getAttribute("wrapped_context")).andReturn(null).times(2);
    }

}
