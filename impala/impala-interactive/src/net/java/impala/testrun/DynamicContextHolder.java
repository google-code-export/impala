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

import java.util.Collection;

import net.java.impala.spring.plugin.ParentSpec;
import net.java.impala.spring.plugin.PluginSpec;
import net.java.impala.spring.plugin.SpringContextSpec;
import net.java.impala.spring.util.ApplicationContextLoader;
import net.java.impala.testrun.spring.TestApplicationContextLoader;

import org.springframework.context.ApplicationContext;

public class DynamicContextHolder {

	private static PluginContextHolder holder = null;

	public static void setContextLoader(TestApplicationContextLoader applicationContextLoader) {
		if (holder == null)
			holder = new PluginContextHolder(applicationContextLoader);
	}

	public static TestApplicationContextLoader getContextLoader() {
		if (holder != null) {
			final ApplicationContextLoader contextLoader = holder.getContextLoader();

			if (contextLoader != null) {
				if (!(contextLoader instanceof TestApplicationContextLoader)) {
					throw new IllegalStateException("Context loader needs to be an instance of "
							+ TestApplicationContextLoader.class.getName() + ". Actual class: "
							+ contextLoader.getClass().getName());
				}
			}

			return (TestApplicationContextLoader) contextLoader;
		}
		return null;
	}

	public static void init(Object test) {
		SpringContextSpec contextSpec = getPluginSpec(test);
		if (!holder.hasParentContext()) {
			if (contextSpec != null) {
				holder.loadParentContext(test, contextSpec);
			}
		}
		else {
			if (contextSpec != null) {
				ParentSpec newParent = contextSpec.getParentSpec();
				ParentSpec existingParent = holder.getParent();

				if (!existingParent.containsAll(newParent)) {
					System.out.println("Changes to parent context. Reloading.");
					holder.shutParentConext();
					holder.loadParentContext(test, contextSpec);
				}
				else {
					Collection<PluginSpec> plugins = contextSpec.getPlugins();
					for (PluginSpec plugin : plugins) {

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
								holder.removePlugin(pluginName);
								holder.addPlugin(plugin);
							}
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
