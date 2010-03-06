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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.impalaframework.module.definition.SimpleModuleDefinition;

public class ServletFactoryBeanTest extends TestCase {

    private HttpServletRequest request;
    private ServletContext context;
    private ServletFactoryBean factoryBean;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        request = createMock(HttpServletRequest.class);
        context = createMock(ServletContext.class);
        factoryBean = new ServletFactoryBean();
        factoryBean.setInitParameters(null);
        factoryBean.setServletName("myservlet");
        factoryBean.setServletClass(TestServlet.class);
        factoryBean.setServletContext(context);
    }
    
    public void testGetObject() throws Exception {
        
        factoryBean.afterPropertiesSet();
        TestServlet servlet = (TestServlet) factoryBean.getObject();
        
        expect(request.getRequestURI()).andStubReturn("/app/somepath/morebits");
        replay(request);
        replay(context);
        
        servlet.service(request, null);
        
        verify(request);
        verify(context);
    }
    
    public void testNoServletName() throws Exception {
        
        factoryBean.setServletName(null);
        factoryBean.setModuleDefinition(new SimpleModuleDefinition("mymodule"));
        factoryBean.afterPropertiesSet();
        TestServlet servlet = (TestServlet) factoryBean.getObject();
        
        expect(request.getRequestURI()).andStubReturn("/app/somepath/morebits");
        
        replay(request);
        replay(context);
        
        servlet.service(request, null);
        
        verify(request);
        verify(context);
    }
    
    public void testWithPrefix() throws Exception {
        
        factoryBean.setInitParameters(Collections.singletonMap("modulePrefix", "pathprefix-"));
        factoryBean.afterPropertiesSet();
        TestServlet servlet = (TestServlet) factoryBean.getObject();
        
        expect(request.getRequestURI()).andStubReturn("/app/somepath/morebits");
        replay(request);
        replay(context);
        
        servlet.service(request, null);
        
        verify(request);
        verify(context);
    }

}

class TestServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestURI();
    }
    
}
