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
