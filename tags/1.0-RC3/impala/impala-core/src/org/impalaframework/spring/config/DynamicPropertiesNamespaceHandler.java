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

package org.impalaframework.spring.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.config.BooleanPropertyValue;
import org.impalaframework.config.DatePropertyValue;
import org.impalaframework.config.DoublePropertyValue;
import org.impalaframework.config.FloatPropertyValue;
import org.impalaframework.config.IntPropertyValue;
import org.impalaframework.config.LongPropertyValue;
import org.impalaframework.config.StringPropertyValue;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.w3c.dom.Element;

/**
 * Implementation of {@link NamespaceHandler} interface for <code>dynamicproperties</code>
 * namespace.
 * 
 * @author Phil Zoio
 */
public class DynamicPropertiesNamespaceHandler extends NamespaceHandlerSupport {

    private static final Log logger = LogFactory.getLog(DynamicPropertiesNamespaceHandler.class);
    
    public void init() {
        
        if (logger.isDebugEnabled()) {
            logger.debug("Setting up " + DynamicPropertiesNamespaceHandler.class.getName());
        }

        registerBeanDefinitionParser("boolean", new BooleanPropertyDefinitionParser());
        registerBeanDefinitionParser("date", new DatePropertyDefinitionParser());
        registerBeanDefinitionParser("double", new DoublePropertyDefinitionParser());
        registerBeanDefinitionParser("float", new FloatPropertyDefinitionParser());
        registerBeanDefinitionParser("int", new IntPropertyDefinitionParser());
        registerBeanDefinitionParser("long", new LongPropertyDefinitionParser());
        registerBeanDefinitionParser("string", new StringPropertyDefinitionParser());
    }
    
    /**
     * Parser for "boolean" element
     * @author Phil Zoio
     */
    private static class BooleanPropertyDefinitionParser extends BasePropertyDefinitionParser {
        @Override
        protected Class<?> getBeanClass(Element element) {
            return BooleanPropertyValue.class;
        }
    }
    
    /**
     * Parser for "date" element
     * @author Phil Zoio
     */
    private static class DatePropertyDefinitionParser extends BasePropertyDefinitionParser {
        @Override
        protected Class<?> getBeanClass(Element element) {
            return DatePropertyValue.class;
        }
    }
    
    /**
     * Parser for "double" element
     * @author Phil Zoio
     */
    private static class DoublePropertyDefinitionParser extends BasePropertyDefinitionParser {
        @Override
        protected Class<?> getBeanClass(Element element) {
            return DoublePropertyValue.class;
        }
    }
    
    /**
     * Parser for "float" element
     * @author Phil Zoio
     */
    private static class FloatPropertyDefinitionParser extends BasePropertyDefinitionParser {
        @Override
        protected Class<?> getBeanClass(Element element) {
            return FloatPropertyValue.class;
        }
    }
    
    /**
     * Parser for "int" element
     * @author Phil Zoio
     */
    private static class IntPropertyDefinitionParser extends BasePropertyDefinitionParser {
        @Override
        protected Class<?> getBeanClass(Element element) {
            return IntPropertyValue.class;
        }
    }
    
    /**
     * Parser for "int" element
     * @author Phil Zoio
     */
    private static class LongPropertyDefinitionParser extends BasePropertyDefinitionParser {
        @Override
        protected Class<?> getBeanClass(Element element) {
            return LongPropertyValue.class;
        }
    }
    
    /**
     * Parser for "string" element
     * @author Phil Zoio
     */
    private static class StringPropertyDefinitionParser extends BasePropertyDefinitionParser {
        @Override
        protected Class<?> getBeanClass(Element element) {
            return StringPropertyValue.class;
        }
    }
    
    private static class BasePropertyDefinitionParser extends AbstractSimpleBeanDefinitionParser {
        @Override
        protected Class<?> getBeanClass(Element element) {
            return BooleanPropertyValue.class;
        }
        
        @Override
        protected boolean isEligibleAttribute(String attributeName) {
            if ("propertySource".equals(attributeName)) {
                return false;
            }
            return super.isEligibleAttribute(attributeName);
        }
        
        @Override
        protected void postProcess(BeanDefinitionBuilder beanDefinition, Element element) {
            super.postProcess(beanDefinition, element);
            String propertySource = element.getAttribute("propertySource");
            beanDefinition.addPropertyReference("propertySource", propertySource);
        }
    }
}
