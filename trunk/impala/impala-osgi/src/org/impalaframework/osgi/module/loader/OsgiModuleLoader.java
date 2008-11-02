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
import org.impalaframework.osgi.spring.ImpalaOSGiApplicationContext;
import org.impalaframework.osgi.util.OSGIUtils;
import org.impalaframework.util.ObjectUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.osgi.context.BundleContextAware;
import org.springframework.osgi.context.DelegatedExecutionOsgiBundleApplicationContext;
import org.springframework.osgi.io.OsgiBundleResource;

//FIXME test
public class OsgiModuleLoader implements ModuleLoader, BundleContextAware {

	private BundleContext bundleContext;
	
	private ClassLoaderFactory classLoaderFactory;

	public Resource[] getClassLocations(ModuleDefinition moduleDefinition) {
		//FIXME use this to figure out how to install bundle
		return null;
	}

	public Resource[] getSpringConfigResources(ModuleDefinition moduleDefinition, ClassLoader classLoader) {
		Bundle bundle = OSGIUtils.findBundle(bundleContext, moduleDefinition.getName());
		
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

	public ConfigurableApplicationContext newApplicationContext(
			ApplicationContext parent, ModuleDefinition moduleDefinition,
			ClassLoader classLoader) {

		Bundle bundle = OSGIUtils.findBundle(bundleContext, moduleDefinition.getName());
		final BundleContext bc = bundle.getBundleContext();
		
		//FIXME test
		final ImpalaOSGiApplicationContext applicationContext = new ImpalaOSGiApplicationContext(parent);
		applicationContext.setBundleContext(bc);
		
		final Resource[] springConfigResources = getSpringConfigResources(moduleDefinition, classLoader);
		final ClassLoader newClassLoader = newClassLoader(moduleDefinition, parent);
		
		applicationContext.setClassLoader(newClassLoader);
		applicationContext.setConfigResources(springConfigResources);

		DelegatedExecutionOsgiBundleApplicationContext dc = ObjectUtils.cast(applicationContext, DelegatedExecutionOsgiBundleApplicationContext.class);
		dc.startRefresh();
		
		return applicationContext;
	}

	public ClassLoader newClassLoader(ModuleDefinition moduleDefinition,
			ApplicationContext parent) {
		Bundle bundle = OSGIUtils.findBundle(bundleContext, moduleDefinition.getName());
		
		//FIXME handle exception if this returns null
		//note that this will not work if bundle has not been loaded
		
		return classLoaderFactory.newClassLoader(null, bundle);
	}	

	public BeanDefinitionReader newBeanDefinitionReader(
			ConfigurableApplicationContext context,
			ModuleDefinition moduleDefinition) {
		//don't implement this as this is handled internally within ImpalaOSGiApplicationContext
		return null;
	}

	public void handleRefresh(ConfigurableApplicationContext context) {
		DelegatedExecutionOsgiBundleApplicationContext dc = ObjectUtils.cast(context, DelegatedExecutionOsgiBundleApplicationContext.class);
		dc.completeRefresh();
	}
	
	public void afterRefresh(ConfigurableApplicationContext context,
			ModuleDefinition definition) {
	}

	public void setClassLoaderFactory(ClassLoaderFactory classLoaderFactory) {
		this.classLoaderFactory = classLoaderFactory;
	}

	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}
}
