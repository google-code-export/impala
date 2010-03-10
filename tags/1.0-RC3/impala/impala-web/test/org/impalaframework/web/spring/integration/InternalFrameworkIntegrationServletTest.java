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

package org.impalaframework.web.spring.integration;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.impalaframework.web.integration.IntegrationServletConfig;
import org.springframework.web.context.WebApplicationContext;

public class InternalFrameworkIntegrationServletTest extends TestCase {

    private InternalFrameworkIntegrationServlet servlet;
    private ServletContext servletContext;
    private WebApplicationContext applicationContext;
    private HttpServlet delegateServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        servlet = new InternalFrameworkIntegrationServlet();
        servletContext = createMock(ServletContext.class);
        applicationContext = createMock(WebApplicationContext.class);
        delegateServlet = createMock(HttpServlet.class);
        servlet.setApplicationContext(applicationContext);
        servlet.setDelegateServlet(delegateServlet);
        
        request = createMock(HttpServletRequest.class);
        response = createMock(HttpServletResponse.class);
    }

    public void testInitDestroy() throws ServletException {

        replayMocks();
        servlet.init(new IntegrationServletConfig(new HashMap<String, String>(), servletContext, "myservlet"));
        servlet.destroy();
        verifyMocks();
    }

    public void testService() throws ServletException, IOException {
        expect(applicationContext.getClassLoader()).andReturn(null);
        delegateServlet.service(request, response);

        replayMocks();
        servlet.service(request, response);
        verifyMocks();
    }

    private void verifyMocks() {
        verify(servletContext);
        verify(applicationContext);
        verify(delegateServlet);
    }

    private void replayMocks() {
        replay(servletContext);
        replay(applicationContext);
        replay(delegateServlet);
    }
}
