package org.impalaframework.plugin.spec;

import java.util.Collection;

public class PluginSpecUtils {
	
	public static PluginSpec findPlugin(String pluginName, final PluginSpec pluginSpec, boolean exactMatch) {

		if (exactMatch) {
			if (pluginName.equals(pluginSpec.getName()))
				return pluginSpec;
		}
		else {
			if (pluginSpec.getName().contains(pluginName))
				return pluginSpec;
		}

		final Collection<PluginSpec> childPlugins = pluginSpec.getPlugins();
		for (PluginSpec childSpec : childPlugins) {
			final PluginSpec findPlugin = findPlugin(pluginName, childSpec, exactMatch);
			if (findPlugin != null) {
				return findPlugin;
			}
		}
		return null;
	}
}
