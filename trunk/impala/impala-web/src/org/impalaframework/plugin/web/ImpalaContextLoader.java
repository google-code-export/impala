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

package org.impalaframework.plugin.web;

import javax.servlet.ServletContext;

import org.impalaframework.plugin.bootstrap.BootstrapBeanFactory;
import org.impalaframework.plugin.builder.SingleStringPluginSpecBuilder;
import org.impalaframework.plugin.modification.ModificationCalculationType;
import org.impalaframework.plugin.modification.PluginModificationCalculator;
import org.impalaframework.plugin.modification.PluginTransitionSet;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.SimpleParentSpec;
import org.impalaframework.plugin.transition.PluginStateManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

public class ImpalaContextLoader extends ContextLoader {

	@Override
	protected WebApplicationContext createWebApplicationContext(ServletContext servletContext, ApplicationContext parent)
			throws BeansException {

		String[] locations = getBootstrapContextLocations();

		final DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		final GenericWebApplicationContext applicationContext = new GenericWebApplicationContext(beanFactory);
		applicationContext.setServletContext(servletContext);

		XmlBeanDefinitionReader definitionReader = new XmlBeanDefinitionReader(beanFactory);
		for (int i = 0; i < locations.length; i++) {
			definitionReader.loadBeanDefinitions(new ClassPathResource(locations[i]));
		}
		applicationContext.refresh();

		BootstrapBeanFactory factory = new BootstrapBeanFactory(applicationContext);

		PluginStateManager pluginStateManager = factory.getPluginStateManager();
		
		// load the parent context, which is web-independent
		ParentSpec pluginSpec = getPluginSpec(servletContext);

		// figure out the plugins to reload
		PluginModificationCalculator calculator = factory.getPluginModificationCalculatorRegistry().getPluginModificationCalculator(ModificationCalculationType.STRICT);
		PluginTransitionSet transitions = calculator.getTransitions(null, pluginSpec);
		pluginStateManager.processTransitions(transitions);

		// add factory to servlet context
		servletContext.setAttribute(WebConstants.IMPALA_FACTORY_PARAM, factory);
		WebApplicationContext parentContext = (WebApplicationContext) pluginStateManager.getParentContext();

		return parentContext;
	}

	protected String[] getBootstrapContextLocations() {
		//FIXME test
		String[] locations = new String[] { 
				"org/impalaframework/plugin/bootstrap/impala-bootstrap.xml",
				"org/impalaframework/plugin/web/impala-web-bootstrap.xml",
				"org/impalaframework/plugin/web/impala-web-listener-bootstrap.xml" };
		return locations;
	}

	// FIXME find better way of handling this
	ParentSpec getPluginSpec(ServletContext servletContext) {

		// subclasses can override to get PluginSpec more intelligently
		String[] locations = null;
		String configLocationString = servletContext.getInitParameter(CONFIG_LOCATION_PARAM);
		if (configLocationString != null) {
			locations = (StringUtils.tokenizeToStringArray(configLocationString,
					ConfigurableWebApplicationContext.CONFIG_LOCATION_DELIMITERS));
		}

		String pluginNameString = servletContext.getInitParameter(WebConstants.PLUGIN_NAMES_PARAM);
		ParentSpec parentSpec = new SimpleParentSpec(locations);
		return new SingleStringPluginSpecBuilder(parentSpec, pluginNameString).getParentSpec();
	}

}
