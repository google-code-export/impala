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

package net.java.impala.spring.util;

import java.util.Collection;

import net.java.impala.spring.plugin.ApplicationContextSet;
import net.java.impala.spring.plugin.PluginLoader;
import net.java.impala.spring.plugin.PluginLoaderRegistry;
import net.java.impala.spring.plugin.PluginSpec;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

public class RegistryBasedApplicationContextLoader implements ApplicationContextLoader {

	private PluginLoaderRegistry registry;

	public RegistryBasedApplicationContextLoader(PluginLoaderRegistry registry) {
		Assert.notNull(registry, PluginLoaderRegistry.class.getName() + " cannot be null");
		this.registry = registry;
	}

	public void loadParentContext(ApplicationContextSet appSet, PluginSpec parentSpec, ClassLoader classLoader) {

		ConfigurableApplicationContext context = null;
		ClassLoader existingClassLoader = ClassUtils.getDefaultClassLoader();

		if (classLoader == null) {
			// if not supplied, use existing
			classLoader = existingClassLoader;
		}

		try {

			Thread.currentThread().setContextClassLoader(classLoader);

			final PluginLoader pluginLoader = registry.getPluginLoader(parentSpec.getType());
			final Resource[] resources = pluginLoader.getSpringConfigResources(appSet, parentSpec, classLoader);
			
			context = this.loadContextFromResources(null, resources, classLoader);
			appSet.getPluginContext().put(parentSpec.getName(), context);

			Collection<PluginSpec> plugins = parentSpec.getPlugins();
			for (PluginSpec plugin : plugins) {
				addApplicationPlugin(appSet, plugin, context);
			}
		}
		finally {
			Thread.currentThread().setContextClassLoader(existingClassLoader);
		}
	}

	public void addApplicationPlugin(ApplicationContextSet appSet, PluginSpec plugin, ApplicationContext parent) {

		final PluginLoader pluginLoader = registry.getPluginLoader(plugin.getType());
		
		// create the class loader
		ClassLoader classLoader = pluginLoader.newClassLoader(appSet, plugin);

		ClassLoader existing = ClassUtils.getDefaultClassLoader();

		try {
			
			Thread.currentThread().setContextClassLoader(classLoader);

			final Resource[] resources = pluginLoader.getSpringConfigResources(appSet, plugin, classLoader);

			ConfigurableApplicationContext context = this.loadContextFromResources(parent,
					resources, classLoader);

			appSet.getPluginContext().put(plugin.getName(), context);

			// now recursively add context
			final Collection<PluginSpec> plugins = plugin.getPlugins();
			for (PluginSpec childPlugin : plugins) {
				addApplicationPlugin(appSet, childPlugin, context);
			}
			
		}
		finally {
			Thread.currentThread().setContextClassLoader(existing);
		}

	}

	public ConfigurableApplicationContext loadContextFromResources(ApplicationContext parent, Resource[] resource,
			ClassLoader classLoader) {

		DefaultListableBeanFactory beanFactory = newBeanFactory();
		beanFactory.setBeanClassLoader(classLoader);

		// create the application context, and set the class loader
		GenericApplicationContext context = newApplicationContext(parent, beanFactory);
		context.setClassLoader(classLoader);

		XmlBeanDefinitionReader xmlReader = newBeanDefinitionReader(context);
		xmlReader.loadBeanDefinitions(resource);
		xmlReader.setBeanClassLoader(classLoader);

		// refresh the application context - now we're ready to go
		refresh(context);
		return context;
	}

	protected GenericApplicationContext newApplicationContext(ApplicationContext parent,
			DefaultListableBeanFactory beanFactory) {
		GenericApplicationContext context = parent != null ? new GenericApplicationContext(beanFactory, parent)
				: new GenericApplicationContext(beanFactory);
		return context;
	}

	protected void beforeRefresh() {
	}

	protected void afterRefresh() {
	}

	protected XmlBeanDefinitionReader newBeanDefinitionReader(GenericApplicationContext context) {
		return new XmlBeanDefinitionReader(context);
	}

	protected DefaultListableBeanFactory newBeanFactory() {
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		return beanFactory;
	}

	private void refresh(GenericApplicationContext context) {
		beforeRefresh();
		context.refresh();
		afterRefresh();
	}
}
