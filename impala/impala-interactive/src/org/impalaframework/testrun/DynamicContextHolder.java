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

package org.impalaframework.testrun;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.module.bootstrap.BeanFactoryModuleManagementSource;
import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.loader.ApplicationContextLoader;
import org.impalaframework.module.operation.AddPluginOperation;
import org.impalaframework.module.operation.IncrementalReloadParentOperation;
import org.impalaframework.module.operation.LoadParentOperation;
import org.impalaframework.module.operation.ReloadNamedPluginOperation;
import org.impalaframework.module.operation.ReloadNewNamedPluginOperation;
import org.impalaframework.module.operation.ReloadParentOperation;
import org.impalaframework.module.operation.RemovePluginOperation;
import org.impalaframework.module.operation.ShutParentOperation;
import org.impalaframework.module.spec.ConstructedModuleDefinitionSource;
import org.impalaframework.module.spec.RootModuleDefinition;
import org.impalaframework.module.spec.ModuleDefinition;
import org.impalaframework.module.spec.ModuleDefinitionSource;
import org.impalaframework.module.transition.PluginStateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DynamicContextHolder {
	
	static final Logger logger = LoggerFactory.getLogger(DynamicContextHolder.class);

	private static PluginStateManager pluginStateManager = null;

	private static ModuleManagementSource factory;

	/*
	 * **************************** initialising operations
	 * **************************
	 */

	public static void init(boolean reloadableParent) {
		if (pluginStateManager == null) {

			String[] locations = null;

			if (reloadableParent) {
				locations = new String[] { "META-INF/impala-bootstrap.xml" };
			}
			else {
				locations = new String[] { "META-INF/impala-bootstrap.xml",
						"META-INF/impala-interactive-bootstrap.xml" };
			}

			factory = new BeanFactoryModuleManagementSource(new ClassPathXmlApplicationContext(locations));
			pluginStateManager = factory.getPluginStateManager();
		}
	}

	public static void init(ModuleDefinitionSource pluginSpecProvider) {
		init(false);
		
		ReloadParentOperation operation = new IncrementalReloadParentOperation(factory, pluginSpecProvider);
		operation.execute();
	}

	/*
	 * **************************** modifying operations
	 * **************************
	 */
	
	public static boolean reload(String plugin) {
		ReloadNamedPluginOperation operation = new ReloadNamedPluginOperation(factory, plugin);
		return operation.execute();
	}

	public static boolean reload(ModuleDefinitionSource pluginSpecProvider, String plugin) {
		ReloadNewNamedPluginOperation operation = new ReloadNewNamedPluginOperation(factory, plugin, pluginSpecProvider);
		return operation.execute();
	}

	public static String reloadLike(ModuleDefinitionSource pluginSpecProvider, String plugin) {
		String like = findLike(pluginSpecProvider, plugin);
		if (like != null) {
			reload(pluginSpecProvider, plugin);
		}
		return like;
	}
	
	public static String reloadLike(String plugin) {
		String like = findLike(getPluginStateManager(), plugin);
		if (like != null) {
			reload(getPluginStateManager(), plugin);
		}
		return like;
	}

	public static void reloadParent() {
		RootModuleDefinition rootModuleDefinition = getPluginStateManager().getParentSpec();
		new ShutParentOperation(factory).execute();
		new LoadParentOperation(factory, new ConstructedModuleDefinitionSource(rootModuleDefinition)).execute();
	}

	public static void unloadParent() {
		new ShutParentOperation(factory).execute();
	}

	public static boolean remove(String plugin) {
		return new RemovePluginOperation(factory, plugin).execute();
	}

	public static void addPlugin(final ModuleDefinition moduleDefinition) {
		new AddPluginOperation(factory, moduleDefinition).execute();
	}

	/* **************************** getters ************************** */

	public static boolean hasPlugin(String plugin) {
		RootModuleDefinition spec = getPluginStateManager().getParentSpec();
		return (spec.findPlugin(plugin, true) != null);
	}	
	
	public static String findLike(ModuleDefinitionSource pluginSpecProvider, String plugin) {
		RootModuleDefinition newSpec = pluginSpecProvider.getPluginSpec();
		ModuleDefinition actualPlugin = newSpec.findPlugin(plugin, false);
		if (actualPlugin != null) {
			return actualPlugin.getName();
		}
		return null;
	}
	
	public static ApplicationContext get() {
		ConfigurableApplicationContext context = internalGet();
		if (context == null) {
			throw new NoServiceException("No root application has been loaded");
		}
		return context;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Object> T getBean(String beanName, Class<T> t) {
		ApplicationContext context = get();
		return (T) context.getBean(beanName);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Object> T getPluginBean(String pluginName, String beanName, Class<T> t) {
		ApplicationContext context = getPluginStateManager().getPlugin(pluginName);
		if (context == null) {
			throw new NoServiceException("No application context could be found for plugin " + pluginName);
		}
		return (T) context.getBean(beanName);
	}

	public static ApplicationContextLoader getContextLoader() {
		return factory.getApplicationContextLoader();
	}

	public static PluginStateManager getPluginStateManager() {
		init(false);
		return pluginStateManager;
	}

	/* **************************** private methods ************************** */

	private static ConfigurableApplicationContext internalGet() {
		PluginStateManager pluginStateManager2 = getPluginStateManager();
		return pluginStateManager2.getParentContext();
	}

}
