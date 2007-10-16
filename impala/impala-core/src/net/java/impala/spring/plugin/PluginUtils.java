package net.java.impala.spring.plugin;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.ClassUtils;

/**
 * @author Phil Zoio
 */
public class PluginUtils {

	public static ClassLoader getParentClassLoader(ApplicationContextSet contextSet, PluginSpec pluginSpec) {
		ClassLoader parentClassLoader = null;
		final PluginSpec parent = pluginSpec.getParent();
		if (parent != null) {
			final ConfigurableApplicationContext parentContext = contextSet.getPluginContext().get(parent.getName());
			if (parentContext != null) {
				parentClassLoader = parentContext.getClassLoader();
			}
		}
		if (parentClassLoader == null) {
			parentClassLoader = ClassUtils.getDefaultClassLoader();
		}
		return parentClassLoader;
	}
	
	public static ClassLoader getParentClassLoader(ApplicationContext parent) {
		ClassLoader parentClassLoader = null;
		if (parent != null) {
			parentClassLoader = parent.getClassLoader();
		}
		if (parentClassLoader == null) {
			parentClassLoader = ClassUtils.getDefaultClassLoader();
		}
		return parentClassLoader;
	}

}
