package org.impalaframework.spring.service.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.beans.factory.xml.ParserContext;
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
        registerBeanDefinitionParser("auto-export", new MapBeanDefinitionParser());
    }

    private static class ExportBeanDefinitionParser implements BeanDefinitionParser {

        public BeanDefinition parse(Element element, ParserContext parserContext) {
            //FIXME provide implementation of this ...
            return null;
        }

    }
    
    private static class ImportBeanDefinitionParser implements BeanDefinitionParser {

        public BeanDefinition parse(Element element, ParserContext parserContext) {
            //FIXME provide implementation of this ...
            return null;
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
