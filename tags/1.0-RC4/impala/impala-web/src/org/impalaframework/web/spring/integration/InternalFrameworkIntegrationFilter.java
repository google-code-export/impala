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

package org.impalaframework.web.spring.integration;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.impalaframework.util.ObjectUtils;
import org.impalaframework.web.servlet.invoker.ThreadContextClassLoaderHttpServiceInvoker;
import org.impalaframework.web.spring.helper.ImpalaServletUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;

/**
 * <p>
 * Filter which is design to integrate with web frameworks other than Spring
 * MVC. Performs the same function as <code>InternalFrameworkIntegrationServlet</code>,
 * but works with <code>Filter</code> instances.
 * 
 * @see ModuleProxyServlet
 * @see InternalFrameworkIntegrationServlet
 * @author Phil Zoio
 */
public class InternalFrameworkIntegrationFilter implements javax.servlet.Filter, ApplicationContextAware {
    
    private static final long serialVersionUID = 1L;

    private WebApplicationContext applicationContext;
    
    private Filter delegateFilter;
    
    /**
     * Determine whether to set the context class loader. This almost certainly
     * need to be true. Frameworks such as Struts which dynamically instantiate
     * classes typically use <code>Thread.currentThread().getContextClassLoader()</code> to 
     * retrieve the class loader with which to instantiate classes. This needs to be set correctly to the 
     * class loader of the current module's application context to ensure that resource contained within the module
	 * (e.g. Struts action and form classes) can be found using the current thread's context class loader.
     */
    private boolean setContextClassLoader = true;

    private ClassLoader currentClassLoader;

    private ThreadContextClassLoaderHttpServiceInvoker invoker;
    
    private FilterConfig filterConfig;
    
    public InternalFrameworkIntegrationFilter() {
        super();
    }

    public void init(FilterConfig config) throws ServletException {
        
        this.filterConfig = config;
        
        ImpalaServletUtils.checkIsWebApplicationContext(filterConfig.getFilterName(), applicationContext);
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        
        ClassLoader moduleClassLoader = applicationContext.getClassLoader();
        if (this.invoker == null || this.currentClassLoader != moduleClassLoader) {
            this.invoker = new ThreadContextClassLoaderHttpServiceInvoker(delegateFilter, setContextClassLoader, moduleClassLoader);
            this.currentClassLoader = moduleClassLoader;
        }
        
        final HttpServletRequest httpServletRequest = ObjectUtils.cast(request, HttpServletRequest.class);
        final HttpServletResponse httpServletResponse = ObjectUtils.cast(response, HttpServletResponse.class);
        
        this.invoker.invoke(httpServletRequest, httpServletResponse, chain);
    }

    public void destroy() {
    }   
    
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = ObjectUtils.cast(applicationContext, WebApplicationContext.class);
    }
    
    /* ************************ injected setters ************************** */

    public void setDelegateFilter(Filter filter) {
        this.delegateFilter = filter;
    }

    public void setSetContextClassLoader(boolean setContextClassLoader) {
        this.setContextClassLoader = setContextClassLoader;
    }

}
