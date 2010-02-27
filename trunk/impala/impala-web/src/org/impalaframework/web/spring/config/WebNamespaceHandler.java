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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Implementation of {@link NamespaceHandler} interface for <code>web</code>
 * namespace.
 * 
 * @author Phil Zoio
 */
public class WebNamespaceHandler extends NamespaceHandlerSupport {

    private static final Log logger = LogFactory.getLog(WebNamespaceHandler.class);
    
    private static final String SERVLET_ELEMENT =  "servlet";
    private static final String JSP_SERVLET_ELEMENT =  "jsp-servlet";
    private static final String FILTER_ELEMENT =  "filter";
    private static final String MAPPING_ELEMENT =  "mapping";
    private static final String CONTEXT_LISTENER_ELEMENT =  "context-listener";
    
    public void init() {
        
        if (logger.isDebugEnabled()) {
            logger.debug("Setting up " + WebNamespaceHandler.class.getName());
        }

        registerBeanDefinitionParser(SERVLET_ELEMENT, new ServletBeanDefinitionParser());
        registerBeanDefinitionParser(JSP_SERVLET_ELEMENT, new JspServletBeanDefinitionParser());
        registerBeanDefinitionParser(FILTER_ELEMENT, new FilterBeanDefinitionParser());
        registerBeanDefinitionParser(MAPPING_ELEMENT, new WebMappingBeanDefinitionParser());
        registerBeanDefinitionParser(CONTEXT_LISTENER_ELEMENT, new ContextListenerBeanDefinitionParser());
    }
}
