package net.java.impala.spring.plugin;

import org.springframework.util.Assert;

public class ChildSpecContainerImpl implements ChildSpecContainer {
	private PluginSpec[] plugins;

	public ChildSpecContainerImpl(PluginSpec[] plugins) {
		super();
		Assert.notNull(plugins);
		this.plugins = plugins;
	}

	public String[] getPluginNames() {
		String[] pluginNames = new String[plugins.length];
		for (int i = 0; i < pluginNames.length; i++) {
			pluginNames[i] = plugins[i].getName();
		}
		return pluginNames;
	}

	public PluginSpec getPlugin(String pluginName) {
		for (PluginSpec plugin : plugins) {
			if (plugin.getName().equals(pluginName)) {
				return plugin;
			}
		}
		return null;
	}

	public boolean hasPlugin(String pluginName) {
		return getPlugin(pluginName) != null;
	}

	public PluginSpec[] getPlugins() {
		return plugins;
	}
}
