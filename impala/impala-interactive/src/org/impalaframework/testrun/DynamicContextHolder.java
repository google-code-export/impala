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
import org.impalaframework.module.definition.ConstructedModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.loader.ApplicationContextLoader;
import org.impalaframework.module.manager.ModuleStateHolder;
import org.impalaframework.module.operation.AddModuleOperation;
import org.impalaframework.module.operation.IncrementalUpdateRootModuleOperation;
import org.impalaframework.module.operation.UpdateRootModuleOperation;
import org.impalaframework.module.operation.ReloadNamedModuleOperation;
import org.impalaframework.module.operation.ReloadNewNamedModuleOperation;
import org.impalaframework.module.operation.ReloadRootModuleOperation;
import org.impalaframework.module.operation.RemoveModuleOperation;
import org.impalaframework.module.operation.CloseRootModuleOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DynamicContextHolder {
	
	static final Logger logger = LoggerFactory.getLogger(DynamicContextHolder.class);

	private static ModuleStateHolder moduleStateHolder = null;

	private static ModuleManagementSource factory;

	/*
	 * **************************** initialising operations
	 * **************************
	 */

	public static void init(boolean reloadableParent) {
		if (moduleStateHolder == null) {

			String[] locations = null;

			if (reloadableParent) {
				locations = new String[] { "META-INF/impala-bootstrap.xml" };
			}
			else {
				locations = new String[] { "META-INF/impala-bootstrap.xml",
						"META-INF/impala-interactive-bootstrap.xml" };
			}

			factory = new BeanFactoryModuleManagementSource(new ClassPathXmlApplicationContext(locations));
			moduleStateHolder = factory.getPluginStateManager();
		}
	}

	public static void init(ModuleDefinitionSource pluginSpecProvider) {
		init(false);
		
		ReloadRootModuleOperation operation = new IncrementalUpdateRootModuleOperation(factory, pluginSpecProvider);
		operation.execute();
	}

	/*
	 * **************************** modifying operations
	 * **************************
	 */
	
	public static boolean reload(String plugin) {
		ReloadNamedModuleOperation operation = new ReloadNamedModuleOperation(factory, plugin);
		return operation.execute();
	}

	public static boolean reload(ModuleDefinitionSource pluginSpecProvider, String plugin) {
		ReloadNewNamedModuleOperation operation = new ReloadNewNamedModuleOperation(factory, plugin, pluginSpecProvider);
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
		new CloseRootModuleOperation(factory).execute();
		new UpdateRootModuleOperation(factory, new ConstructedModuleDefinitionSource(rootModuleDefinition)).execute();
	}

	public static void unloadParent() {
		new CloseRootModuleOperation(factory).execute();
	}

	public static boolean remove(String plugin) {
		return new RemoveModuleOperation(factory, plugin).execute();
	}

	public static void addPlugin(final ModuleDefinition moduleDefinition) {
		new AddModuleOperation(factory, moduleDefinition).execute();
	}

	/* **************************** getters ************************** */

	public static boolean hasPlugin(String plugin) {
		RootModuleDefinition spec = getPluginStateManager().getParentSpec();
		return (spec.findPlugin(plugin, true) != null);
	}	
	
	public static String findLike(ModuleDefinitionSource pluginSpecProvider, String plugin) {
		RootModuleDefinition newSpec = pluginSpecProvider.getModuleDefinition();
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

	public static ModuleStateHolder getPluginStateManager() {
		init(false);
		return moduleStateHolder;
	}

	/* **************************** private methods ************************** */

	private static ConfigurableApplicationContext internalGet() {
		ModuleStateHolder pluginStateManager2 = getPluginStateManager();
		return pluginStateManager2.getParentContext();
	}

}
