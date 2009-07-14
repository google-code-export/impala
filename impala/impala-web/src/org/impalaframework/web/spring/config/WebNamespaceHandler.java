package org.impalaframework.web.spring.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.web.spring.integration.ServletFactoryBean;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.w3c.dom.Element;

/**
 * Implementation of {@link NamespaceHandler} interface for <code>web</code>
 * namespace.
 * 
 * @author Phil Zoio
 */
public class WebNamespaceHandler extends NamespaceHandlerSupport {

    private static final Log logger = LogFactory.getLog(WebNamespaceHandler.class);
    
    public void init() {
        
        if (logger.isDebugEnabled()) {
            logger.debug("Setting up " + WebNamespaceHandler.class.getName());
        }

        registerBeanDefinitionParser("servlet", new ServletBeanDefinitionParser());
    }
    
    private static class ServletBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {

        @Override
        protected Class<?> getBeanClass(Element element) {
            return ServletFactoryBean.class;
        }

        @Override
        protected boolean shouldGenerateIdAsFallback() {
            return true;
        }
    }
}
