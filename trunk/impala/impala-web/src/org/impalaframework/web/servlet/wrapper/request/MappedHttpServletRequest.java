package org.impalaframework.web.servlet.wrapper.request;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.web.servlet.wrapper.CacheableHttpSession;
import org.impalaframework.web.servlet.wrapper.HttpSessionWrapper;
import org.impalaframework.web.servlet.wrapper.RequestModuleMapping;
import org.springframework.util.Assert;

/**
 * Extension of {@link HttpServletRequestWrapper} which provides implementations for 
 * servlet path related methods. Note that if servlet path supplied in the constructor
 * is not valid (in the sense that, added to context path, it should be a substring of the request
 * URI), then the supplied servlet path value will be ignored.
 * 
 * @author Phil Zoio
 */
public class MappedHttpServletRequest extends HttpServletRequestWrapper {
    
    private static final Log logger = LogFactory.getLog(MappedHttpServletRequest.class);
    
    private static final String WRAPPED_SESSION_KEY = MappedHttpServletRequest.class.getName() + ".WRAPPED_SESSION";
    
    private ServletContext servletContext;
    
    private HttpSessionWrapper httpSessionWrapper;
    
    private String servletPath;

    private String moduleName;
    
    private String applicationId;
    
    private Map<String,String> uriToPathInfo;

    private String contextPathPlusServletPath;

    public MappedHttpServletRequest(
            ServletContext servletContext, 
            HttpServletRequest request, 
            HttpSessionWrapper httpSessionWrapper, 
            RequestModuleMapping moduleMapping, 
            String applicationId) {
        
        super(request);

        Assert.notNull(servletContext, "servletContext cannot be null");
        Assert.notNull(httpSessionWrapper, "httpSessionWrapper cannot be null");
        
        this.servletContext = servletContext;
        this.httpSessionWrapper = httpSessionWrapper;
        this.applicationId = applicationId;
        
        if (moduleMapping != null) {
            
            this.moduleName = moduleMapping.getModuleName();
            String servletPath = moduleMapping.getServletPath();
            
            if (servletPath != null) {
                this.servletPath = servletPath;
                this.contextPathPlusServletPath = request.getContextPath() + (servletPath != null ? servletPath : "");
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
        
        //servlet path not set, corresponding with no "setServletPath" or "servletPath" property - simply use value from superclass
        if (servletPath == null) {
            return super.getPathInfo();
        }
        
        if (uriToPathInfo == null) {
            uriToPathInfo = new HashMap<String, String>();
        }
        
        final String uri = getRequestURI();
        String pathInfo = uriToPathInfo.get(uri);
        if (pathInfo == null) {
            pathInfo = getPathInfo(uri);
            uriToPathInfo.put(uri, pathInfo);
        }
        return pathInfo;
    }
    
    /**
     * If valid servlet path is provided, then real path associated with path info is returned. 
     * Otherwise, delegates to wrapped {@link #getPathInfo()}.
     * 
     * See {@link ServletContext#getRealPath(String)}
     */
    @Override
    public String getPathTranslated() {
        String pathInfo = getPathInfo();
        if (pathInfo != null) {
            return servletContext.getRealPath(pathInfo);
        }
        return super.getPathTranslated();
    }  
    
    @Override
    public HttpSession getSession() {
        
        final CacheableHttpSession cachedSession = getCachedSession();
        if (cachedSession != null) {
            return cachedSession;
        }
        
        HttpSession session = super.getSession();
        return wrapSession(session);
    }

    @Override
    public HttpSession getSession(boolean create) {

        final CacheableHttpSession cachedSession = getCachedSession();
        if (cachedSession != null) {
            return cachedSession;
        }
        
        HttpSession session = super.getSession(create);
        return wrapSession(session);
    }
    
    /* ****************** Helper methods ****************** */
    
    String getPathInfo(String uri) {
        String pathInfo = null;
        
        if (uri.startsWith(this.contextPathPlusServletPath)) {
            pathInfo = uri.substring(this.contextPathPlusServletPath.length());
        } else {
            logger.warn("URI does not start with context plus servlet path combination: " + this.contextPathPlusServletPath);
            final String contextPath = getContextPath();
            if (contextPath != null && uri.startsWith(contextPath)) {
                pathInfo = uri.substring(contextPath.length());
            } else {
                logger.warn("URI does not start with context path: " + contextPath);
                pathInfo = uri;
            }
        }
        return pathInfo;
    }

    HttpSession wrapSession(HttpSession session) {
        if (session == null) {
            return null;
        }
        
        final HttpSession wrappedSession = httpSessionWrapper.wrapSession(session, moduleName, applicationId);
        maybeCacheSession(wrappedSession);
        
        return wrappedSession;
    }

    CacheableHttpSession getCachedSession() {
        final HttpSession existingSession = (HttpSession) getRequest().getAttribute(WRAPPED_SESSION_KEY);
        if (existingSession != null && existingSession instanceof CacheableHttpSession) {
            CacheableHttpSession ss = (CacheableHttpSession) existingSession;
            if (ss.isValid()) {
                return ss;
            } else {
                getRequest().removeAttribute(WRAPPED_SESSION_KEY);
            }
        }
        return null;
    }

    void maybeCacheSession(final HttpSession wrappedSession) {
        if (wrappedSession instanceof CacheableHttpSession) {
            getRequest().setAttribute(WRAPPED_SESSION_KEY, wrappedSession);
        }
    }  
}
