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

package example3.servlet;

import interfaces.EntryDAO;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.impalaframework.util.serialize.ClassLoaderAwareSerializationStreamFactory;
import org.impalaframework.util.serialize.SerializationHelper;
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
        response.getWriter().println("<h1>example-servlet3</h1>");
        response.getWriter().println("Servlet controller delegate for example-servlet3. <br/><br/>No resources are served, the markup is generated within the servlet <strong>" 
                + this.getClass().getName() + "</strong>");
        ApplicationContext applicationContext = (ApplicationContext) request.getAttribute("spring.context");

        response.getWriter().write("<p>Mapped via entry ModuleProxyServlet entry in web.xml, and integrated into this servlet using <strong>" 
                + InternalFrameworkIntegrationServlet.class.getName()
        		+ "</strong></p>");
        /*
        We can execute this because we have exposed the current application context as the "root" web application context
        via the ModuleAwareWrapperServletContext
        */
        SharedBean bean = (SharedBean) applicationContext.getBean("sharedBean");
        bean.executeMe();
        response.getWriter().write("<p>Just executed bean " + SharedBean.class.getName() + ": " + bean + "</p>");
        
        checkSession(request, response, applicationContext);
        
        response.getWriter().println("Accessed class for shared bean: " + SharedBean.class.getName());
        new SharedBean().executeMe();
        
        EntryDAO entryDAO = (EntryDAO) applicationContext.getBean("entryDAO");
        int count = 1996;
        Collection<Entry> entries = entryDAO.getEntriesWithCount(count);
        response.getWriter().println("Retrieved " + entries.size() + " entries of count " + count);
        response.getWriter().println("</body></html>");
        
        return null;
    }

    private void checkSession(HttpServletRequest request, HttpServletResponse response, ApplicationContext applicationContext) throws IOException {
        
        //This method demonstrates the mechanism for recovering an item in the session following a module reload.
        //The framework will build support for this (see ticket 57)
        
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        Object value = session.getAttribute("sessionValueHolder");
        
        SessionValueHolder holder = null;
        if (value == null) {
            holder = new SessionValueHolder();
            session.setAttribute("sessionValueHolder", holder);
            out.println("Created new SessionValueHolder");
        } else {
            try {
                holder = (SessionValueHolder) value;
            } catch (ClassCastException e) {
                //will probably get this the first time you reload a module.
                out.println("Caught class cast exception. Module has probably reloaded. Trying to recover");
                ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                SerializationHelper helper = new SerializationHelper(new ClassLoaderAwareSerializationStreamFactory(contextClassLoader));
                holder = (SessionValueHolder) helper.clone((Serializable) value);
                out.println("Successfully recovered session data, restoring in session");
                session.setAttribute("sessionValueHolder", holder);
            }
        }
        holder.increment();
        holder.setMessage("Time in milliseconds - " + System.currentTimeMillis());
        
        out.println("Value is now " + holder.getValue());
        out.println("Title now " + holder.getMessage());
    }

}
