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

package example4.servlet;

import interfaces.EntryDAO;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.impalaframework.web.integration.ModuleProxyServlet;
import org.impalaframework.web.spring.integration.InternalFrameworkIntegrationServlet;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import shared.SharedBean;
import classes.Entry;

public class ServletControllerDelegate implements Controller {

    public ModelAndView handleRequest(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        response.getWriter().println("<html><head></head><body>");
        response.getWriter().println("<h1>example-servlet4</h1>");
        response.getWriter().println("<p>Servlet controller delegate for example-servlet4. <br/><br/>No resources are served, the markup is generated within the servlet " + this.getClass().getName() + "</p>");
        
        response.getWriter().write("<p>Mapped via <strong>" + ModuleProxyServlet.class.getName()
        		+ "</strong> entry in web.xml, and integrated into this servlet using <strong>" 
                + InternalFrameworkIntegrationServlet.class.getName()
                + "</strong></p>");
        
        ApplicationContext applicationContext = (ApplicationContext) request.getAttribute("spring.context");
        
        response.getWriter().println("<p>Accessed class for shared bean: " + SharedBean.class.getName() + "</p>");
        new SharedBean().executeMe();
        
        EntryDAO entryDAO = (EntryDAO) applicationContext.getBean("entryDAO");
        int count = 1996;
        Collection<Entry> entries = entryDAO.getEntriesWithCount(count);
        response.getWriter().println("<p>Retrieved " + entries.size() + " entries of count <strong>" + count + "</strong></p>");
        response.getWriter().println("</body></html>");
        
        return null;
    }

}
