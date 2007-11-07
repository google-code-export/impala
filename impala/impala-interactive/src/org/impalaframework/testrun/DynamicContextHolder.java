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

import java.util.Collection;


import org.impalaframework.plugin.loader.ApplicationContextLoader;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.plugin.spec.PluginSpecProvider;
import org.impalaframework.resolver.ClassLocationResolver;
import org.impalaframework.resolver.StandaloneClassLocationResolverFactory;
import org.impalaframework.spring.DefaultSpringContextHolder;
import org.impalaframework.spring.SpringContextHolder;
import org.springframework.context.ApplicationContext;

public class DynamicContextHolder {

	private static SpringContextHolder holder = null;

	public static void init() {
		if (holder == null) {
			ClassLocationResolver classLocationResolver = new StandaloneClassLocationResolverFactory().getClassLocationResolver();
			ApplicationContextLoader contextLoader = new ContextLoaderFactory().newContextLoader(classLocationResolver, false, false);
			setContextLoader(contextLoader);
		}
	}
	
	public static void setContextLoader(ApplicationContextLoader applicationContextLoader) {
		if (holder == null)
			holder = new DefaultSpringContextHolder(applicationContextLoader);
	}

	public static void setPluginContextHolder(SpringContextHolder pluginContextHolder) {
		holder = pluginContextHolder;
	}

	public static ApplicationContextLoader getContextLoader() {
		if (holder != null) {
			return holder.getContextLoader();
		}
		return null;
	}

	public static void init(Object pluginSpecAware) {
		init();
		ParentSpec contextSpec = getPluginSpec(pluginSpecAware);
		try {
			if (!holder.hasParentContext()) {
				if (contextSpec != null) {
					holder.loadParentContext(contextSpec);
				}
			}
			else {
				if (contextSpec != null) {
					ParentSpec newParent = contextSpec;
					ParentSpec existingParent = holder.getParent();

					if (!newParent.containsAll(existingParent)) {
						System.out.println("Changes to parent context. Reloading.");
						holder.shutParentConext();
						holder.loadParentContext(contextSpec);
					}
					else {
						//FIXME set new parent context
						System.out.println("Using existing context. Reloading.");
						existingParent.addContextLocations(newParent);
						
						Collection<PluginSpec> plugins = contextSpec.getPlugins();
						for (PluginSpec plugin : plugins) {
							maybeAddPlugin(plugin);
						}
					}
				}
			}
		}
		finally {
			if (contextSpec != null)
				holder.setSpringContextSpec(contextSpec);
		}
	}

	private static void maybeAddPlugin(PluginSpec plugin) {
		
		//FIXME make sure that this does not attempt to load the same plugin more than once
		//without any changes having occurred in the spec.
		final String pluginName = plugin.getName();

		final PluginSpec loadedPluginSpec = holder.getPlugin(pluginName);

		if (loadedPluginSpec == null) {
			System.out.println("Plugin " + pluginName + " not present. Loading this.");
			// we don't have plugin, so load it
			holder.addPlugin(plugin);
		}
		else {
			if (!loadedPluginSpec.equals(plugin)) {
				System.out.println("Spec for plugin " + pluginName + " has changed. Re-loading this.");
				holder.removePlugin(loadedPluginSpec);
				holder.addPlugin(plugin);
			}
		}

		// recursively call children
		final Collection<PluginSpec> plugins = plugin.getPlugins();
		for (PluginSpec spec : plugins) {
			maybeAddPlugin(spec);
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

	public static boolean reload(String plugin) {
		final PluginSpec loadedPlugin = holder.getPlugin(plugin);
		if (loadedPlugin == null) return false;
		
		removePlugin(loadedPlugin, false);
		addPlugin(loadedPlugin);
		return true;
	}
	
	public static String reloadLike(String plugin) {
		final PluginSpec loadedPlugin = holder.findPluginLike(plugin);
		if (loadedPlugin == null) return null;
		
		removePlugin(loadedPlugin, false);
		addPlugin(loadedPlugin);
		
		return loadedPlugin.getName();
	}

	public static boolean remove(String plugin) {
		final PluginSpec loadedPlugin = holder.getPlugin(plugin);
		if (loadedPlugin == null) return false;
		
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
			holder.removePlugin(loadedPlugin);
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
	}

	public static ApplicationContext get() {
		return holder.getContext();
	}

	@SuppressWarnings("unchecked")
	public static <T extends Object> T getBean(Object test, String string, Class<T> t) {
		init(test);
		ApplicationContext context = get();
		return (T) context.getBean(string);
	}

	public static boolean reloadParent() {
		holder.shutParentConext();
		return holder.loadParentContext();
	}

	public static boolean reloadParent(ParentSpec pluginSpec) {
		holder.shutParentConext();
		return holder.loadParentContext(pluginSpec);
	}

	static SpringContextHolder getHolder() {
		return holder;
	}

}
