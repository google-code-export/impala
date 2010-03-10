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

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.module.spi.FrameworkLockHolder;
import org.impalaframework.util.ObjectUtils;
import org.impalaframework.web.servlet.invoker.HttpServiceInvoker;
import org.impalaframework.web.servlet.invoker.ServletInvokerUtils;
import org.impalaframework.web.spring.ImpalaFrameworkServlet;
import org.impalaframework.web.spring.helper.FrameworkServletContextCreator;
import org.impalaframework.web.spring.helper.ImpalaServletUtils;
import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.FrameworkServlet;

/**
 * Servlet base class which performs similar function to <code>BaseExternalModuleServlet</code>
 * except that it does not participate in the Spring MVC dispatch infrastructure, hence
 * it subclasses directly from <code>FrameworkServlet</code>, and not from
 * <code>DispatcherServlet</code>.
 * 
 * @author Phil Zoio
 */
public class ExternalFrameworkIntegrationServlet extends FrameworkServlet implements ImpalaFrameworkServlet {

    private static final long serialVersionUID = 1L;
    
    /**
     * Whether to set the thread context class loader to that of the class loader 
     * of the module. By default this is true.
     */
    private boolean setThreadContextClassLoader = true;

    private HttpServiceInvoker invoker;
    private FrameworkServletContextCreator frameworkContextCreator;
    private FrameworkLockHolder frameworkLockHolder;
    
    private String delegateServletBeanName = "delegateServlet";
    
    public ExternalFrameworkIntegrationServlet() {
        super();
        this.frameworkContextCreator = new FrameworkServletContextCreator(this);
    }
    
    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //simply pass request through to invoker
        this.invoker.invoke(request, response, null);
    }

    @Override
    protected WebApplicationContext initWebApplicationContext() throws BeansException {
        
        ModuleManagementFacade moduleManagementFacade = ImpalaServletUtils.getModuleManagementFacade(getServletContext());
        this.frameworkLockHolder = moduleManagementFacade.getFrameworkLockHolder();
        
        try {
            
            this.frameworkLockHolder.writeLock();
        
            //create the web application context
            WebApplicationContext wac = createContext();
            
            //make sure that the delegate servlet is available
            Servlet delegateServlet = ObjectUtils.cast(wac.getBean(delegateServletBeanName),
                    Servlet.class);
            
            if (delegateServlet == null) {
                throw new ConfigurationException("No Servlet registered under name " + delegateServletBeanName);
            }
            this.invoker = getInvoker(wac, delegateServlet, this.frameworkLockHolder, this.setThreadContextClassLoader);
            return wac;
            
        }
        finally {
            this.frameworkLockHolder.writeUnlock();
        }
    }

    protected HttpServiceInvoker getInvoker(WebApplicationContext wac,
            Servlet delegateServlet,
            FrameworkLockHolder frameworkLockHolder,
            boolean setThreadContextClassLoader) {
        
        return ServletInvokerUtils.getHttpServiceInvoker(delegateServlet, wac, frameworkLockHolder, setThreadContextClassLoader);
    }
    
    @Override
    public void destroy() {
        ImpalaServletUtils.unpublishWebApplicationContext(this);
        super.destroy();
    }

    /* *************** Helper methods ************** */
    
    protected WebApplicationContext createContext() {
        WebApplicationContext wac = this.frameworkContextCreator.createWebApplicationContext();
        publishContext(wac);
        return wac;
    }

    protected void publishContext(WebApplicationContext wac) {
        ImpalaServletUtils.publishWebApplicationContext(wac, this);
    }

    void setFrameworkContextCreator(FrameworkServletContextCreator helper) {
        this.frameworkContextCreator = helper;
    }

    /* *************** Injection setters ************** */

    /**
     * Delegate servlet bean name in module Spring configuration file
     */
    public void setDelegateServletBeanName(String delegateServletBean) {
        this.delegateServletBeanName = delegateServletBean;
    }
    
    /**
     * Sets whether to set the thread context class loader to that of the class loader 
     * of the module.
     */
    public void setSetThreadContextClassLoader(boolean setThreadContextClassLoader) {
        this.setThreadContextClassLoader = setThreadContextClassLoader;
    }
}
