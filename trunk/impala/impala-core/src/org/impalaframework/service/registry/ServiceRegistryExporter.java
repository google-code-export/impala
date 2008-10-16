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

package org.impalaframework.service.registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionAware;
import org.impalaframework.service.ServiceRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class ServiceRegistryExporter implements ServiceRegistryAware, BeanFactoryAware, InitializingBean, DisposableBean, ModuleDefinitionAware, BeanClassLoaderAware {

	private String beanName;
	
	private String exportName;
	
	private List<String> tags;
	
	private Map<String, String> attributes;
	
	private ModuleDefinition moduleDefinition;
	
	private ServiceRegistry serviceRegistry;

	private BeanFactory beanFactory;
	
	private Object service;

	private ClassLoader beanClassLoader;
	
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(beanName, "beanName cannot be null");
		Assert.notNull(serviceRegistry);
		Assert.notNull(beanFactory);
		Assert.notNull(moduleDefinition);
		
		if (exportName == null) {
			exportName = beanName;
		}
		
		service = beanFactory.getBean(beanName);
		serviceRegistry.addService(exportName, moduleDefinition.getName(), service, tags, attributes, beanClassLoader);
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

	public void setBeanClassLoader(ClassLoader classLoader) {
		this.beanClassLoader = classLoader;
	}

}
