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
 * Base bean definition parser for <i>servlet</i> and <i>filter</i> elements from <i>web</i> namespace.
 * 
 * @author Phil Zoio
 */
public abstract class AbstractWebHandlerBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {

    private static final String INIT_PARAMS_ELEMENT =  "init-parameters";
    private static final String PARAM_ELEMENT =  "param";
    private static final String PROPERTY_ELEMENT = "property";    
    private static final String INIT_PARAMS_ATTRIBUTE =  "initParameters";
    private static final String FACTORY_CLASS_ATTRIBUTE =  "factoryClass";
    private static final String NAME_ATTRIBUTE = "name";
    private static final String VALUE_ATTRIBUTE = "value";
    
    private static final String INIT_PARAMS_PROPERTY=  "initParameters";
    
    public AbstractWebHandlerBeanDefinitionParser() {
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
        Class<?> beanClass = guessBeanClass(element);
        if (beanClass != null) {
            return beanClass;
        }
        return getDefaultFactoryBeanClass();
    }
    
    protected Class<?> guessBeanClass(Element element) {
        return null;
    }

    @Override
    protected boolean isEligibleAttribute(String attributeName) {
        
        if (FACTORY_CLASS_ATTRIBUTE.equals(attributeName) 
            || INIT_PARAMS_ATTRIBUTE.equals(attributeName)
            || (getDelegatorHandlerNameAttribute() != null && getDelegatorHandlerNameAttribute().equals(attributeName))) {
            return false;
        }
        return super.isEligibleAttribute(attributeName);
    }

    @Override
    protected void doParse(Element element, 
            ParserContext parserContext,
            BeanDefinitionBuilder builder) {
        
        super.doParse(element, parserContext, builder);
        
        String handlerNameProperty = getHandlerNameProperty();
        if (!StringUtils.hasText(element.getAttribute(handlerNameProperty))) {
            builder.addPropertyValue(getHandlerNameProperty(), element.getAttribute(ID_ATTRIBUTE));
        }
        
        handleHandlerClass(element, builder);

        handleInitParameters(element, builder);
        
        handlePropertyElements(element, parserContext, builder);
        
        handleDelegatorServletAttribute(element, parserContext);
    }

    protected void handleHandlerClass(Element element, BeanDefinitionBuilder builder) {
    }

    void handleInitParameters(Element element, BeanDefinitionBuilder builder) {
        
        Map<String,String> initParameters = new LinkedHashMap<String,String>();
        handleInitParamsAttribute(element, initParameters);
        handleInitParametersElement(element, initParameters);
        
        // Specific environment settings defined, overriding any shared properties.
        builder.addPropertyValue(INIT_PARAMS_PROPERTY, initParameters);
    }

    void handleDelegatorServletAttribute(Element element, ParserContext parserContext) {
        
        final String delegatorHandlerNameAttribute = getDelegatorHandlerNameAttribute();
        
        if (delegatorHandlerNameAttribute != null) {
            String delegatorServletName = element.getAttribute(delegatorHandlerNameAttribute);
            
            if (StringUtils.hasText(delegatorServletName)) {
                String id = element.getAttribute(ID_ATTRIBUTE);
                
                Class<?> beanClass = getIntegrationHandlerFactoryClass();
                RootBeanDefinition handlerDefinition = new RootBeanDefinition(beanClass);
                MutablePropertyValues propertyValues = handlerDefinition.getPropertyValues();
                propertyValues.addPropertyValue(getHandlerNameProperty(), delegatorServletName);
                propertyValues.addPropertyValue(getHandlerClassProperty(), getIntegrationHandlerClassName());
                propertyValues.addPropertyValue(getDelegateHandlerProperty(), new RuntimeBeanReference(id));
                
                String beanName = parserContext.getReaderContext().generateBeanName(handlerDefinition);
                parserContext.getRegistry().registerBeanDefinition(beanName, handlerDefinition);
            }
        }
    }

    protected String getIntegrationHandlerClassName() {
        return getIntegrationHandlerClass().getName();
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

    protected abstract String getHandlerClassAttribute();

    protected abstract Class<?> getDefaultFactoryBeanClass();

    protected abstract Class<?> getIntegrationHandlerFactoryClass();

    protected abstract Class<?> getIntegrationHandlerClass();

    protected abstract String getDelegateHandlerProperty();
    
    protected abstract String getDelegatorHandlerNameAttribute();
    
    protected abstract String getHandlerNameProperty();

    protected abstract String getHandlerClassProperty();

}
