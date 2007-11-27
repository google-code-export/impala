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
import org.impalaframework.plugin.loader.ApplicationPluginLoader;
import org.impalaframework.plugin.loader.BeansetApplicationPluginLoader;
import org.impalaframework.plugin.loader.PluginLoaderRegistry;
import org.impalaframework.plugin.modification.ModificationCalculationType;
import org.impalaframework.plugin.modification.PluginModificationCalculator;
import org.impalaframework.plugin.modification.PluginTransitionSet;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginTypes;
import org.impalaframework.plugin.spec.SimpleParentSpec;
import org.impalaframework.plugin.transition.PluginStateManager;
import org.impalaframework.resolver.ClassLocationResolver;
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

	public static final String PLUGIN_NAMES_PARAM = "pluginNames";

	public static final String IMPALA_FACTORY_PARAM = WebApplicationContext.class.getName() + ".CONTEXT_HOLDER";

	public static final String WEBAPP_LOCATION_PARAM = "webappConfigLocation";

	@Override
	protected WebApplicationContext createWebApplicationContext(ServletContext servletContext, ApplicationContext parent)
			throws BeansException {

		String[] locations = new String[] { 
				"org/impalaframework/plugin/bootstrap/impala-bootstrap.xml",
				"org/impalaframework/plugin/web/impala-web-bootstrap.xml",
				"org/impalaframework/plugin/web/impala-web-listener-bootstrap.xml" };

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

		// add context holder to servlet context
		// FIXME bind factory to servletContext instead!
		servletContext.setAttribute(IMPALA_FACTORY_PARAM, factory);
		WebApplicationContext parentContext = (WebApplicationContext) pluginStateManager.getParentContext();

		return parentContext;
	}

	@Deprecated
	// FIXME remove when tests
	protected PluginLoaderRegistry newRegistry(ServletContext servletContext,
			ClassLocationResolver classLocationResolver) {
		PluginLoaderRegistry registry = new PluginLoaderRegistry();
		registry.setPluginLoader(PluginTypes.ROOT, new WebParentPluginLoader(classLocationResolver, servletContext));
		registry.setPluginLoader(PluginTypes.APPLICATION, new ApplicationPluginLoader(classLocationResolver));
		registry.setPluginLoader(PluginTypes.APPLICATION_WITH_BEANSETS, new BeansetApplicationPluginLoader(
				classLocationResolver));
		registry.setPluginLoader(WebPluginTypes.SERVLET, new WebPluginLoader(classLocationResolver, servletContext));
		return registry;
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

		String pluginNameString = servletContext.getInitParameter(PLUGIN_NAMES_PARAM);
		ParentSpec parentSpec = new SimpleParentSpec(locations);
		return new SingleStringPluginSpecBuilder(parentSpec, pluginNameString).getParentSpec();
	}

}
