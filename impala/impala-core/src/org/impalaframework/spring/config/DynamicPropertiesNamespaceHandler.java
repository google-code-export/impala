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

package org.impalaframework.spring.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.config.BooleanPropertyValue;
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

        registerBeanDefinitionParser("boolean", new BooeleanPropertyDefinitionParser());
    }
    
    private static class BooeleanPropertyDefinitionParser extends AbstractSimpleBeanDefinitionParser {
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
