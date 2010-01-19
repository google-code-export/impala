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
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
public class ServletContextListenerFactoryBean implements ApplicationContextAware, ServletContextAware, BeanClassLoaderAware, InitializingBean, DisposableBean, FactoryBean {

    private ServletContext servletContext;
    
    private String listenerClass;
    
    private ClassLoader classLoader;

    private ServletContextListener listener;
    
    private ApplicationContext applicationContext;

    public void afterPropertiesSet() throws Exception {
        Object instantiate = InstantiationUtils.instantiate(listenerClass, classLoader);
        listener = ObjectUtils.cast(instantiate, ServletContextListener.class);
        
        if (listener instanceof ApplicationContextAware) {
            //FIXME test
            ((ApplicationContextAware) listener).setApplicationContext(applicationContext);
        }
        
        listener.contextInitialized(new ServletContextEvent(servletContext));
    }

    public void destroy() throws Exception {
        listener.contextDestroyed(new ServletContextEvent(servletContext));
    }

    public void setListenerClass(String listenerClassName) {
        this.listenerClass = listenerClassName;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
    
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    ServletContextListener getListener() {
        return listener;
    }

    public Object getObject() throws Exception {
        return listener;
    }

    public Class<?> getObjectType() {
        return ServletContextListener.class;
    }

    public boolean isSingleton() {
        return true;
    }

}
