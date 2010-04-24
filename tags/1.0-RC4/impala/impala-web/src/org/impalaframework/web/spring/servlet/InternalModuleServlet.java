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

package org.impalaframework.web.spring.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.impalaframework.web.servlet.invoker.HttpServiceInvoker;
import org.impalaframework.web.servlet.invoker.ThreadContextClassLoaderHttpServiceInvoker;
import org.impalaframework.web.spring.ImpalaFrameworkServlet;
import org.impalaframework.web.spring.helper.ImpalaServletUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.util.NestedServletException;

/**
 * Extension of {@link DispatcherServlet} for servlets which are defined
 * not in web.xml but internally within the module using the
 * <code>ServletFactoryBean</code>. At runtime, an instance can be retrieved
 * using <code>ModuleProxyServlet</code>
 * 
 * @author Phil Zoio
 */
public class InternalModuleServlet extends DispatcherServlet implements ApplicationContextAware, HttpServiceInvoker, ImpalaFrameworkServlet {
    
    private static final long serialVersionUID = 1L;
    
    private WebApplicationContext applicationContext;
    
    private HttpServiceInvoker invoker;
    
    /**
     * Sets whether to set the thread context class loader to that of the class loader 
     * of the module. By default this is false.
     */
    private boolean setThreadContextClassLoader;
    
    public InternalModuleServlet() {
        super();
    }

    @Override
    protected WebApplicationContext initWebApplicationContext()
            throws BeansException {
        onRefresh(applicationContext);
        
        //FIXME also, can this not simply override the findApplicationContext method
        ImpalaServletUtils.publishWebApplicationContext(applicationContext, this);

        setInvoker();
        return applicationContext;
    }

    void setInvoker() {
        this.invoker = new ThreadContextClassLoaderHttpServiceInvoker(this, setThreadContextClassLoader, applicationContext.getClassLoader());
    }   

    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
        invoker.invoke(request, response, null);
    }
    
    public void invoke(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            super.doService(request, response);
        } catch (Exception e) {
            throw new NestedServletException("Request processing failed", e);
        }
    }

    @Override
    public void destroy() {
        ImpalaServletUtils.unpublishWebApplicationContext(this);
        super.destroy();
    }

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = ImpalaServletUtils.checkIsWebApplicationContext(getServletName(), applicationContext);
    }

    public void setSetThreadContextClassLoader(boolean setThreadContextClassLoader) {
        this.setThreadContextClassLoader = setThreadContextClassLoader;
    }
    
}
