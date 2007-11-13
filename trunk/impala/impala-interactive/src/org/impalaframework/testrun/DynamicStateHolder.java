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

import org.impalaframework.plugin.loader.ApplicationContextLoader;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpecProvider;
import org.impalaframework.plugin.spec.modification.PluginModificationCalculator;
import org.impalaframework.plugin.spec.modification.PluginTransitionSet;
import org.impalaframework.plugin.spec.transition.PluginStateManager;
import org.impalaframework.resolver.ClassLocationResolver;
import org.impalaframework.resolver.StandaloneClassLocationResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

public class DynamicStateHolder {

	static final Logger logger = LoggerFactory.getLogger(DynamicStateHolder.class);

	private static PluginStateManager holder = null;
	private static PluginModificationCalculator calculator = null;

	public static void init() {
		if (holder == null) {
			ClassLocationResolver classLocationResolver = new StandaloneClassLocationResolverFactory()
					.getClassLocationResolver();
			ApplicationContextLoader contextLoader = new ContextLoaderFactory().newContextLoader(classLocationResolver,
					false, false);
			setContextLoader(contextLoader);
		}
	}

	public static void setContextLoader(ApplicationContextLoader applicationContextLoader) {
		if (holder == null){
			holder = new PluginStateManager();
			holder.setApplicationContextLoader(applicationContextLoader);
			calculator = new PluginModificationCalculator();
		}
	}

	public static ApplicationContextLoader getContextLoader() {
		if (holder != null) {
			return holder.getContextLoader();
		}
		return null;
	}

	public static void init(Object pluginSpecProvider) {
		init();
		ParentSpec providedSpec = getPluginSpec(pluginSpecProvider);

		if (holder.getParentContext() == null) {
			if (providedSpec != null) {
				PluginTransitionSet transitions = calculator.getTransitions(null, providedSpec);
				holder.processTransitions(transitions);
			}
		}
		else {
			if (providedSpec != null) {
				ParentSpec oldSpec = holder.getParentSpec();
				PluginTransitionSet transitions = calculator.getTransitions(oldSpec, providedSpec);
				holder.processTransitions(transitions);
			}
		}
	}

	private static ParentSpec getPluginSpec(Object test) {
		ParentSpec pluginSpec = null;
		if (test instanceof PluginSpecProvider) {
			PluginSpecProvider p = (PluginSpecProvider) test;
			pluginSpec = p.getPluginSpec();
		}
		return pluginSpec;
	}
/*
	public static boolean reload(String plugin) {
		final PluginSpec loadedPlugin = holder.getPlugin(plugin);
		if (loadedPlugin == null)
			return false;

		removePlugin(loadedPlugin, false);
		addPlugin(loadedPlugin);
		return true;
	}

	public static String reloadLike(String plugin) {
		final PluginSpec loadedPlugin = holder.findPluginLike(plugin);
		if (loadedPlugin == null)
			return null;

		removePlugin(loadedPlugin, false);
		addPlugin(loadedPlugin);

		return loadedPlugin.getName();
	}

	public static boolean remove(String plugin) {
		final PluginSpec loadedPlugin = holder.getPlugin(plugin);
		if (loadedPlugin == null)
			return false;

		removePlugin(loadedPlugin, true);
		return true;
	}

	private static void removePlugin(final PluginSpec loadedPlugin, boolean removeFromSpec) {
		if (loadedPlugin != null) {
			final Collection<PluginSpec> plugins = loadedPlugin.getPlugins();
			for (PluginSpec spec : plugins) {
				removePlugin(spec, removeFromSpec);
			}
			if (removeFromSpec) {
				loadedPlugin.getParent().remove(loadedPlugin.getName());
			}
			holder.removePlugin(loadedPlugin, removeFromSpec);
		}
	}

	private static void addPlugin(final PluginSpec loadedPlugin) {
		if (loadedPlugin != null) {
			holder.addPlugin(loadedPlugin);

			final Collection<PluginSpec> plugins = loadedPlugin.getPlugins();
			for (PluginSpec spec : plugins) {
				addPlugin(spec);
			}
		}
	}*/

	public static ApplicationContext get() {
		return holder.getParentContext();
	}

	@SuppressWarnings("unchecked")
	public static <T extends Object> T getBean(Object test, String string, Class<T> t) {
		init(test);
		ApplicationContext context = get();
		return (T) context.getBean(string);
	}	
	
	public static void setPluginStateManager(PluginStateManager stateManager) {
		holder = stateManager;
	}
	public static void setPluginModificationCalculator(PluginModificationCalculator stateManager) {
		calculator = stateManager;
	}

}
