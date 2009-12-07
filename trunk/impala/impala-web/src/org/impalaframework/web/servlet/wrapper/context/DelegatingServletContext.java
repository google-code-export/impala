package org.impalaframework.web.servlet.wrapper.context;

import javax.servlet.ServletContext;

/**
 * Marker interface which can be used to expose the real
 * {@link ServletContext} which is wrapped. Used by "wrapper" {@link ServletContext}
 * implementations.
 * 
 * @author Phil Zoio
 */
public interface DelegatingServletContext {
    public ServletContext getRealContext();
}
