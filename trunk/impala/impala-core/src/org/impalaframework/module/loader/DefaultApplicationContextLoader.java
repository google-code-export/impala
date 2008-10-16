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

package org.impalaframework.module.loader;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.ApplicationContextLoader;
import org.impalaframework.module.DelegatingContextLoader;
import org.impalaframework.module.ModuleLoader;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.monitor.ModuleChangeMonitor;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.registry.ServiceRegistryPostProcessor;
import org.impalaframework.spring.module.ModuleDefinitionPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * @author Phil Zoio
 */
public class DefaultApplicationContextLoader implements ApplicationContextLoader {
	
	private static Log logger = LogFactory.getLog(DefaultApplicationContextLoader.class);
	
	private ModuleLoaderRegistry moduleLoaderRegistry;

	private ServiceRegistry serviceRegistry;
	
	private ModuleChangeMonitor moduleChangeMonitor;

	public DefaultApplicationContextLoader() {
	}

	public ConfigurableApplicationContext loadContext(ModuleDefinition definition, ApplicationContext parent) {

		Assert.notNull(moduleLoaderRegistry, ModuleLoaderRegistry.class.getName() + " cannot be null");
		Assert.notNull(serviceRegistry, ServiceRegistry.class.getName() + " cannot be null");
		ConfigurableApplicationContext context = null;
		
		final ModuleLoader moduleLoader = moduleLoaderRegistry.getModuleLoader(definition.getType(), false);
		final DelegatingContextLoader delegatingLoader = moduleLoaderRegistry.getDelegatingLoader(definition.getType());

		try {

			if (moduleLoader != null) {
				if (logger.isDebugEnabled()) logger.debug("Loading module " + definition + " using ModuleLoader " + moduleLoader);
				context = loadApplicationContext(moduleLoader, parent, definition);
				moduleLoader.afterRefresh(context, definition);
			}
			else if (delegatingLoader != null) {
				if (logger.isDebugEnabled()) logger.debug("Loading module " + definition + " using DelegatingContextLoader " + moduleLoader);
				context = delegatingLoader.loadApplicationContext(parent, definition);
			}
			else {
				throw new ConfigurationException("No " + ModuleLoader.class.getName() + " or "
						+ DelegatingContextLoader.class.getName() + " specified for module definition type " + definition.getType());
			}

		}
		finally {
			if (moduleLoader != null) {
				Resource[] toMonitor = moduleLoader.getClassLocations(definition);
				if (moduleChangeMonitor != null) {
					if (logger.isDebugEnabled()) logger.debug("Monitoring resources " + Arrays.toString(toMonitor) + " using ModuleChangeMonitor " + moduleChangeMonitor);
					moduleChangeMonitor.setResourcesToMonitor(definition.getName(), toMonitor);
				}
			}
		}

		return context;
	}

	private ConfigurableApplicationContext loadApplicationContext(final ModuleLoader moduleLoader,
			ApplicationContext parent, ModuleDefinition definition) {

		ClassLoader existing = ClassUtils.getDefaultClassLoader();

		// note that existing class loader is not used to figure out parent
		ClassLoader classLoader = moduleLoader.newClassLoader(definition, parent);

		try {
			Thread.currentThread().setContextClassLoader(classLoader);
			
			if (logger.isDebugEnabled()) logger.debug("Setting class loader to " + classLoader);

			final Resource[] resources = moduleLoader.getSpringConfigResources(definition, classLoader);

			ConfigurableApplicationContext context = moduleLoader.newApplicationContext(parent, definition, classLoader);
			ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
			beanFactory.addBeanPostProcessor(new ServiceRegistryPostProcessor(serviceRegistry));
			beanFactory.addBeanPostProcessor(new ModuleDefinitionPostProcessor(definition));
			
			BeanDefinitionReader reader = moduleLoader.newBeanDefinitionReader(context, definition);

			if (reader instanceof AbstractBeanDefinitionReader) {
				((AbstractBeanDefinitionReader) reader).setBeanClassLoader(classLoader);
			}
			reader.loadBeanDefinitions(resources);

			// refresh the application context - now we're ready to go
			context.refresh();
			return context;
		}
		finally {
			Thread.currentThread().setContextClassLoader(existing);
		}
	}

	public void setModuleLoaderRegistry(ModuleLoaderRegistry moduleLoaderRegistry) {
		this.moduleLoaderRegistry = moduleLoaderRegistry;
	}

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	public void setModuleChangeMonitor(ModuleChangeMonitor moduleChangeMonitor) {
		this.moduleChangeMonitor = moduleChangeMonitor;
	}

	public ModuleLoaderRegistry getRegistry() {
		return moduleLoaderRegistry;
	}

}
