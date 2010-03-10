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

package org.impalaframework.spring.service.config;

import org.impalaframework.spring.bean.NamedFactoryBean;
import org.impalaframework.spring.service.bean.ProxiedNamedFactoryBean;
import org.impalaframework.spring.service.bean.SingletonAwareNamedFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * {@link BeanDefinitionParser} for 'named-bean' element from service registry namespace.
 * @author Phil Zoio
 */
public class NamedBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected Class<?> getBeanClass(Element element) {
        Boolean singletonAware = Boolean.valueOf(element.getAttribute("singletonAware"));
        Boolean proxied = Boolean.valueOf(element.getAttribute("proxied"));
        String proxyTypes = element.getAttribute("proxyTypes");
        
        if (proxied || StringUtils.hasText(proxyTypes)) {
            return ProxiedNamedFactoryBean.class;
        }
        
        if (singletonAware) {
            return SingletonAwareNamedFactoryBean.class;
        }
        
        return NamedFactoryBean.class;
    }
    
    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        
        builder.addPropertyValue("beanName", element.getAttribute("beanName"));
        String proxyTypes = element.getAttribute("proxyTypes");
        if (StringUtils.hasText(proxyTypes)) {
            builder.addPropertyValue("proxyTypes", proxyTypes);
        }
    }
}
