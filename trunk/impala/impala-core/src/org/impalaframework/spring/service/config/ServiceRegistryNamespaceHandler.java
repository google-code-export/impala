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

package org.impalaframework.spring.service.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.spring.service.bean.ParentFactoryBean;
import org.impalaframework.spring.service.contribution.ServiceRegistryList;
import org.impalaframework.spring.service.contribution.ServiceRegistryMap;
import org.impalaframework.spring.service.contribution.ServiceRegistrySet;
import org.impalaframework.spring.service.exporter.NamedServiceAutoExportPostProcessor;
import org.impalaframework.spring.service.exporter.ServiceArrayRegistryExporter;
import org.impalaframework.spring.service.exporter.ServiceRegistryExporter;
import org.impalaframework.spring.service.proxy.FilteredServiceProxyFactoryBean;
import org.impalaframework.spring.service.proxy.NamedServiceProxyFactoryBean;
import org.impalaframework.spring.service.proxy.TypedServiceProxyFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * Implementation of {@link NamespaceHandler} interface for <code>service</code>
 * namespace.
 * 
 * @author Phil Zoio
 */
public class ServiceRegistryNamespaceHandler extends NamespaceHandlerSupport {

    private static final Log logger = LogFactory.getLog(ServiceRegistryNamespaceHandler.class);
    
    public void init() {
        
        if (logger.isDebugEnabled()) {
            logger.debug("Setting up " + ServiceRegistryNamespaceHandler.class.getName());
        }

        registerBeanDefinitionParser("named-bean", new NamedBeanDefinitionParser());
        registerBeanDefinitionParser("parent", new ParentBeanDefinitionParser());
        registerBeanDefinitionParser("export", new ExportBeanDefinitionParser());
        registerBeanDefinitionParser("import", new ImportBeanDefinitionParser());
        registerBeanDefinitionParser("list", new ListBeanDefinitionParser());
        registerBeanDefinitionParser("set", new SetBeanDefinitionParser());
        registerBeanDefinitionParser("map", new MapBeanDefinitionParser());
        registerBeanDefinitionParser("export-array", new ArrayExportDefinitionParser());
        registerBeanDefinitionParser("auto-export", new AutoExportBeanDefinitionParser());
    }
    
    private static class ParentBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {
        @Override
        protected Class<?> getBeanClass(Element element) {
            return ParentFactoryBean.class;
        }
    }
    
    private static class ExportBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {

        /**
         * Parses the <code>export</code> element. An example is below.
         * 
         * <pre class = "code">
         * &lt;service:export beanName = "beanToBeExported" 
         *         exportName = "optionalNameInExportRegistry"
         *         exportTypes = "optionalCommaSeparatedListOfTypes"
         *         attributes = "attributeMapStringWhichWillBeParseToMap"/&gt;
         * </pre>
         */
        @Override
        protected Class<?> getBeanClass(Element element) {
            return ServiceRegistryExporter.class;
        }

        @Override
        protected boolean shouldGenerateIdAsFallback() {
            return true;
        }
    }
    
    static class ImportBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {
        
        /**
         * Parses the <code>export</code> element. An example is below.
         * 
         * <pre class = "code">
         * &lt;service:import 
         *         exportName = "optionalNameInExportRegistry"
         *         exportTypes = "optionalCommaSeparatedListOfTypes"
         *         filterExpression = "(name=value)"/&gt;
         * </pre>
         */
        
        @Override
        protected Class<?> getBeanClass(Element element) {
            if (hasAttribute(element, "filterExpression")) {
                return FilteredServiceProxyFactoryBean.class;
            }
            
            final boolean hasExportTypes = hasAttribute(element, "exportTypes");
            
            if (hasExportTypes) {
                return TypedServiceProxyFactoryBean.class;
            }
            
            return NamedServiceProxyFactoryBean.class;
        }

        @Override
        protected void postProcess(BeanDefinitionBuilder beanDefinition,
                Element element) {
            
            boolean hasProxyTypes = hasAttribute(element, "proxyTypes");
            boolean hasExportTypes = hasAttribute(element, "exportTypes");

            boolean hasFilterExpression = hasAttribute(element, "filterExpression");
            boolean hasExportName = hasAttribute(element, "exportName");

            if (!hasExportTypes && !hasFilterExpression && !hasExportName) {
                hasExportName = true;
                String id = element.getAttribute(ID_ATTRIBUTE);
                beanDefinition.addPropertyValue("exportName", id);
            }
            
            if (hasExportName) {
                if (hasFilterExpression) {
                    throw new BeanDefinitionValidationException("The 'exportName' attribute has been specified, which cannot be used with the 'filterExpression' attribute " + inNameString("import"));
                }
                
                if (!hasProxyTypes && !hasExportTypes) {
                    throw new BeanDefinitionValidationException("The 'exportName' attribute has been specified, requiring also that the 'proxyTypes' be specified" + inNameString("import"));
                }
            } else {
                if (!hasProxyTypes && !hasExportTypes) {
                    throw new BeanDefinitionValidationException("Either 'exportTypes' or 'proxyTypes' must be specified" + inNameString("import"));
                }
            }
        }

        private String inNameString(String elementName) {
            return ", in Impala 'service' namespace, '" + elementName + "' element";
        }

        private boolean hasAttribute(Element element, String attribute) {
            return StringUtils.hasText(element.getAttribute(attribute));
        }
    }
    
    static class ListBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {

        @Override
        protected Class<?> getBeanClass(Element element) {
            return ServiceRegistryList.class;
        }

    }
    
    static class SetBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {

        @Override
        protected Class<?> getBeanClass(Element element) {
            return ServiceRegistrySet.class;
        }

    }
    
    static class MapBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {

        @Override
        protected Class<?> getBeanClass(Element element) {
            return ServiceRegistryMap.class;
        }
    }
    
    static class ArrayExportDefinitionParser extends AbstractSimpleBeanDefinitionParser {

        @Override
        protected Class<?> getBeanClass(Element element) {
            return ServiceArrayRegistryExporter.class;
        }

        @Override
        protected boolean shouldGenerateIdAsFallback() {
            return true;
        }
    }
    
    static class AutoExportBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {

        @Override
        protected Class<?> getBeanClass(Element element) {
            return NamedServiceAutoExportPostProcessor.class;
        }

        @Override
        protected boolean shouldGenerateIdAsFallback() {
            return true;
        }
    }
}
