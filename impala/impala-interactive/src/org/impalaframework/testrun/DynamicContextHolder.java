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
import org.impalaframework.plugin.loader.ApplicationContextLoader;
import org.impalaframework.plugin.modification.PluginModificationCalculator;
import org.impalaframework.plugin.modification.PluginTransition;
import org.impalaframework.plugin.modification.PluginTransitionSet;
import org.impalaframework.plugin.modification.StickyPluginModificationCalculator;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.plugin.spec.PluginSpecProvider;
import org.impalaframework.plugin.transition.AddLocationsTransitionProcessor;
import org.impalaframework.plugin.transition.DefaultPluginStateManager;
import org.impalaframework.plugin.transition.LoadTransitionProcessor;
import org.impalaframework.plugin.transition.PluginStateManager;
import org.impalaframework.plugin.transition.PluginStateUtils;
import org.impalaframework.plugin.transition.TransitionProcessorRegistry;
import org.impalaframework.plugin.transition.UnloadTransitionProcessor;
import org.impalaframework.resolver.ClassLocationResolver;
import org.impalaframework.resolver.StandaloneClassLocationResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public class DynamicContextHolder {

	static final Logger logger = LoggerFactory.getLogger(DynamicContextHolder.class);

	private static DefaultPluginStateManager pluginStateManager = null;

	private static PluginModificationCalculator calculator = null;

	private static PluginModificationCalculator stickyCalculator = null;

	/*
	 * **************************** initialising operations
	 * **************************
	 */

	public static void init() {
		if (pluginStateManager == null) {
			ClassLocationResolver classLocationResolver = new StandaloneClassLocationResolverFactory()
					.getClassLocationResolver();
			ContextLoaderFactory contextLoaderFactory = new ContextLoaderFactory();
			
			ApplicationContextLoader contextLoader = contextLoaderFactory.newContextLoader(classLocationResolver,
					false, true);

			pluginStateManager = new DefaultPluginStateManager();

			TransitionProcessorRegistry transitionProcessors = new TransitionProcessorRegistry();
			LoadTransitionProcessor loadTransitionProcessor = new LoadTransitionProcessor(contextLoader);
			UnloadTransitionProcessor unloadTransitionProcessor = new UnloadTransitionProcessor();
			AddLocationsTransitionProcessor addLocationsTransitionProcessor = new AddLocationsTransitionProcessor(
					contextLoaderFactory.getPluginLoaderRegistry(classLocationResolver, true));

			transitionProcessors.addTransitionProcessor(PluginTransition.UNLOADED_TO_LOADED, loadTransitionProcessor);
			transitionProcessors.addTransitionProcessor(PluginTransition.LOADED_TO_UNLOADED, unloadTransitionProcessor);
			transitionProcessors.addTransitionProcessor(PluginTransition.CONTEXT_LOCATIONS_ADDED,
					addLocationsTransitionProcessor);
			pluginStateManager.setTransitionProcessorRegistry(transitionProcessors);

			pluginStateManager.setApplicationContextLoader(contextLoader);
			calculator = new PluginModificationCalculator();
			stickyCalculator = new StickyPluginModificationCalculator();
		}
	}

	public static void init(PluginSpecProvider pluginSpecProvider) {
		init();
		ParentSpec providedSpec = getPluginSpec(pluginSpecProvider);

		if (getPluginStateManager().getParentContext() == null) {
			loadParent(providedSpec);
		}
		else {
			ParentSpec oldSpec = getPluginStateManager().getParentSpec();
			loadParent(oldSpec, providedSpec);
		}
	}

	/*
	 * **************************** modifying operations
	 * **************************
	 */

	public static boolean reload(String plugin) {
		ParentSpec spec = getPluginStateManager().getParentSpec();
		return reload(spec, spec, plugin);
	}

	public static boolean reload(PluginSpecProvider pluginSpecProvider, String plugin) {
		ParentSpec oldSpec = getPluginStateManager().getParentSpec();
		ParentSpec newSpec = pluginSpecProvider.getPluginSpec();
		return reload(oldSpec, newSpec, plugin);
	}

	public static String reloadLike(PluginSpecProvider pluginSpecProvider, String plugin) {
		ParentSpec newSpec = pluginSpecProvider.getPluginSpec();
		return reloadLike(newSpec, plugin);
	}

	public static String reloadLike(String plugin) {
		ParentSpec spec = getPluginStateManager().getParentSpec();
		return reloadLike(spec, plugin);
	}

	public static void reloadParent() {
		ParentSpec spec = getPluginStateManager().getParentSpec();
		unloadParent();
		loadParent(spec);
	}

	public static void unloadParent() {
		ParentSpec spec = getPluginStateManager().getParentSpec();
		PluginTransitionSet transitions = calculator.getTransitions(spec, null);
		getPluginStateManager().processTransitions(transitions);
	}

	private static void loadParent(ParentSpec spec) {
		PluginTransitionSet transitions = stickyCalculator.getTransitions(null, spec);
		getPluginStateManager().processTransitions(transitions);
	}

	private static void loadParent(ParentSpec old, ParentSpec spec) {
		PluginTransitionSet transitions = stickyCalculator.getTransitions(old, spec);
		getPluginStateManager().processTransitions(transitions);
	}

	private static boolean reload(ParentSpec oldSpec, ParentSpec newSpec, String plugin) {
		PluginTransitionSet transitions = calculator.reload(oldSpec, newSpec, plugin);
		getPluginStateManager().processTransitions(transitions);
		return !transitions.getPluginTransitions().isEmpty();
	}

	private static String reloadLike(ParentSpec newSpec, String plugin) {
		ParentSpec oldSpec = getPluginStateManager().getParentSpec();

		PluginSpec actualPlugin = newSpec.findPlugin(plugin, false);
		if (actualPlugin != null) {
			reload(oldSpec, newSpec, actualPlugin.getName());
			return actualPlugin.getName();
		}
		return null;
	}

	public static boolean remove(String plugin) {
		return PluginStateUtils.removePlugin(getPluginStateManager(), calculator, plugin);
	}

	public static void addPlugin(final PluginSpec pluginSpec) {
		PluginStateUtils.addPlugin(getPluginStateManager(), calculator, pluginSpec);
	}

	/* **************************** getters ************************** */

	public static ApplicationContext get() {
		ConfigurableApplicationContext context = internalGet();
		if (context == null) {
			throw new NoServiceException("No root application has been loaded");
		}
		return context;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Object> T getBean(PluginSpecProvider test, String beanName, Class<T> t) {
		ApplicationContext context = get();
		return (T) context.getBean(beanName);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Object> T getPluginBean(PluginSpecProvider test, String pluginName, String beanName,
			Class<T> t) {
		ApplicationContext context = getPluginStateManager().getPlugin(pluginName);
		if (context == null) {
			throw new NoServiceException("No application context could be found for plugin " + pluginName);
		}
		return (T) context.getBean(beanName);
	}

	public static ApplicationContextLoader getContextLoader() {
		return getPluginStateManager().getContextLoader();
	}

	public static PluginStateManager getPluginStateManager() {
		init();
		return pluginStateManager;
	}

	/* **************************** private methods ************************** */

	private static ParentSpec getPluginSpec(PluginSpecProvider provider) {
		ParentSpec pluginSpec = provider.getPluginSpec();
		if (pluginSpec == null) {
			throw new NullPointerException(provider.getClass().getName() + " cannot return a null PluginSpec");
		}
		return pluginSpec;
	}

	private static ConfigurableApplicationContext internalGet() {
		PluginStateManager pluginStateManager2 = getPluginStateManager();
		return pluginStateManager2.getParentContext();
	}	

}
