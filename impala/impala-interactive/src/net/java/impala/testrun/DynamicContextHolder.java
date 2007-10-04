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
import net.java.impala.spring.util.ApplicationContextLoader;

import org.springframework.context.ApplicationContext;

public class DynamicContextHolder {

	private static PluginContextHolder holder = null;

	public static void setContextLoader(ApplicationContextLoader applicationContextLoader) {
		holder = new PluginContextHolder(applicationContextLoader);
	}
	
	public static void init(Object test) {
		if (!holder.hasParentContext()) {
			PluginSpec pluginSpec = getPluginSpec(test);

			if (pluginSpec != null) {
				holder.loadParentContext(test, pluginSpec);
			}
		}
	}

	private static PluginSpec getPluginSpec(Object test) {
		PluginSpec pluginSpec = null;
		if (test instanceof PluginSpecAware) {
			PluginSpecAware p = (PluginSpecAware) test;
			pluginSpec = p.getPluginSpec();
		}
		return pluginSpec;
	}

	public static void removePlugin(String remove) {
		holder.removePlugin(remove);
	}

	public static boolean addPlugin(String add) {
		return holder.addPlugin(add);
	}
	
	public static boolean reload(String plugin) {
		holder.removePlugin(plugin);
		return holder.addPlugin(plugin);
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

	public static boolean reloadParent(ClassLoader classLoader, PluginSpec pluginSpec) {
		holder.shutParentConext();
		return holder.loadParentContext(classLoader, pluginSpec);
	}

}
