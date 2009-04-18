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

package org.impalaframework.web.spring.helper;

import static org.easymock.classextension.EasyMock.createMock;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import junit.framework.TestCase;

import org.impalaframework.web.AttributeServletContext;
import org.impalaframework.web.helper.WebServletUtils;

public class WebServletUtilsTest extends TestCase {
    
    private ServletContext servletContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        servletContext = new AttributeServletContext();
    }
    
    public void testGetModuleServletContextKey() throws Exception {
        assertEquals("module_moduleName:attributeName", WebServletUtils.getModuleServletContextKey("moduleName", "attributeName"));
    }

    public void testPublishFilter() throws Exception {
        final Filter filter = createMock(Filter.class);
        WebServletUtils.publishFilter(servletContext, "myfilter", filter);
        
        assertSame(filter, WebServletUtils.getModuleFilter(servletContext, "myfilter"));
        
        WebServletUtils.unpublishFilter(servletContext, "myfilter");
        assertNull(WebServletUtils.getModuleFilter(servletContext, "myfilter"));
    }
    
    public void testPublishServlet() throws Exception {
        final HttpServlet servlet = createMock(HttpServlet.class);
        WebServletUtils.publishServlet(servletContext, "myservlet", servlet);
        
        assertSame(servlet, WebServletUtils.getModuleServlet(servletContext, "myservlet"));
        
        WebServletUtils.unpublishServlet(servletContext, "myservlet");
        assertNull(WebServletUtils.getModuleServlet(servletContext, "myservlet"));
    }

}
