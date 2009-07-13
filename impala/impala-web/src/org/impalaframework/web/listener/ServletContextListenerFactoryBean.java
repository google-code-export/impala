/*
 * Copyright 2007-2008 the original author or authors.
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

package org.impalaframework.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.impalaframework.util.InstantiationUtils;
import org.impalaframework.util.ObjectUtils;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.ServletContextAware;

/**
 * {@link FactoryBean} which instantiates {@link ServletContextListener}, and
 * invokes life cycle methods.
 * 
 * Triggers
 * {@link ServletContextListener#contextInitialized(ServletContextEvent)} in
 * {@link #afterPropertiesSet()}, and
 * {@link ServletContextListener#contextDestroyed(ServletContextEvent)} in
 * {@link #destroy()}.
 * @author Phil Zoio
 */
public class ServletContextListenerFactoryBean implements ServletContextAware, BeanClassLoaderAware, InitializingBean, DisposableBean {

    private ServletContext servletContext;
    
    private String listenerClassName;
    
    private ClassLoader classLoader;

    private ServletContextListener listener;

    public void afterPropertiesSet() throws Exception {
        Object instantiate = InstantiationUtils.instantiate(listenerClassName, classLoader);
        listener = ObjectUtils.cast(instantiate, ServletContextListener.class);
        listener.contextInitialized(new ServletContextEvent(servletContext));
    }

    public void destroy() throws Exception {
        listener.contextDestroyed(new ServletContextEvent(servletContext));
    }

    public void setListenerClassName(String listenerClassName) {
        this.listenerClassName = listenerClassName;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
    
    ServletContextListener getListener() {
        return listener;
    }

}
