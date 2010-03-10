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

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

public class JspDispatcher implements RequestDispatcher {

    private HttpServlet jspServlet;

    private String path;

    public JspDispatcher(HttpServlet servlet, String path) {
        super();
        this.jspServlet = servlet;
        this.path = path;
    }

    public void forward(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {
        doInclude(request, response);
    }

    public void include(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {
        doInclude(request, response);
    }

    private void doInclude(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {
        
        request.setAttribute("javax.servlet.include.servlet_path", path);
        jspServlet.service(request, response);
    }

}
