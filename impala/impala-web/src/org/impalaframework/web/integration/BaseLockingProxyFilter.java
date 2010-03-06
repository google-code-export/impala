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

package org.impalaframework.web.integration;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.module.spi.FrameworkLockHolder;
import org.impalaframework.util.ObjectUtils;
import org.impalaframework.web.spring.helper.ImpalaServletUtils;
import org.impalaframework.web.utils.WebPathUtils;

/**
 * Base {@link Filter} which provides locking support for subclasses
 * through a final implementation of
 * {@link #doFilter(ServletRequest, ServletResponse)} which wraps call to
 * {@link #doFilter(HttpServletRequest, HttpServletResponse, ServletContext, FilterChain)}
 * with a read lock obtained from {@link FrameworkLockHolder}
 * 
 * @author Phil Zoio
 */
public abstract class BaseLockingProxyFilter implements Filter {
    
    private static final Log logger = LogFactory.getLog(BaseLockingProxyFilter.class);   

    private FrameworkLockHolder frameworkLockHolder;

    private FilterConfig filterConfig;
    
    public BaseLockingProxyFilter() {
        super();
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        ModuleManagementFacade moduleManagementFacade = ImpalaServletUtils.getModuleManagementFacade(filterConfig.getServletContext());
        this.frameworkLockHolder = moduleManagementFacade.getFrameworkLockHolder();
    }

    public final void doFilter(ServletRequest servletRequest, 
            ServletResponse servletResponse,
            FilterChain chain) throws IOException, ServletException {
        
        try {
            this.frameworkLockHolder.readLock();
            
            HttpServletRequest request = ObjectUtils.cast(servletRequest, HttpServletRequest.class);
            HttpServletResponse response = ObjectUtils.cast(servletResponse, HttpServletResponse.class);
    
            WebPathUtils.maybeLogRequest(request, logger);
            
            ServletContext context = filterConfig.getServletContext();
            doFilter(request, response, context, chain);
        
        } finally {
            this.frameworkLockHolder.readUnlock();
        }
    }
    
    /* **************** protected abstract methods ******************* */
    
    protected abstract void doFilter(HttpServletRequest request, 
            HttpServletResponse response,
            ServletContext context, 
            FilterChain chain)
            throws ServletException, IOException;
    
    /* **************** protected getters ******************* */

    protected FilterConfig getFilterConfig() {
        return filterConfig;
    }
}
