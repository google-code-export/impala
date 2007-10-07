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

package net.java.impala.testrun;

import net.java.impala.spring.plugin.PluginSpec;
import net.java.impala.spring.plugin.SpringContextSpec;
import net.java.impala.spring.util.ApplicationContextLoader;

import org.springframework.context.ApplicationContext;

public class DynamicContextHolder {

	private static PluginContextHolder holder = null;

	public static void setContextLoader(ApplicationContextLoader applicationContextLoader) {
		holder = new PluginContextHolder(applicationContextLoader);
	}

	public static void init(Object test) {
		SpringContextSpec pluginSpec = getPluginSpec(test);
		if (!holder.hasParentContext()) {
			if (pluginSpec != null) {
				holder.loadParentContext(test, pluginSpec);
			}
		}
		else {
			if (pluginSpec != null) {
				PluginSpec[] plugins = pluginSpec.getPlugins();
				for (PluginSpec plugin : plugins) {
					
					final String pluginName = plugin.getName();
					final PluginSpec loadedPluginSpec = holder.getPlugin(pluginName);
					if (loadedPluginSpec != null) {
						//we don't have plugin, so load it
						holder.addPlugin(plugin);
					}
					else {
						//we have the plugin, but need to check that it equals the one in the spec
						if (!loadedPluginSpec.equals(plugin)) {
							holder.removePlugin(pluginName);
							holder.addPlugin(plugin);
						}
					}
				}
			}
		}
	}

	private static SpringContextSpec getPluginSpec(Object test) {
		SpringContextSpec pluginSpec = null;
		if (test instanceof SpringContextSpecAware) {
			SpringContextSpecAware p = (SpringContextSpecAware) test;
			pluginSpec = p.getPluginSpec();
		}
		return pluginSpec;
	}

	public static boolean reload(String plugin) {
		final PluginSpec loadedPlugin = holder.getPlugin(plugin);
		if (loadedPlugin != null) {
			holder.removePlugin(plugin);
			return holder.addPlugin(loadedPlugin);
		}
		return false;
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

	public static boolean reloadParent(ClassLoader classLoader) {
		holder.shutParentConext();
		return holder.loadParentContext(classLoader);
	}

	public static boolean reloadParent(ClassLoader classLoader, SpringContextSpec pluginSpec) {
		holder.shutParentConext();
		return holder.loadParentContext(classLoader, pluginSpec);
	}

}
