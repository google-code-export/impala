package org.impalaframework.web.spring.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.exception.ExecutionException;
import org.impalaframework.web.spring.integration.ServletFactoryBean;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * Implementation of {@link NamespaceHandler} interface for <code>web</code>
 * namespace.
 * 
 * @author Phil Zoio
 */
public class WebNamespaceHandler extends NamespaceHandlerSupport implements BeanClassLoaderAware {

    private static final Log logger = LogFactory.getLog(WebNamespaceHandler.class);
    
    private ClassLoader classLoader;
    
    public void init() {
        
        if (logger.isDebugEnabled()) {
            logger.debug("Setting up " + WebNamespaceHandler.class.getName());
        }

        registerBeanDefinitionParser("servlet", new ServletBeanDefinitionParser());
    }
    
    private class ServletBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {

        @Override
        protected Class<?> getBeanClass(Element element) {
            String factoryClass = element.getAttribute("factoryClass");
            if (StringUtils.hasText(factoryClass)) {
                
                try {
                    ClassUtils.forName(factoryClass, classLoader);
                }
                catch (Throwable e) {
                    throw new ExecutionException("Unable to load class: " + factoryClass + ": " + e.getMessage(), e);
                }
            }
            return ServletFactoryBean.class;
        }
        
        @Override
        protected boolean isEligibleAttribute(String attributeName) {
            boolean equals = "factoryClass".equals(attributeName);
            if (equals) {
                return false;
            }
            return super.isEligibleAttribute(attributeName);
        }
        
        @Override
        protected String extractPropertyName(String attributeName) {
            return super.extractPropertyName(attributeName);
        }

        @Override
        protected boolean shouldGenerateIdAsFallback() {
            return true;
        }
    }

    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
}
