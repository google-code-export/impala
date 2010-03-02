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

package org.impalaframework.web.spring.servlet;

import static org.easymock.EasyMock.createMock;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.springframework.util.ClassUtils;
import org.springframework.web.context.support.GenericWebApplicationContext;

public class InternalModuleServletTest extends TestCase {

    public void testInternalModuleServlet() throws Exception {
        
        final AtomicBoolean b = new AtomicBoolean();
        
        assertFalse(b.get());
        
        InternalModuleServlet servlet = new InternalModuleServlet(){

            private static final long serialVersionUID = 1L;

            @Override
            protected void service(HttpServletRequest req,
                    HttpServletResponse resp) throws ServletException,
                    IOException {
                b.set(true);
            }
        };
        final GenericWebApplicationContext ac = new GenericWebApplicationContext();
        ac.setClassLoader(ClassUtils.getDefaultClassLoader());
        servlet.setApplicationContext(ac);
        
        servlet.setInvoker();
        
        servlet.service(createMock(HttpServletRequest.class), createMock(HttpServletResponse.class));

        assertTrue(b.get());
    }
    
}
