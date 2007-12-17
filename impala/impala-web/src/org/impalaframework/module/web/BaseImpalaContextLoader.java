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

package org.impalaframework.module.web;

import java.util.Arrays;

import javax.servlet.ServletContext;

import org.impalaframework.module.bootstrap.BeanFactoryModuleManagementSource;
import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.operation.LoadParentOperation;
import org.impalaframework.module.operation.ShutParentOperation;
import org.impalaframework.module.spec.PluginSpecProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

public abstract class BaseImpalaContextLoader extends ContextLoader implements ServletPluginSpecProvider {

	final Logger logger = LoggerFactory.getLogger(BaseImpalaContextLoader.class);

	@Override
	protected WebApplicationContext createWebApplicationContext(ServletContext servletContext, ApplicationContext parent)
			throws BeansException {

		BeanFactoryModuleManagementSource factory = createBootStrapFactory(servletContext);

		// load the parent context, which is web-independent
		PluginSpecProvider pluginSpecBuilder = getPluginSpecBuilder(servletContext);

		LoadParentOperation operation = new LoadParentOperation(factory, pluginSpecBuilder);
		operation.execute();

		// add items to servlet context
		servletContext.setAttribute(WebConstants.PLUGIN_SPEC_BUILDER_ATTRIBUTE, pluginSpecBuilder);
		servletContext.setAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE, factory);

		ConfigurableApplicationContext context = factory.getPluginStateManager().getParentContext();

		// FIXME test
		if (context == null) {
			throw new IllegalStateException("Application context " + context + " is null");
		}
		
		if (!(context instanceof WebApplicationContext)) {
			throw new IllegalStateException("Application context " + context + " has class "
					+ context.getClass().getName() + " which is not an instance of "
					+ WebApplicationContext.class.getName());
		}

		return (WebApplicationContext) context;
	}

	protected BeanFactoryModuleManagementSource createBootStrapFactory(ServletContext servletContext) {
		String[] locations = getBootstrapContextLocations(servletContext);
		logger.info("Loading bootstrap context from locations {}", Arrays.toString(locations));

		final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		final GenericWebApplicationContext applicationContext = new GenericWebApplicationContext(beanFactory);
		applicationContext.setServletContext(servletContext);

		XmlBeanDefinitionReader definitionReader = new XmlBeanDefinitionReader(beanFactory);
		for (int i = 0; i < locations.length; i++) {
			definitionReader.loadBeanDefinitions(new ClassPathResource(locations[i]));
		}
		applicationContext.refresh();

		BeanFactoryModuleManagementSource factory = new BeanFactoryModuleManagementSource(applicationContext);
		return factory;
	}

	@Override
	public void closeWebApplicationContext(ServletContext servletContext) {

		// the superclass closes the plugins
		ModuleManagementSource factory = (ModuleManagementSource) servletContext
				.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE);

		if (factory != null) {

			servletContext.log("Closing plugins and root application context hierarchy");

			boolean success = new ShutParentOperation(factory).execute();

			if (!success) {
				// this is the fallback in case the parentSpec is null
				super.closeWebApplicationContext(servletContext);
			}

			// now close the bootstrap factory
			factory.close();
		}
	}

	protected String[] getBootstrapContextLocations(ServletContext servletContext) {
		return new DefaultBootstrapLocationResolutionStrategy().getBootstrapContextLocations(servletContext);
	}

	public abstract PluginSpecProvider getPluginSpecBuilder(ServletContext servletContext);

}
