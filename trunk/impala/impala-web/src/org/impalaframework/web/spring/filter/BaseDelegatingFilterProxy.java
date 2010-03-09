package org.impalaframework.web.spring.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.web.servlet.qualifier.WebAttributeQualifier;
import org.impalaframework.web.spring.helper.ImpalaServletUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Extension of {@link BaseDelegatingFilterProxy} which passes in login call to
 * user-configured bean and module combination. The target bean must implement
 * the {@link Filter} interface. Inspired by the Spring
 * {@link org.springframework.web.filter.DelegatingFilterProxy}
 * @author Phil Zoio
 */
public abstract class BaseDelegatingFilterProxy extends GenericFilterBean {
    
    private String targetModuleName;
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //FIXME test
        ServletContext servletContext = getServletContext();

        ModuleManagementFacade moduleManagementFacade = ImpalaServletUtils.getModuleManagementFacade(servletContext);
        WebAttributeQualifier qualifier = getWebAttributeQualifier(moduleManagementFacade);
        String applicationId = getApplicationId(moduleManagementFacade);
        
        String attributeName = qualifier.getQualifiedAttributeName(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, applicationId, targetModuleName);
        
        WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext, attributeName);
        final String beanName = getBeanName(request);
        Filter delegate = (Filter) applicationContext.getBean(beanName, Filter.class);
        invokeDelegate(delegate, request, response, filterChain);
    }

    protected WebAttributeQualifier getWebAttributeQualifier(ModuleManagementFacade moduleManagementFacade) {
        return (WebAttributeQualifier) moduleManagementFacade.getBean("webAttributeQualifier", WebAttributeQualifier.class);
    }

    protected String getApplicationId(ModuleManagementFacade moduleManagementFacade) {
        return moduleManagementFacade.getApplicationManager().getCurrentApplication().getId();
    }

    protected abstract String getBeanName(ServletRequest request);
    
    protected void invokeDelegate(Filter delegate, ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        delegate.doFilter(request, response, filterChain);
    }
    
    public void setTargetModuleName(String moduleName) {
        this.targetModuleName = moduleName;
    }
    
}
