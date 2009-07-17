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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.impalaframework.exception.ExecutionException;
import org.impalaframework.util.CollectionStringUtils;
import org.impalaframework.web.spring.integration.InternalFrameworkIntegrationServlet;
import org.impalaframework.web.spring.integration.InternalFrameworkIntegrationServletFactoryBean;
import org.impalaframework.web.spring.integration.ServletFactoryBean;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 * Bean definition parser for <i>servlet</i> element from <i>web</i> namespace.
 * 
 * @author Phil Zoio
 */
public class ServletBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {

    private static final String INIT_PARAMS_ELEMENT =  "init-parameters";
    private static final String PARAM_ELEMENT =  "param";
    private static final String PROPERTY_ELEMENT = "property";    

    //FIXME rename to delegatorServletName
    private static final String DELEGATOR_SERVLET_ATTRIBUTE =  "delegatorServlet";
    private static final String INIT_PARAMS_ATTRIBUTE =  "initParameters";
    private static final String FACTORY_CLASS_ATTRIBUTE =  "factoryClass";
    private static final String NAME_ATTRIBUTE = "name";
    private static final String VALUE_ATTRIBUTE = "value";
    
    private static final String INIT_PARAMS_PROPERTY=  "initParameters";
    private static final String SERVLET_NAME_PROPERTY = "servletName";
    private static final String SERVLET_CLASS_PROPERTY = "servletClass";
    
    private static final String DELEGATE_SERVLET_PROPERTY = "delegateServlet";
    
    public ServletBeanDefinitionParser() {
        super();
    }

    @Override
    protected Class<?> getBeanClass(Element element) {
        String factoryClass = element.getAttribute(FACTORY_CLASS_ATTRIBUTE);
        if (StringUtils.hasText(factoryClass)) {
            
            try {
                return ClassUtils.forName(factoryClass);
            }
            catch (Throwable e) {
                throw new ExecutionException("Unable to load class: " + factoryClass + ": " + e.getMessage(), e);
            }
        }
        return ServletFactoryBean.class;
    }
    
    @Override
    protected boolean isEligibleAttribute(String attributeName) {
        if (FACTORY_CLASS_ATTRIBUTE.equals(attributeName) 
            || INIT_PARAMS_ATTRIBUTE.equals(attributeName)
            || DELEGATOR_SERVLET_ATTRIBUTE.equals(attributeName)) {
            return false;
        }
        return super.isEligibleAttribute(attributeName);
    }

    @Override
    protected void doParse(Element element, 
            ParserContext parserContext,
            BeanDefinitionBuilder builder) {
        
        super.doParse(element, parserContext, builder);
        
        if (!StringUtils.hasText(element.getAttribute(SERVLET_NAME_PROPERTY))) {
            builder.addPropertyValue(SERVLET_NAME_PROPERTY, element.getAttribute(ID_ATTRIBUTE));
        }

        handleInitParameters(element, builder);
        
        handlePropertyElements(element, parserContext, builder);
        
        handleDelegatorServletAttribute(element, parserContext);
    }
    
    void handleInitParameters(Element element, BeanDefinitionBuilder builder) {
        
        Map<String,String> initParameters = new LinkedHashMap<String,String>();
        handleInitParametersElement(element, initParameters);
        handleInitParamsAttribute(element, initParameters);
        
        // Specific environment settings defined, overriding any shared properties.
        builder.addPropertyValue(INIT_PARAMS_PROPERTY, initParameters);
    }

    void handleDelegatorServletAttribute(Element element, ParserContext parserContext) {
        
        String delegatorServlet = element.getAttribute(DELEGATOR_SERVLET_ATTRIBUTE);
        
        if (StringUtils.hasText(delegatorServlet)) {
            String id = element.getAttribute(ID_ATTRIBUTE);
            
            RootBeanDefinition integrationServlet = new RootBeanDefinition(InternalFrameworkIntegrationServletFactoryBean.class);
            MutablePropertyValues propertyValues = integrationServlet.getPropertyValues();
            propertyValues.addPropertyValue(SERVLET_NAME_PROPERTY, delegatorServlet);
            propertyValues.addPropertyValue(SERVLET_CLASS_PROPERTY, InternalFrameworkIntegrationServlet.class.getName());
            propertyValues.addPropertyValue(DELEGATE_SERVLET_PROPERTY, new RuntimeBeanReference(id));
            
            String beanName = parserContext.getReaderContext().generateBeanName(integrationServlet);
            parserContext.getRegistry().registerBeanDefinition(beanName, integrationServlet);
        }
    }

    @SuppressWarnings("unchecked")
    void handlePropertyElements(
            Element element,
            ParserContext parserContext, 
            BeanDefinitionBuilder builder) {
        
        List<Element> properties = DomUtils.getChildElementsByTagName(element, PROPERTY_ELEMENT);
        for (Element propertyElement : properties) {
            parserContext.getDelegate().parsePropertyElement(propertyElement, builder.getRawBeanDefinition());
        }
    }

    void handleInitParamsAttribute(Element element, Map<String, String> initParameters) {
        
        String initParamsAttribute = element.getAttribute(INIT_PARAMS_ATTRIBUTE);
        if (StringUtils.hasText(initParamsAttribute)) {
            Map<String, String> initParamsFromAttribute = CollectionStringUtils.parsePropertiesFromString(initParamsAttribute);
            initParameters.putAll(initParamsFromAttribute);
        }
    }

    @SuppressWarnings("unchecked")
    void handleInitParametersElement(Element element, Map<String, String> initParameters) {
        
        Element initParams = DomUtils.getChildElementByTagName(element, INIT_PARAMS_ELEMENT);
        if (initParams != null) {

            List<Element> params = DomUtils.getChildElementsByTagName(initParams, PARAM_ELEMENT);
            for (Element param : params) {
                String name = param.getAttribute(NAME_ATTRIBUTE);
                String value = param.getAttribute(VALUE_ATTRIBUTE);
                initParameters.put(name.trim(), value.trim());
            }
        }
    }
    
}
