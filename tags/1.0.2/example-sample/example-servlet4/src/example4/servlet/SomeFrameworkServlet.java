package example4.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.impalaframework.util.InstantiationUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.mvc.Controller;

public class SomeFrameworkServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private String controllerClassName;
    private Controller instance;
    private ServletContext servletContext;
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        controllerClassName = config.getInitParameter("controllerClassName");
        servletContext = config.getServletContext();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (instance == null) {
            //instantiate, using the ContextClassLoader
            instance = InstantiationUtils.instantiate(controllerClassName);
        }
        try {
            ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
            request.setAttribute("spring.context", context);
            instance.handleRequest(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
