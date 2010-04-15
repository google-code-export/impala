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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.web.servlet.invocation.ModuleInvokerContributor;
import org.impalaframework.web.spring.integration.ContextAndServletPath;
import org.impalaframework.web.spring.integration.ModuleUrlPrefixContributor;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 * Responsible for handling module URL mappings, both using the 'prefix' element, which maps
 * URLs by prefix to this module, and the 'suffix' element, which maps requests directed to this module
 * to combinations of filters and/or servlets.
 * @author Phil Zoio
 */
public class WebMappingBeanDefinitionParser implements BeanDefinitionParser {
    
    private static final Log logger = LogFactory.getLog(WebMappingBeanDefinitionParser.class);
    
    private static final String TO_RESOURCE_ELEMENT = "to-handler";
    private static final String TO_MDOULE_ELEMENT = "to-module";

    /**
     * Prefix used to map an entry in a 'to-module' element to the current module
     */
    private static final String PREFIX_ATTRIBUTE = "prefix";
    
    /**
     * Whether to set the servlet path in the request passed to servlets or filters in this module. If 
     * true, and 'servletPath' attribute is not specified, then uses the mapping specified using the 'path'
     * attribute. If 'servletPath' attribute is specified, then uses this value.
     */
    private static final String SET_SERVLET_PATH_ATTRIBUTE = "setServletPath";
    
    /**
     * If a value is set for this optional attribute, 
     * then as long as 'setServletPath' is not set with a value of false, then sets 
     * the servlet path in the request passed to servlets or filters in this module.
     */
    private static final String SERVLET_PATH_ATTRIBUTE = "servletPath";
    
    /**
     * Whether to set the context path in the request passed to servlets or filters in this module. If 
     * true, and 'contextPath' attribute is not specified, then uses the mapping specified using the 'path'
     * attribute. If 'servletPath' attribute is specified, then uses this value.
     */
    private static final String SET_CONTEXT_PATH_ATTRIBUTE = "setContextPath";
    
    /**
     * If a value is set for this optional attribute, 
     * then as long as 'setContextPath' is not set with a value of false, then sets 
     * the servlet path in the request passed to servlets or filters in this module.
     */
    private static final String CONTEXT_PATH_ATTRIBUTE = "contextPath";
    
    /**
     * Used in the 'suffix' element to map an extension to a servlet and/or list of filters. Special values include:
     * <ul>
     * <li>* - denotes all extensions
     * <li>[none] - denotes a path without an extension
     * </ul>
     */
    private static final String EXTENSION_ATTRIBUTE = "extension";
    
    /**
     * Used in the 'suffix' element to name the servlet to which requests with URI extensions as specified using the
     * 'extension' attribute are mapped. Only a single named servlet may be specified.
     */
    private static final String SERVLET_NAME_ATTTRIBUTE = "servletName";
    
    /**
     * Used in the 'suffix' element to name the filters to which requests with URI extensions as specified using the
     * 'extension' attribute are mapped. A list of named filters may be specified. During invocation of the request,
     * each filter's {@link Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}
     * method is invoked in the order specified in the 'filterNames' attribute declaration.
     */
    private static final String FILTER_NAMES = "filterNames";
    
    /**
     * Refers to {@link ModuleUrlPrefixContributor#setPrefixMap(Map)}
     */
    private static final String PREFIX_MAP_PROPERTY = "prefixMap";

    public BeanDefinition parse(Element element, ParserContext parserContext) {
        handlePrefixes(element, parserContext);
        handleSuffixes(element, parserContext);
        return null;
    }
    
    /* ******************* methods to support prefix mappings ****************** */

    @SuppressWarnings("unchecked")
    private void handlePrefixes(Element element, ParserContext parserContext) {
        List<Element> toModules = DomUtils.getChildElementsByTagName(element, TO_MDOULE_ELEMENT);
        
        Map<String,ContextAndServletPath> toModulesMap = new LinkedHashMap<String, ContextAndServletPath>();
        
        for (Element toModulesElement : toModules) {
            String pathAttribute = toModulesElement.getAttribute(PREFIX_ATTRIBUTE);
            
            String servletPath = getPathAttributeValue(toModulesElement, pathAttribute, SET_SERVLET_PATH_ATTRIBUTE, SERVLET_PATH_ATTRIBUTE);
            String contextPath = getPathAttributeValue(toModulesElement, pathAttribute, SET_CONTEXT_PATH_ATTRIBUTE, CONTEXT_PATH_ATTRIBUTE);    
            
            //check that not relying on both setServletPath and setContextPath only
            if (toModulesElement.hasAttribute(SET_SERVLET_PATH_ATTRIBUTE) && 
                    toModulesElement.hasAttribute(SET_CONTEXT_PATH_ATTRIBUTE) && 
                    !toModulesElement.hasAttribute(SERVLET_PATH_ATTRIBUTE) && 
                    !toModulesElement.hasAttribute(CONTEXT_PATH_ATTRIBUTE)) {
                logger.warn("Both 'setServletPath' and 'setContextPath' are used for to-handler attribute. This is ordinarily not required, and is not a recommended usage.");
            }
            
            ContextAndServletPath paths = new ContextAndServletPath(contextPath, servletPath);
            toModulesMap.put(pathAttribute.trim(), paths);
        }

        RootBeanDefinition definition = newContributorDefinition(toModulesMap);
        registerDefinition(parserContext, definition);
    }

    private String getPathAttributeValue(Element toModulesElement,
            String pathAttribute, final String setPathAttributeName,
            final String pathAttributeName) {
        String setServletPathAttribute = toModulesElement.getAttribute(setPathAttributeName);
        String servletPathAttribute = toModulesElement.getAttribute(pathAttributeName);
        if (!StringUtils.hasText(servletPathAttribute)) {
            servletPathAttribute = toModulesElement.hasAttribute(pathAttributeName) ? "" : null;
        }
        
        String servletPath = getServletPath(pathAttribute, setServletPathAttribute, servletPathAttribute);
        return servletPath;
    }

    String getServletPath(String prefixAttribute, String setPathAttribute, String pathAttribute) {
        
        String servletPath = null;
        if (StringUtils.hasText(setPathAttribute)) {
            boolean setServletPath = Boolean.parseBoolean(setPathAttribute);
            if (setServletPath) {
                servletPath = (pathAttribute != null ? pathAttribute : prefixAttribute);
            }
        } else {
            boolean setServletPath = pathAttribute != null;
            if (setServletPath) {
                servletPath = pathAttribute;
            }
        }
        return servletPath != null ? servletPath.trim() : null;
    }

    RootBeanDefinition newContributorDefinition(Map<String, ContextAndServletPath> prefixMap) {
        RootBeanDefinition definition = new RootBeanDefinition(ModuleUrlPrefixContributor.class);
        MutablePropertyValues propertyValues = definition.getPropertyValues();
        propertyValues.addPropertyValue(PREFIX_MAP_PROPERTY, prefixMap);
        return definition;
    }

    /* ******************* methods to support suffix mappings ****************** */
    
    @SuppressWarnings("unchecked")
    private void handleSuffixes(Element element, ParserContext parserContext) {
        
        List<Element> suffixes = DomUtils.getChildElementsByTagName(element, TO_RESOURCE_ELEMENT);
        
        for (Element suffixElement : suffixes) {
            String extensionAttribute = suffixElement.getAttribute(EXTENSION_ATTRIBUTE);
            String servletNameAttribute = suffixElement.getAttribute(SERVLET_NAME_ATTTRIBUTE);
            String filterNamesAttribute = suffixElement.getAttribute(FILTER_NAMES);
           
            String[] filterNames = getFilterNames(filterNamesAttribute);
            
            String servletName = getServletName(servletNameAttribute);

            RootBeanDefinition definition = new RootBeanDefinition(ModuleInvokerContributor.class);
            MutablePropertyValues propertyValues = definition.getPropertyValues();
            propertyValues.addPropertyValue("suffix", extensionAttribute);
            propertyValues.addPropertyValue("servletName", servletName);
            propertyValues.addPropertyValue("filterNames", filterNames);
            
            registerDefinition(parserContext, definition);
        }
    }

    String getServletName(String servletNameAttribute) {
        String servletName = StringUtils.hasText(servletNameAttribute) ? 
                servletNameAttribute.trim() :
                null;
        return servletName;
    }

    String[] getFilterNames(String filterNamesAttribute) {
        String[] filterNames = 
            StringUtils.hasText(filterNamesAttribute) ? 
            StringUtils.tokenizeToStringArray(filterNamesAttribute, " ,") :
            null;
        return filterNames;
    }

    void registerDefinition(ParserContext parserContext,
            RootBeanDefinition definition) {
        String beanName = parserContext.getReaderContext().generateBeanName(definition);
        parserContext.getRegistry().registerBeanDefinition(beanName, definition);
    }
}
