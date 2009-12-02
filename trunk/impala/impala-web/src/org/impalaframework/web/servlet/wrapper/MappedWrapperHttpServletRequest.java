package org.impalaframework.web.servlet.wrapper;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

/**
 * Extension of {@link HttpServletRequestWrapper} which provides implementations for 
 * servlet path related methods. Note that if servlet path supplied in the constructor
 * is not valid (in the sense that, added to context path, it should be a substring of the request
 * URI), then the supplied servlet path value will be ignored.
 * 
 * @author Phil Zoio
 */
public class MappedWrapperHttpServletRequest extends
        HttpServletRequestWrapper {
    
    private static final Log logger = LogFactory.getLog(MappedWrapperHttpServletRequest.class);
    
    private ServletContext servletContext;
    
    private HttpSessionWrapper httpSessionWrapper;
    
    private String servletPath;
    
    private String pathInfo;

    private String moduleName;

    public MappedWrapperHttpServletRequest(
            ServletContext servletContext, 
            HttpServletRequest request, 
            HttpSessionWrapper httpSessionWrapper, 
            RequestModuleMapping moduleMapping) {
        
        super(request);

        Assert.notNull(servletContext, "servletContext cannot be null");
        Assert.notNull(httpSessionWrapper, "httpSessionWrapper cannot be null");
        
        this.servletContext = servletContext;
        this.httpSessionWrapper = httpSessionWrapper;
        
        if (moduleMapping != null) {
            
            this.moduleName = moduleMapping.getModuleName();
            String servletPath = moduleMapping.getServletPath();
            
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
    }

    /**
     * If valid servlet path is provided, then this is returned. Otherwise, delegates to wrapped {@link #getServletPath()}
     */
    @Override
    public String getServletPath() {
        if (servletPath != null) {
            return servletPath;
        }
        return super.getServletPath();
    }
    
    /**
     * If valid servlet path is provided, the remainder of URI is returned. Otherwise, delegates to wrapped {@link #getPathInfo()}
     */
    @Override
    public String getPathInfo() {
        if (pathInfo != null) {
            return pathInfo;
        }
        return super.getPathInfo();
    }
    
    /**
     * If valid servlet path is provided, then real path associated with path info is returned. 
     * Otherwise, delegates to wrapped {@link #getPathInfo()}.
     * 
     * See {@link ServletContext#getRealPath(String)}
     */
    @Override
    public String getPathTranslated() {
        if (pathInfo != null) {
            return servletContext.getRealPath(pathInfo);
        }
        return super.getPathTranslated();
    }  
    
    @Override
    public HttpSession getSession() {
        
        HttpSession session = super.getSession();
        return wrapSession(session);
    }

    @Override
    public HttpSession getSession(boolean create) {
        
        HttpSession session = super.getSession(create);
        return wrapSession(session);
    }
    
    /* ****************** Helper methods ****************** */

    HttpSession wrapSession(HttpSession session) {
        return httpSessionWrapper.wrapSession(session, moduleName);
    }  
}
