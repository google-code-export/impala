package org.impalaframework.service.registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.spring.module.ModuleDefinitionAware;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class ServiceRegistryExporter implements ServiceRegistryAware, BeanFactoryAware, InitializingBean, DisposableBean, ModuleDefinitionAware {

	private String beanName;
	
	private String exportName;
	
	private List<String> tags;
	
	private Map<String, String> attributes;
	
	private ModuleDefinition moduleDefinition;
	
	private ServiceRegistry serviceRegistry;

	private BeanFactory beanFactory;
	
	private Object service;
	
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(beanName, "beanName cannot be null");
		Assert.notNull(serviceRegistry);
		Assert.notNull(beanFactory);
		Assert.notNull(moduleDefinition);
		
		if (exportName == null) {
			exportName = beanName;
		}
		
		service = beanFactory.getBean(beanName);
		serviceRegistry.addService(exportName, moduleDefinition.getName(), service, tags, attributes);
	}
	
	public void destroy() throws Exception {
		serviceRegistry.remove(service);
	}

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}	

	public void setModuleDefinition(ModuleDefinition moduleDefinition) {
		this.moduleDefinition = moduleDefinition;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public void setExportName(String exportName) {
		this.exportName = exportName;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	public void setTagsArray(String[] tags) {
		Assert.notNull(tags);
		this.tags = new ArrayList<String>(Arrays.asList(tags));
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

}
