package net.java.impala.spring.plugin;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.ClassUtils;

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

}
