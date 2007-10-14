package net.java.impala.spring.plugin;

import java.util.HashMap;
import java.util.Map;

public class PluginLoaderRegistry {
	private Map<String, PluginLoader> pluginLoaders = new HashMap<String, PluginLoader>();

	public void setPluginLoader(String type, PluginLoader pluginLoader) {
		pluginLoaders.put(type, pluginLoader);
	}

	public PluginLoader getPluginLoader(String type) {
		return pluginLoaders.get(type);
	}
}
