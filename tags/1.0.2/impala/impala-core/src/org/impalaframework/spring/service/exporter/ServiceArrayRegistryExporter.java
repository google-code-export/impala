 package org.impalaframework.spring.service.exporter;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionAware;
import org.impalaframework.service.ServiceBeanReference;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.ServiceRegistryEntry;
import org.impalaframework.service.registry.ServiceRegistryAware;
import org.impalaframework.spring.service.SpringServiceBeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.Assert;

/**
 * Exports the named beans to the {@link ServiceRegistry}. An array of export
 * names can be optionally provided (see {@link #exportNames}, but this array
 * must be the same in length as {@link #beanNames}.
 * 
 * Registry entries are added when {@link InitializingBean#afterPropertiesSet()} is invoked, and removed when 
 * {@link DisposableBean#destroy()} is invoked.
 * 
 * @author Phil Zoio
 */
public class ServiceArrayRegistryExporter 
    implements ServiceRegistryAware, 
                BeanFactoryAware,
                ModuleDefinitionAware, 
                BeanClassLoaderAware,
                ApplicationListener {
    
    private static final Log logger = LogFactory.getLog(NamedServiceAutoExportPostProcessor.class);

    private String[] beanNames;
    
    private String[] exportNames;
    
    private ModuleDefinition moduleDefinition;
    
    private ServiceRegistry serviceRegistry;

    private BeanFactory beanFactory;
    
    private Set<ServiceRegistryEntry> services = new HashSet<ServiceRegistryEntry>();

    private ClassLoader beanClassLoader;
    
    /* *************** Application Event ************** */
    
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
           logger.info("################################ " + this.getClass().getName() + " - context refreshed for " + moduleDefinition.getName() + " ####################");
           this.init();
        } else if (event instanceof ContextClosedEvent) {
           logger.info("################################ " + this.getClass().getName() + " - context closed for " + moduleDefinition.getName() + " ####################");
           this.destroy(); 
        }
    }
    
    public void init() {
        Assert.notNull(beanNames, "beanNames cannot be null");
        Assert.notNull(serviceRegistry);
        Assert.notNull(beanFactory);
        Assert.notNull(moduleDefinition);
        
        if (exportNames == null) {
            exportNames = beanNames;
        } else {
            if (exportNames.length != beanNames.length) {
                throw new ConfigurationException("beanNames array length [" + beanNames.length + "] is not the same length as exportNames array [" + exportNames.length + "]");
            }
        }
        
        for (int i = 0; i < beanNames.length; i++) {
            String beanName = beanNames[i];
            final ServiceBeanReference beanReference = SpringServiceBeanUtils.newServiceBeanReference(beanFactory, beanName);
            final ServiceRegistryEntry serviceReference = serviceRegistry.addService(exportNames[i], moduleDefinition.getName(), beanReference, beanClassLoader);
            services.add(serviceReference);
        }
    }
    
    public void destroy() {
        for (ServiceRegistryEntry service : services) {
            serviceRegistry.remove(service);
        }
    }

    public void setServiceRegistry(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }       
    
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    public void setModuleDefinition(ModuleDefinition moduleDefinition) {
        this.moduleDefinition = moduleDefinition;
    }

    public void setBeanNames(String[] beanNames) {
        this.beanNames = beanNames;
    }

    public void setExportNames(String[] exportNames) {
        this.exportNames = exportNames;
    }

}
