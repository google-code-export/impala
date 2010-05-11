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

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.impalaframework.web.servlet.qualifier.WebAttributeQualifier;

/**
 * Simple servlet implementation which is able to wrap a JSP invocation. Setup
 * require and use requires the following:
 * <ul>
 * <li>Ensure that the property 'partitioned.servlet.context' is set to true in
 * <i>impala.properties</i>
 * <li>Add a JSP servlet entry into the module itself, for example using an
 * entry such as the following:<br/> 
 * <code>&lt;bean id="jspServlet" class="org.impalaframework.web.jsp.JspServletFactoryBean"&gt;<br/>
        &lt;property name="servletName" value="jspServlet"/&gt;<br/>
    &lt;/bean&gt;</code>
 * </ul>
 * Note that direct external invocation to JSPs hosted within modules via this
 * servlet is not supported. Invocation is expected to be internally within the
 * application, for example via {@link java.servet.RequestDispatcher}
 * <code>forward()</code> or <code>include()</code> calls.
 * 
 * @author Phil Zoio
 */
public class ModuleJspServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ServletContext servletContext;
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.servletContext = config.getServletContext();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        final String prefix = (String) req.getAttribute(WebAttributeQualifier.MODULE_QUALIFIER_PREFIX);
        if (prefix == null) {
            throw new IllegalStateException("Cannot resolve attribute '" + WebAttributeQualifier.MODULE_QUALIFIER_PREFIX + "'." +
                    " This attribute needs to be set in order for ModuleJspServlet to be able to find the module registered JSP servlet to which the request will be forwarded." +
                    " Possible causes: 1) you have not set the property 'partitioned.servlet.context=true' in impala.properties, or " +
                    " 2) you have attempted to access the JSP directly rather than through a request or forward from a servlet or filter within the application.");
        }

        final HttpServlet jspServlet = (HttpServlet) servletContext.getAttribute(prefix + JspConstants.JSP_SERVLET);
        
        if (jspServlet == null) {
            throw new IllegalStateException("No JSP servlet registered in the module to which the request was directed." +
                    " Your module configuration requires a " + JspServletFactoryBean.class + " configuration entry, or equivalent.");
        }
        
        jspServlet.service(req, resp);
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        service(req, resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        service(req, resp);
    }
    
}
