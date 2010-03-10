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

package org.impalaframework.web.spring.helper;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;

import java.util.HashMap;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.web.AttributeServletContext;
import org.impalaframework.web.integration.IntegrationServletConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.FrameworkServlet;

public class ImpalaServletUtilsTest extends TestCase {
    
    private ServletContext servletContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        servletContext = new AttributeServletContext();
    }

    public void testCheckIsWebApplicationContext() {
        ImpalaServletUtils.checkIsWebApplicationContext("myservlet", createMock(WebApplicationContext.class));
        try {
            ImpalaServletUtils.checkIsWebApplicationContext("myservlet", createMock(ApplicationContext.class));
        } catch (ConfigurationException e) {
            assertEquals("Servlet 'myservlet' is not backed by an application context of type org.springframework.web.context.WebApplicationContext: EasyMock for interface org.springframework.context.ApplicationContext", e.getMessage());
        }
    }

    public void testApplicationContext() throws Exception {

        final FrameworkServlet frameworkServlet = createMock(FrameworkServlet.class);
        expect(frameworkServlet.getServletContextAttributeName()).andStubReturn("attName");
        expect(frameworkServlet.getServletConfig()).andStubReturn(new IntegrationServletConfig(new HashMap<String, String>(), servletContext, "myservlet" ));
        replay(frameworkServlet);
        
        final WebApplicationContext applicationContext = createMock(WebApplicationContext.class);
        ImpalaServletUtils.publishWebApplicationContext(applicationContext, frameworkServlet);
        
        assertSame(applicationContext, servletContext.getAttribute("attName"));
        
        ImpalaServletUtils.unpublishWebApplicationContext(frameworkServlet);
        assertNull(servletContext.getAttribute("attName"));
    }

}
