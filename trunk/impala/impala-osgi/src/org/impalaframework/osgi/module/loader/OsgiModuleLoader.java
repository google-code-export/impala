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

package org.impalaframework.osgi.module.loader;

import java.util.List;

import org.impalaframework.classloader.ClassLoaderFactory;
import org.impalaframework.module.ModuleLoader;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.osgi.spring.ImpalaOsgiApplicationContext;
import org.impalaframework.osgi.util.OsgiUtils;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.service.ServiceRegistry;
import org.impalaframework.service.registry.ServiceRegistryPostProcessor;
import org.impalaframework.spring.module.ModuleDefinitionPostProcessor;
import org.impalaframework.util.ObjectUtils;
import org.impalaframework.util.ResourceUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.osgi.context.BundleContextAware;
import org.springframework.osgi.context.DelegatedExecutionOsgiBundleApplicationContext;
import org.springframework.osgi.io.OsgiBundleResource;

//FIXME test
/**
 * {@link ModuleLoader} whose purpose is to represent an Impala module loaded within OSGi. 
 * Accesses {@link BundleContext} by implementing {@link BundleContextAware}
 */
public class OsgiModuleLoader implements ModuleLoader, BundleContextAware {

	private BundleContext bundleContext;
	
	private ModuleLocationResolver moduleLocationResolver;

	private ClassLoaderFactory classLoaderFactory;
	
	private ServiceRegistry serviceRegistry;

	/* ************************* ModuleLoader implementation ************************ */	
	
	/**
	 * Returns the class resource locations as determined by the wired in {@link ModuleLocationResolver#getApplicationModuleClassLocations(String)}.
	 */
	public Resource[] getClassLocations(ModuleDefinition moduleDefinition) {
		List<Resource> locations = moduleLocationResolver.getApplicationModuleClassLocations(moduleDefinition.getName());
		return ResourceUtils.toArray(locations);
	}

	/**
	 * Finds the bundle whose name is the same as the module name. Then returns the context locations as an array of 
	 * {@link OsgiBundleResource} instances.
	 */
	public Resource[] getSpringConfigResources(ModuleDefinition moduleDefinition, ClassLoader classLoader) {
		Bundle bundle = OsgiUtils.findBundle(bundleContext, moduleDefinition.getName());
		
		//FIXME handle exception if this returns null
		//note that this will not work if bundle has not been loaded
		
		//TODO could make better use of OsgiBundleResource
		
		final List<String> contextLocations = moduleDefinition.getContextLocations();
		Resource[] resources = new Resource[contextLocations.size()];
		for (int i = 0; i < resources.length; i++) {
			resources[i] = new OsgiBundleResource(bundle, contextLocations.get(i));
		}
		
		return resources;
	}

	/**
	 * Returns an instance of {@link ImpalaOsgiApplicationContext}. Also
	 * registers {@link ServiceRegistryPostProcessor} and
	 * {@link ModuleDefinitionPostProcessor} {@link BeanPostProcessor}
	 * instances. Wires in resources obtained suing
	 * {@link #getSpringConfigResources(ModuleDefinition, ClassLoader)}, and
	 * calls the application context
	 * {@link DelegatedExecutionOsgiBundleApplicationContext#startRefresh()}
	 * method. Note that the rest of the refresh operation is called in 
	 * {@link #handleRefresh(ConfigurableApplicationContext)}.
	 */
	public ConfigurableApplicationContext newApplicationContext(
			ApplicationContext parent, final ModuleDefinition moduleDefinition,
			ClassLoader classLoader) {

		Bundle bundle = OsgiUtils.findBundle(bundleContext, moduleDefinition.getName());
		final BundleContext bc = bundle.getBundleContext();
		
		//FIXME test
		final ImpalaOsgiApplicationContext applicationContext = new ImpalaOsgiApplicationContext(parent) {

			@Override
			protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
				//need to add these here because don't get the chance after startRefresh() has been called
				beanFactory.addBeanPostProcessor(new ServiceRegistryPostProcessor(serviceRegistry));
				beanFactory.addBeanPostProcessor(new ModuleDefinitionPostProcessor(moduleDefinition));
				super.registerBeanPostProcessors(beanFactory);
			}
			
		};
		applicationContext.setBundleContext(bc);
		
		final Resource[] springConfigResources = getSpringConfigResources(moduleDefinition, classLoader);
		final ClassLoader newClassLoader = newClassLoader(moduleDefinition, parent);
		
		applicationContext.setClassLoader(newClassLoader);
		applicationContext.setConfigResources(springConfigResources);

		DelegatedExecutionOsgiBundleApplicationContext dc = ObjectUtils.cast(applicationContext, DelegatedExecutionOsgiBundleApplicationContext.class);
		dc.startRefresh();
		
		return applicationContext;
	}

	/**
	 * Finds the bundle whose name matches the module name. The uses the wired in
	 * {@link #classLoaderFactory} to return the bundle-specific class loader instance.
	 */
	public ClassLoader newClassLoader(ModuleDefinition moduleDefinition,
			ApplicationContext parent) {
		Bundle bundle = OsgiUtils.findBundle(bundleContext, moduleDefinition.getName());
		
		//FIXME handle exception if this returns null
		//note that this will not work if bundle has not been loaded
		
		return classLoaderFactory.newClassLoader(null, bundle);
	}	

	public BeanDefinitionReader newBeanDefinitionReader(
			ConfigurableApplicationContext context,
			ModuleDefinition moduleDefinition) {
		//don't implement this as this is handled internally within ImpalaOsgiApplicationContext
		return null;
	}

	/**
	 * Completes the second part of the refresh process by calling {@link DelegatedExecutionOsgiBundleApplicationContext#completeRefresh()}
	 */
	public void handleRefresh(ConfigurableApplicationContext context) {
		DelegatedExecutionOsgiBundleApplicationContext dc = ObjectUtils.cast(context, DelegatedExecutionOsgiBundleApplicationContext.class);
		dc.completeRefresh();
	}
	
	/**
	 * No operation is performed in this method implementation
	 */
	public void afterRefresh(ConfigurableApplicationContext context,
			ModuleDefinition definition) {
	}
	
	/* ************************* injection setters ************************ */

	public void setClassLoaderFactory(ClassLoaderFactory classLoaderFactory) {
		this.classLoaderFactory = classLoaderFactory;
	}

	public void setModuleLocationResolver(ModuleLocationResolver moduleLocationResolver) {
		this.moduleLocationResolver = moduleLocationResolver;
	}
	
	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	/* ************************* BundleContextAware implementation ************************ */

	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}
}
