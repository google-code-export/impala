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

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.impalaframework.web.servlet.invoker.ThreadContextClassLoaderHttpServiceInvoker;
import org.impalaframework.web.spring.helper.ImpalaServletUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.HttpServletBean;

/**
 * <p>
 * Servlet which is design to integrate with web frameworks other than Spring
 * MVC. Unlike <code>BaseExternalModuleServlet</code> it does not need a read-write
 * lock to synchronize updates to the <code>ApplicationContext</code> because
 * it shares the application context's life cycle. In it's <code>service</code>
 * method, it then passes control to the the wired in
 * <code>delegateServlet</code> instance, which itself needs to be set up
 * using <code>ServletFactoryBean</code>.
 * <p>To configure
 * <code>InternalFrameworkIntegrationServlet</code> you will need to use
 * <code>InternalFrameworkIntegrationServletFactoryBean</code>
 * 
 * @see ModuleProxyServlet
 * @see InternalFrameworkIntegrationServletFactoryBean
 * @author Phil Zoio
 */
public class InternalFrameworkIntegrationServlet extends HttpServletBean implements ApplicationContextAware {
    
    private static final long serialVersionUID = 1L;

    private WebApplicationContext applicationContext;
    
    private Servlet delegateServlet;
    
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
    
    public InternalFrameworkIntegrationServlet() {
        super();
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        ClassLoader moduleClassLoader = applicationContext.getClassLoader();
        if (this.invoker == null || this.currentClassLoader != moduleClassLoader) {
            this.invoker = new ThreadContextClassLoaderHttpServiceInvoker(delegateServlet, setContextClassLoader, moduleClassLoader);
            this.currentClassLoader = moduleClassLoader;
        }
        
        this.invoker.invoke(request, response, null);
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = ImpalaServletUtils.checkIsWebApplicationContext(getServletName(), applicationContext);
    }

    /* ************************ protected accessor method ************************** */
    
    protected Servlet getDelegateServlet() {
        return delegateServlet;
    }
    
    protected WebApplicationContext getApplicationContext() {
        return applicationContext;
    }
    
    /* ************************ injected setters ************************** */

    public void setDelegateServlet(Servlet delegateServlet) {
        this.delegateServlet = delegateServlet;
    }

    public void setSetContextClassLoader(boolean setContextClassLoader) {
        this.setContextClassLoader = setContextClassLoader;
    }

}
