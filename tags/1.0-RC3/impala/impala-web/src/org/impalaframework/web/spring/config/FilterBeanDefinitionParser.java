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

import org.impalaframework.web.spring.integration.FilterFactoryBean;
import org.impalaframework.web.spring.integration.InternalFrameworkIntegrationFilter;
import org.impalaframework.web.spring.integration.InternalFrameworkIntegrationFilterFactoryBean;


/**
 * Bean definition parser for <i>filter</i> element from <i>web</i> namespace.
 * 
 * @author Phil Zoio
 */
public class FilterBeanDefinitionParser extends AbstractWebHandlerBeanDefinitionParser {

    private static final String DELEGATOR_FILTER_NAME_ATTRIBUTE =  "delegatorFilterName";

    private static final String FILTER_NAME_PROPERTY = "filterName";
    private static final String FILTER_CLASS_PROPERTY = "filterClass";
    
    private static final String DELEGATE_FILTER_PROPERTY = "delegateFilter";
    
    public FilterBeanDefinitionParser() {
        super();
    }
    
    protected String getHandlerClassAttribute() {
        return FILTER_CLASS_PROPERTY;
    }
    
    protected Class<?> getDefaultFactoryBeanClass() {
        return FilterFactoryBean.class;
    }

    protected Class<?> getIntegrationHandlerClass() {
        return InternalFrameworkIntegrationFilter.class;
    }
    
    protected Class<?> getIntegrationHandlerFactoryClass() {
        return InternalFrameworkIntegrationFilterFactoryBean.class;
    }
    
    protected String getDelegateHandlerProperty() {
        return DELEGATE_FILTER_PROPERTY;
    }
    
    protected String getDelegatorHandlerNameAttribute() {
        return DELEGATOR_FILTER_NAME_ATTRIBUTE;
    }
    
    protected String getHandlerNameProperty() {
        return FILTER_NAME_PROPERTY;
    }

    protected String getHandlerClassProperty() {
        return FILTER_CLASS_PROPERTY;
    }
    
}
