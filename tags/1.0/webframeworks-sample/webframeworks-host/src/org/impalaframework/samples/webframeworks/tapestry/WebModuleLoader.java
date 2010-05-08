package org.impalaframework.samples.webframeworks.tapestry;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.spring.module.loader.ModuleLoaderUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

public class WebModuleLoader extends
        org.impalaframework.web.spring.module.WebModuleLoader {

    @Override
    protected GenericWebApplicationContext newApplicationContext(
            ModuleDefinition moduleDefinition, ApplicationContext parent,
            ClassLoader classLoader, ServletContext servletContext,
            DefaultListableBeanFactory beanFactory) {
        final GenericWebApplicationContext context = new FriggedApplicationContext(beanFactory);
        context.setServletContext(servletContext);
        context.setClassLoader(classLoader);
        
        context.setParent(parent);
        final String displayName = ModuleLoaderUtils.getDisplayName(moduleDefinition, context);
        context.setDisplayName(displayName);
        return context;
    }
    
}

class FriggedApplicationContext extends GenericWebApplicationContext implements ConfigurableWebApplicationContext {

    public FriggedApplicationContext() {
        super();
    }

    public FriggedApplicationContext(DefaultListableBeanFactory beanFactory) {
        super(beanFactory);
    }

    public String[] getConfigLocations() {
        return null;
    }

    public String getNamespace() {
        return null;
    }

    public ServletConfig getServletConfig() {
        return null;
    }

    public void setConfigLocation(String configLocation) {
    }

    public void setConfigLocations(String[] configLocations) {
    }

    public void setNamespace(String namespace) {
    }

    public void setServletConfig(ServletConfig servletConfig) {
    }
        
}

