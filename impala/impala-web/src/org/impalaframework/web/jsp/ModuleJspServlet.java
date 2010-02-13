package org.impalaframework.web.jsp;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        
        //FIXME issue 255 - added module qualifier prefix
        final String prefix = (String) req.getAttribute("module_qualifier_prefix");

        final HttpServlet jspServlet = (HttpServlet) servletContext.getAttribute(prefix + JspConstants.JSP_SERVLET);
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
