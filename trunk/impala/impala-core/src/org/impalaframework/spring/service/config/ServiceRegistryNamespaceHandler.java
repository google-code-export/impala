package org.impalaframework.spring.service.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.spring.service.exporter.ServiceRegistryExporter;
import org.impalaframework.spring.service.proxy.FilteredServiceProxyFactoryBean;
import org.impalaframework.spring.service.proxy.NamedServiceProxyFactoryBean;
import org.impalaframework.spring.service.proxy.TypedServiceProxyFactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
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
    
    private static class ImportBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {
        
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
            if (StringUtils.hasText(element.getAttribute("filterExpression"))) {
                return FilteredServiceProxyFactoryBean.class;
            }
            
            if (StringUtils.hasText(element.getAttribute("exportName"))) {
                return NamedServiceProxyFactoryBean.class;
            }
            return TypedServiceProxyFactoryBean.class;
        }
        
    }
    
    private static class ListBeanDefinitionParser implements BeanDefinitionParser {

        public BeanDefinition parse(Element element, ParserContext parserContext) {
            //FIXME provide implementation of this ...
            return null;
        }

    }
    
    private static class MapBeanDefinitionParser implements BeanDefinitionParser {

        public BeanDefinition parse(Element element, ParserContext parserContext) {
            //FIXME provide implementation of this ...
            return null;
        }
    }
    
    private static class AutoExportBeanDefinitionParser implements BeanDefinitionParser {

        public BeanDefinition parse(Element element, ParserContext parserContext) {
            //FIXME provide implementation of this ...
            return null;
        }
    }
}
