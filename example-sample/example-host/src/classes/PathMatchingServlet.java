package classes;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PathMatchingServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        final PrintWriter writer = resp.getWriter();
        writer.println("Servlet output:");
        writer.println("Request URI: " + req.getRequestURI());
        writer.println("Context path" + req.getContextPath());
        writer.println("Servlet path: " + req.getServletPath());
        writer.println("Path info: " + req.getPathInfo());
    }
}
