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

import java.util.Map;

import javax.servlet.Servlet;

import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.servlet.FrameworkServlet;

/**
 * Extension of {@link ServletFactoryBean} which is designed specifically to
 * handle non-Impala subclasses of Spring MVC's framework servlet. In addition
 * to instantiating the servlet concerned (done in
 * {@link ServletFactoryBean#afterPropertiesSet()}) this implementation will
 * also instantiate {@link InternalFrameworkIntegrationServlet}, and set the
 * Spring MVC's {@link FrameworkServlet} instance as a delegate. It will also
 * publish the current {@link ApplicationContext} using the
 * {@link FrameworkServlet#getServletContextAttributeName()}, so that the
 * {@link FrameworkServlet} instance can find the application context to use,
 * rather than instantiate its own.
 * 
 * @author Phil Zoio
 */
public class FrameworkIntegrationServletFactoryBean extends ServletFactoryBean {

    private String contextAttribute;
    private InternalFrameworkIntegrationServlet integrationServlet;

    @Override
    public void setServletClass(Class<?> servletClass) {
        Assert.isTrue(FrameworkServlet.class.isAssignableFrom(servletClass), "Servlet class must be assignable to FrameworkServlet");
        super.setServletClass(servletClass);
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {

        //this will set up the delegate servlet
        super.afterPropertiesSet();
        
        completeAfterProperties();
    }

    void completeAfterProperties() throws Exception {
        
        //get the FrameworkServlet instance
        FrameworkServlet delegate = (FrameworkServlet) super.getObject();
        
        //set up the integration servlet
        integrationServlet = new InternalFrameworkIntegrationServlet();
        integrationServlet.setApplicationContext(getApplicationContext());
        integrationServlet.setSetContextClassLoader(true);
        integrationServlet.setDelegateServlet(delegate);
    }

    @Override
    protected void initServletProperties(Servlet servlet) {
        
        //check the context attribute
        final Map<String, String> initParameters = super.getInitParameters();
        final String attribute = initParameters.get("contextAttribute");
        
        if (attribute == null) {
            this.contextAttribute = "someattribute";
            initParameters.put("contextAttribute", this.contextAttribute);
        } else {
            this.contextAttribute = attribute;
        }
        
        //bind dispatcher servlet to context attribute
        getServletContext().setAttribute(contextAttribute, getApplicationContext());
    }
    
    @Override
    public Class<?> getObjectType() {
        return InternalFrameworkIntegrationServlet.class;
    }
    
    @Override
    public Object getObject() throws Exception {
        return integrationServlet;
    }
    
    @Override
    public void destroy() throws Exception {
        super.destroy();
        integrationServlet.destroy();
        
        getServletContext().removeAttribute(contextAttribute);

        //FIXME only do this if published context
        String attrName = FrameworkServlet.SERVLET_CONTEXT_PREFIX + getServletName();
        getServletContext().removeAttribute(attrName);
    }
    
}
