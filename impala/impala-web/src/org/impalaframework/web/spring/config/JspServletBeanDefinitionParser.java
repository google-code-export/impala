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

package org.impalaframework.web.spring.config;

import org.impalaframework.web.jsp.JspServletFactoryBean;


/**
 * Bean definition parser for <i>servlet</i> element from <i>web</i> namespace.
 * 
 * @author Phil Zoio
 */
public class JspServletBeanDefinitionParser extends AbstractWebHandlerBeanDefinitionParser {

    private static final String SERVLET_NAME_PROPERTY = "servletName";
    private static final String SERVLET_CLASS_PROPERTY = "servletClass";
    
    public JspServletBeanDefinitionParser() {
        super();
    }
    
    protected String getHandlerClassAttribute() {
        return SERVLET_CLASS_PROPERTY;
    }

    protected Class<?> getDefaultFactoryBeanClass() {
        return JspServletFactoryBean.class;
    }
    
    protected Class<?> getDefaultHandlerClass() {
        return null;
    }

    protected Class<?> getIntegrationHandlerClass() {
        return null;
    }
    
    protected Class<?> getIntegrationHandlerFactoryClass() {
        return null;
    }
    
    protected String getDelegateHandlerProperty() {
        return null;
    }
    
    protected String getDelegatorHandlerNameAttribute() {
        return null;
    }
    
    protected String getHandlerNameProperty() {
        return SERVLET_NAME_PROPERTY;
    }

    protected String getHandlerClassProperty() {
        return SERVLET_CLASS_PROPERTY;
    }
}
