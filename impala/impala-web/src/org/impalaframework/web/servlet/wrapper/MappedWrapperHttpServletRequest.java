package org.impalaframework.web.servlet.wrapper;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MappedWrapperHttpServletRequest extends
        HttpServletRequestWrapper {
    
    private static final Log logger = LogFactory.getLog(ModuleAwareWrapperHttpServletRequest.class);

    //FIXME complete implementation and test
    
    private ServletContext servletContext;
    
    private String servletPath;
    
    private String pathInfo;

    public MappedWrapperHttpServletRequest(HttpServletRequest request, ServletContext servletContext, String servletPath) {
        super(request);
        this.servletContext = servletContext;
        
        if (servletPath != null) {
            String contextPathPlusServletPath = request.getContextPath() + servletPath;
            String uri = request.getRequestURI();
            
            if (uri.startsWith(contextPathPlusServletPath)) {
                this.servletPath = servletPath;
                this.pathInfo = uri.substring(contextPathPlusServletPath.length());
            } else {
                logger.warn("URI does not start with context plus servlet path combination: " + contextPathPlusServletPath);
            }
        }
    }

    @Override
    public String getServletPath() {
        if (servletPath != null) {
            return servletPath;
        }
        return super.getServletPath();
    }
    
    @Override
    public String getPathInfo() {
        if (pathInfo != null) {
            return pathInfo;
        }
        return super.getPathInfo();
    }

    @Override
    public String getPathTranslated() {
        if (pathInfo != null) {
            return servletContext.getRealPath(pathInfo);
        }
        return super.getPathTranslated();
    }
 
}
