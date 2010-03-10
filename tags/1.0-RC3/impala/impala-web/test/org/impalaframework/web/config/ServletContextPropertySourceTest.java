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

package org.impalaframework.web.config;

import javax.servlet.ServletContext;

import static org.easymock.EasyMock.*;

import junit.framework.TestCase;

public class ServletContextPropertySourceTest extends TestCase {

    private ServletContext servletContext;
    private ServletContextPropertySource source;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        servletContext = createMock(ServletContext.class);
        source = new ServletContextPropertySource(servletContext);
    }
    
    public void testNull() throws Exception {
        
        expect(servletContext.getInitParameter("name")).andReturn(null);
        replay(servletContext);
        
        assertNull(source.getValue("name"));
        
        verify(servletContext);
    }

    public void testNotNull() throws Exception {
        
        expect(servletContext.getInitParameter("name")).andReturn("value");
        replay(servletContext);
        
        assertEquals("value", source.getValue("name"));
        
        verify(servletContext);
    }
}
