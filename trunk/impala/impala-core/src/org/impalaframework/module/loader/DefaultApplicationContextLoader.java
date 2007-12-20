/*
 * Copyright 2007 the original author or authors.
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

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.monitor.ModuleChangeMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	final Logger logger = LoggerFactory.getLogger(DefaultApplicationContextLoader.class);

	private ModuleLoaderRegistry registry;

	private ModuleChangeMonitor moduleChangeMonitor;

	public DefaultApplicationContextLoader(ModuleLoaderRegistry registry) {
		Assert.notNull(registry, ModuleLoaderRegistry.class.getName() + " cannot be null");
		this.registry = registry;
	}

	public ConfigurableApplicationContext loadContext(ModuleDefinition definition, ApplicationContext parent) {

		ConfigurableApplicationContext context = null;
		
		final ModuleLoader moduleLoader = registry.getModuleLoader(definition.getType(), false);
		final DelegatingContextLoader delegatingLoader = registry.getDelegatingLoader(definition.getType());

		try {

			if (moduleLoader != null) {
				context = loadApplicationContext(moduleLoader, parent, definition);
				moduleLoader.afterRefresh(context, definition);
			}
			else if (delegatingLoader != null) {
				context = delegatingLoader.loadApplicationContext(parent, definition);
			}
			else {
				throw new IllegalStateException("No " + ModuleLoader.class.getName() + " or "
						+ DelegatingContextLoader.class.getName() + " specified for plugin type " + definition.getType());
			}

		}
		finally {
			if (moduleLoader != null) {
				Resource[] toMonitor = moduleLoader.getClassLocations(definition);
				if (moduleChangeMonitor != null) {
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

			final Resource[] resources = moduleLoader.getSpringConfigResources(definition, classLoader);

			ConfigurableApplicationContext context = moduleLoader.newApplicationContext(parent, definition, classLoader);

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

	public void setModuleChangeMonitor(ModuleChangeMonitor moduleChangeMonitor) {
		this.moduleChangeMonitor = moduleChangeMonitor;
	}

	public ModuleChangeMonitor getModuleChangeMonitor() {
		return this.moduleChangeMonitor;
	}

	public ModuleLoaderRegistry getRegistry() {
		return registry;
	}

}
