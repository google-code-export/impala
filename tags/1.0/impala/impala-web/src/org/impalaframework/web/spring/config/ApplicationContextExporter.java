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

package org.impalaframework.web.spring.config;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;

/**
 * Class which can be used to export the current application context to the {@link ServletContext}
 * using a specified key, as defined using {@link #contextAttribute}.
 * 
 * @author Phil Zoio
 */
public class ApplicationContextExporter implements ServletContextAware, ApplicationContextAware, InitializingBean, DisposableBean {

    private String contextAttribute;
    
    private ServletContext servletContext;
    
    private ApplicationContext applicationContext;
    
    public void setContextAttribute(String contextAttribute) {
        this.contextAttribute = contextAttribute;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(contextAttribute, "contextAttribute cannot be null");
        Assert.notNull(applicationContext, "applicationContext cannot be null");
        Assert.notNull(servletContext, "servletContext cannot be null");
        servletContext.setAttribute(contextAttribute, applicationContext);
    }

    public void destroy() throws Exception {
        if (contextAttribute != null) {
            servletContext.removeAttribute(contextAttribute);
        }
    }
    
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
