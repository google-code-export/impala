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

package org.impalaframework.web.servlet.wrapper;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.impalaframework.util.ReflectionUtils;

public class HttpRequestWrapperFactoryTest extends TestCase {
    
    private RequestModuleMapping moduleMapping;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        moduleMapping = new RequestModuleMapping("/myModule", "myModule", null);
    }

    public void testIdentityWrapper() {
        IdentityHttpRequestWrapperFactory factory = new IdentityHttpRequestWrapperFactory();
        final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        assertSame(request, factory.getWrappedRequest(request, null, null));
        
        assertNull(factory.getWrappedRequest(null, null, null));
    }
    
    public void testModuleWrapper() {
        ModuleAwareRequestWrapperFactory factory = new ModuleAwareRequestWrapperFactory();
        final HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        final ServletContext servletContext = EasyMock.createMock(ServletContext.class);
        
        HttpServletRequest mappedRequest = factory.getWrappedRequest(request, servletContext, moduleMapping);
        assertTrue(mappedRequest instanceof MappedWrapperHttpServletRequest);
        MappedWrapperHttpServletRequest mr = (MappedWrapperHttpServletRequest) mappedRequest;
        assertSame(request, mr.getRequest());
        assertTrue(ReflectionUtils.getFieldValue(mr, "httpSessionWrapper", HttpSessionWrapper.class) instanceof IdentityHttpSessionWrapper);
        
        factory.setEnableModuleSessionProtection(true);
        final HttpServletRequest wrappedRequest = factory.getWrappedRequest(request, servletContext, moduleMapping);
        assertTrue(wrappedRequest instanceof MappedWrapperHttpServletRequest);
        assertTrue(ReflectionUtils.getFieldValue(wrappedRequest, "httpSessionWrapper", HttpSessionWrapper.class) instanceof ModuleAwareHttpSessionWrapper);
    }

}
