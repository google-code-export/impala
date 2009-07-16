package org.impalaframework.web.spring.config;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.exception.ExecutionException;
import org.impalaframework.web.spring.integration.ServletFactoryBean;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 * Implementation of {@link NamespaceHandler} interface for <code>web</code>
 * namespace.
 * 
 * @author Phil Zoio
 */
public class WebNamespaceHandler extends NamespaceHandlerSupport implements BeanClassLoaderAware {

    private static final Log logger = LogFactory.getLog(WebNamespaceHandler.class);
    
    private static final String SERVLET_ELEMENT =  "servlet";
    private static final String INIT_PARAMS_ELEMENT =  "init-parameters";
    private static final String PARAM_ELEMENT =  "param";
   
    private static final String FACTORY_CLASS_ATTRIBUTE =  "factoryClass";
    private static final String NAME_ATTRIBUTE = "name";
    private static final String VALUE_ATTRIBUTE = "value";
    
    private static final String INIT_PARAMS_PROPERTY=  "initParameters";
    
    private ClassLoader classLoader;
    
    public void init() {
        
        if (logger.isDebugEnabled()) {
            logger.debug("Setting up " + WebNamespaceHandler.class.getName());
        }

        registerBeanDefinitionParser(SERVLET_ELEMENT, new ServletBeanDefinitionParser());
    }
    
    private class ServletBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {

        @Override
        protected Class<?> getBeanClass(Element element) {
            String factoryClass = element.getAttribute(FACTORY_CLASS_ATTRIBUTE);
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
            if (FACTORY_CLASS_ATTRIBUTE.equals(attributeName)) {
                return false;
            }
            return super.isEligibleAttribute(attributeName);
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void doParse(Element element, ParserContext parserContext,
                BeanDefinitionBuilder builder) {
            super.doParse(element, parserContext, builder);
            Element initParams = DomUtils.getChildElementByTagName(element, INIT_PARAMS_ELEMENT);
            if (initParams != null) {

                Map<String,String> properties = new LinkedHashMap<String,String>();
                List<Element> params = DomUtils.getChildElementsByTagName(initParams, PARAM_ELEMENT);
                for (Element param : params) {
                    String name = param.getAttribute(NAME_ATTRIBUTE);
                    String value = param.getAttribute(VALUE_ATTRIBUTE);
                    properties.put(name.trim(), value.trim());
                }
                
                // Specific environment settings defined, overriding any shared properties.
                builder.addPropertyValue(INIT_PARAMS_PROPERTY, properties);
            }
        }
    }

    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
}
