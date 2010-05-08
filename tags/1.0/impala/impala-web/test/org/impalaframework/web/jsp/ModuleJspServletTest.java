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

package org.impalaframework.web.jsp;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;
import java.lang.reflect.Field;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.impalaframework.web.AttributeServletContext;
import org.springframework.util.ReflectionUtils;

public class ModuleJspServletTest extends TestCase {
    
    private ModuleJspServlet servlet;
    private AttributeServletContext servletContext;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpServlet delegate;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        servlet = new ModuleJspServlet();
        servletContext = new AttributeServletContext();
        
        delegate = new HttpServlet() {

            private static final long serialVersionUID = 1L;

            @Override
            protected void service(HttpServletRequest req,
                    HttpServletResponse resp) throws ServletException,
                    IOException {
                req.setAttribute("invoked", "true");
            }
        };
        
        final Field field = ReflectionUtils.findField(servlet.getClass(), "servletContext");
        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field, servlet, servletContext);
        
        request = createMock(HttpServletRequest.class);
        response = createMock(HttpServletResponse.class);
    }

    public void testServiceNoPrefixAttribute() throws Exception {
        expect(request.getAttribute("org.impalaframework.web.servlet.qualifier.WebAttributeQualifierMODULE_QUALIFIER_PREFIX")).andReturn(null);
        
        replay(request, response);
        
        try {
            servlet.service(request, response);
            fail();
        }
        catch (IllegalStateException e) {
            e.printStackTrace();
        }
        
        verify(request, response);
    }

    public void testServiceNoServletAttribute() throws Exception {
        expect(request.getAttribute("org.impalaframework.web.servlet.qualifier.WebAttributeQualifierMODULE_QUALIFIER_PREFIX")).andReturn("att");
        
        replay(request, response);
        
        try {
            servlet.service(request, response);
            fail();
        }
        catch (IllegalStateException e) {
            e.printStackTrace();
        }
        
        verify(request, response);
    }

    public void testServiceWithServlet() throws Exception {
        servletContext.setAttribute("attjavax.servlet.jsp.jsp_servlet", delegate);
        expect(request.getAttribute("org.impalaframework.web.servlet.qualifier.WebAttributeQualifierMODULE_QUALIFIER_PREFIX")).andReturn("att");
        request.setAttribute("invoked", "true");
        
        replay(request, response);

        servlet.service(request, response);
        
        verify(request, response);
    }


}
