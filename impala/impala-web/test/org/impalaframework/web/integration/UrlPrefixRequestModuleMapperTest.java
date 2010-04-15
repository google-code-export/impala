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

import static org.easymock.EasyMock.*;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

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
