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

import java.util.ArrayList;
import java.util.Collection;

import org.impalaframework.classloader.ClassLoaderFactory;
import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.ModuleLoader;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.resource.ModuleLocationsResourceLoader;
import org.impalaframework.spring.resource.ClassPathResourceLoader;
import org.impalaframework.spring.resource.CompositeResourceLoader;
import org.impalaframework.spring.resource.ResourceLoader;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public abstract class BaseModuleLoader implements ModuleLoader {

	private ClassLoaderFactory classLoaderFactory;
	
	public GenericApplicationContext newApplicationContext(ApplicationContext parent, ModuleDefinition definition, ClassLoader classLoader) {
		Assert.notNull(classLoader, "classloader cannot be null");
		
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		beanFactory.setBeanClassLoader(classLoader);

		// create the application context, and set the class loader
		GenericApplicationContext context = new GenericApplicationContext(beanFactory, parent);
		context.setClassLoader(classLoader);
		return context;
	}

	public final Resource[] getSpringConfigResources(ModuleDefinition moduleDefinition, ClassLoader classLoader) {
		//FIXME does not need to be part of this interface
		ModuleLocationsResourceLoader loader = new ModuleLocationsResourceLoader();
		Collection<ResourceLoader> resourceLoaders = getSpringLocationResourceLoaders();
		ResourceLoader compositeResourceLoader = new CompositeResourceLoader(resourceLoaders);
		loader.setResourceLoader(compositeResourceLoader);
		return loader.getSpringLocations(moduleDefinition, classLoader);
	}

	protected Collection<ResourceLoader> getSpringLocationResourceLoaders() {
		//FIXME issue 25: wire this in
		Collection<ResourceLoader> resourceLoaders = new ArrayList<ResourceLoader>();
		resourceLoaders.add(new ClassPathResourceLoader());
		return resourceLoaders;
	}
	
	protected ClassLoaderFactory getClassLoaderFactory() {
		if (classLoaderFactory == null) {
			throw new ConfigurationException("No " + ClassLoaderFactory.class.getName() + " set. Check your definition for " + this.getClass().getName());
		}
		return classLoaderFactory;
	}

	public XmlBeanDefinitionReader newBeanDefinitionReader(ConfigurableApplicationContext context, ModuleDefinition definition) {
		final ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
		return new XmlBeanDefinitionReader(ModuleUtils.castToBeanDefinitionRegistry(beanFactory));
	}

	public void afterRefresh(ConfigurableApplicationContext context, ModuleDefinition definition) {
	}
	
	public void setClassLoaderFactory(ClassLoaderFactory classLoaderFactory) {
		this.classLoaderFactory = classLoaderFactory;
	}

}
