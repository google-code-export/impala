package org.impalaframework.samples.springfaces.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Published current application context under the servlet context attribute key: securityContext
 * @author Phil Zoio
 */
public class ContextPublisher implements ServletContextListener, ApplicationContextAware {

    private ApplicationContext applicationContext;

    public void contextInitialized(ServletContextEvent event) {
        event.getServletContext().setAttribute("shared:securityContext", applicationContext); 
    }

    public void contextDestroyed(ServletContextEvent event) {
        event.getServletContext().removeAttribute("shared:securityContext"); 
    }

    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.applicationContext = context;
    }

}
