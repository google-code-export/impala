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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;

import org.impalaframework.web.servlet.ModuleHttpServletRequest;
import org.impalaframework.web.servlet.wrapper.RequestModuleMapping;

public class UrlPrefixRequestModuleMapperTest extends TestCase {
    
    private UrlPrefixRequestModuleMapper mapper;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mapper = new UrlPrefixRequestModuleMapper();
    }

    public void testGetModuleForURI() throws Exception {
        
        mapper.getPrefixTreeHolder().add("module1", "/m1", null, null);
        mapper.getPrefixTreeHolder().add("module1", "/m1a", null, null);
        mapper.getPrefixTreeHolder().add("module2", "/m2", null, null);
        
        assertEquals(new ModuleNameWithPath("module1"), mapper.getModuleForURI("/m1").getValue());
        assertEquals(new ModuleNameWithPath("module1"), mapper.getModuleForURI("/m1abbb").getValue());
        assertEquals(new ModuleNameWithPath("module2"), mapper.getModuleForURI("/m2").getValue());

        assertNull(mapper.getModuleForURI("/m"));
        assertNull(mapper.getModuleForURI("/m3"));
    }
    
    public void testExistingRequest() throws Exception {
        
        final ModuleHttpServletRequest request = createMock(ModuleHttpServletRequest.class);

        expect(request.getRequestURI()).andReturn("/context/servlet/path");
        expect(request.getContextPath()).andReturn("/context");
        final RequestModuleMapping mapping = new RequestModuleMapping("/context/servlet/path", "module", "/context", "/servlet");
        expect(request.getAttribute(UrlPrefixRequestModuleMapper.EXISTING_REQUEST_MODULE_MAPPING)).andReturn(mapping);
        expect(request.setReuse()).andReturn(true);
        
        replay(request);
        assertSame(mapping, mapper.getModuleForRequest(request));
        verify(request);
    }
    
    public void testNotExistingRequest() throws Exception {
        
        final HttpServletRequest request = createMock(HttpServletRequest.class);

        expect(request.getRequestURI()).andReturn("/context/servlet/path");
        expect(request.getContextPath()).andReturn("/context");
        
        replay(request);
        assertNull(mapper.getModuleForRequest(request));
        verify(request);
    }
    
    public void testNotModuleMapper() throws Exception {
        
        final ModuleHttpServletRequest request = createMock(ModuleHttpServletRequest.class);

        expect(request.getRequestURI()).andReturn("/context/servlet/path");
        expect(request.getContextPath()).andReturn("/context");
        expect(request.getAttribute(UrlPrefixRequestModuleMapper.EXISTING_REQUEST_MODULE_MAPPING)).andReturn(null);
        
        replay(request);
        assertNull(mapper.getModuleForRequest(request));
        verify(request);
    }
    
    public void testLifeCycle() throws Exception {
        
        ServletContext servletContext = createMock(ServletContext.class);
        mapper.setServletContext(servletContext);
        
        //called with afterPropertiesSet
        servletContext.setAttribute(UrlPrefixRequestModuleMapper.PREFIX_HOLDER_KEY, mapper.getPrefixTreeHolder());
        //called with destroy
        servletContext.removeAttribute(UrlPrefixRequestModuleMapper.PREFIX_HOLDER_KEY);
        
        replay(servletContext);
        
        mapper.afterPropertiesSet();
        mapper.destroy();
        
        verify(servletContext);        
    }
    
}
