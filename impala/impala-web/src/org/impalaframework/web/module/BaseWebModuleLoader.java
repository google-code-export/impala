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

package org.impalaframework.web.module;

import java.util.List;

import javax.servlet.ServletContext;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.loader.BaseModuleLoader;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.util.ResourceUtils;
import org.impalaframework.web.servlet.wrapper.ServletContextWrapper;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.GenericWebApplicationContext;

public class BaseWebModuleLoader extends BaseModuleLoader implements ServletContextAware {

	private ServletContext servletContext;
	
	private ServletContextWrapper servletContextWrapper;

	private ModuleLocationResolver moduleLocationResolver;

	public BaseWebModuleLoader(ModuleLocationResolver moduleLocationResolver) {
		Assert.notNull(moduleLocationResolver, "moduleLocationResolver cannot be null");
		this.moduleLocationResolver = moduleLocationResolver;
	}

	public BaseWebModuleLoader(ModuleLocationResolver moduleLocationResolver, ServletContext servletContext) {
		Assert.notNull(servletContext, "ServletContext cannot be null");
		Assert.notNull(moduleLocationResolver, "moduleLocationResolver cannot be null");
		this.moduleLocationResolver = moduleLocationResolver;
		this.servletContext = servletContext;
	}

	public GenericWebApplicationContext newApplicationContext(ApplicationContext parent,
			ModuleDefinition moduleDefinition, ClassLoader classLoader) {
		
		ServletContext wrappedServletContext = servletContext;
		
		if (servletContextWrapper != null) {
			wrappedServletContext = servletContextWrapper.wrapServletContext(servletContext, moduleDefinition, classLoader);
		}
		
		final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		beanFactory.setBeanClassLoader(classLoader);

		final GenericWebApplicationContext context = new GenericWebApplicationContext(beanFactory);
		context.setParent(parent);
		context.setServletContext(wrappedServletContext);
		context.setClassLoader(classLoader);

		return context;
	}

	public ClassLoader newClassLoader(ModuleDefinition moduleDefinition, ApplicationContext parent) {
		Resource[] moduleClassLocations = getClassLocations(moduleDefinition);
		ClassLoader classLoader = null;
		if (parent != null) {
			classLoader = parent.getClassLoader();
		}
		else {
			classLoader = ClassUtils.getDefaultClassLoader();
		}
		return getClassLoaderFactory().newClassLoader(classLoader, ResourceUtils.getFiles(moduleClassLocations));
	}

	public Resource[] getClassLocations(ModuleDefinition moduleDefinition) {
		List<Resource> moduleClassLocations = moduleLocationResolver.getApplicationModuleClassLocations(moduleDefinition.getName());
		return ResourceUtils.toArray(moduleClassLocations);
	}

	protected ModuleLocationResolver getClassLocationResolver() {
		return moduleLocationResolver;
	}

	protected ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public void setServletContextWrapper(ServletContextWrapper servletContextWrapper) {
		this.servletContextWrapper = servletContextWrapper;
	}
	
}
