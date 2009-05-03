package org.impalaframework.spring.service.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.spring.service.exporter.ServiceRegistryExporter;
import org.impalaframework.spring.service.proxy.FilteredServiceProxyFactoryBean;
import org.impalaframework.spring.service.proxy.NamedServiceProxyFactoryBean;
import org.impalaframework.spring.service.proxy.TypedServiceProxyFactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.beans.factory.xml.ParserContext;
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
        
        registerBeanDefinitionParser("export", new ExportBeanDefinitionParser());
        registerBeanDefinitionParser("import", new ImportBeanDefinitionParser());
        registerBeanDefinitionParser("list", new ListBeanDefinitionParser());
        registerBeanDefinitionParser("map", new MapBeanDefinitionParser());
        registerBeanDefinitionParser("auto-export", new AutoExportBeanDefinitionParser());
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
        
        //FIXME test this method
        
        @Override
        protected Class<?> getBeanClass(Element element) {
            if (hasAttribute(element, "filterExpression")) {
                return FilteredServiceProxyFactoryBean.class;
            }
            
            if (hasAttribute(element, "exportName")) {
                return NamedServiceProxyFactoryBean.class;
            }
            return TypedServiceProxyFactoryBean.class;
        }

        @Override
        protected void postProcess(BeanDefinitionBuilder beanDefinition,
                Element element) {
            
            boolean hasProxyTypes = hasAttribute(element, "proxyTypes");
            boolean hasExportTypes = hasAttribute(element, "exportTypes");

            boolean hasFilterExpression = hasAttribute(element, "filterExpression");
            boolean hasExportName = hasAttribute(element, "exportName");
            
            if (!hasExportTypes && !hasFilterExpression && !hasExportName) {
                    throw new BeanDefinitionValidationException("Either 'exportTypes', 'filterExpression' or 'exportName' must be specified" + inNameString("import"));
            }
            
            if (hasExportName) {
                if (hasExportTypes || hasFilterExpression) {
                    throw new BeanDefinitionValidationException("The 'exportName' attribute has been specified, which cannot be used with the 'filterExpression' or 'exportTypes' attributes" + inNameString("import"));
                }
                
                if (!hasProxyTypes) {
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
    
    static class ListBeanDefinitionParser implements BeanDefinitionParser {

        public BeanDefinition parse(Element element, ParserContext parserContext) {
            //FIXME provide implementation of this ...
            return null;
        }

    }
    
    static class MapBeanDefinitionParser implements BeanDefinitionParser {

        public BeanDefinition parse(Element element, ParserContext parserContext) {
            //FIXME provide implementation of this ...
            return null;
        }
    }
    
    static class AutoExportBeanDefinitionParser implements BeanDefinitionParser {

        public BeanDefinition parse(Element element, ParserContext parserContext) {
            //FIXME provide implementation of this ...
            return null;
        }
    }
}
