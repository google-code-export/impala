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

package org.impalaframework.web.resource;

import javax.servlet.ServletContext;

import org.springframework.core.io.Resource;
import org.springframework.web.context.support.ServletContextResource;

import static org.easymock.EasyMock.*;

import junit.framework.TestCase;

public class ServletContextResourceLoaderTest extends TestCase {

    private ServletContextResourceLoader resourceLoader;

    private ServletContext servletContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        resourceLoader = new ServletContextResourceLoader();
        servletContext = createMock(ServletContext.class);
        resourceLoader.setServletContext(servletContext);
    }

    public final void testGetResourceForPath() {        
        replay(servletContext);
        Resource resource = resourceLoader.getResource("/mypath", null);
        assertTrue(resource instanceof ServletContextResource);
        ServletContextResource r = (ServletContextResource)resource;
        assertSame(servletContext, r.getServletContext());
        verify(servletContext);
    }

}
