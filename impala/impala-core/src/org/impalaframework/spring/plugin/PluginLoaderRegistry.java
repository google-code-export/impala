package org.impalaframework.spring.plugin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public class PluginLoaderRegistry {
	private Map<String, PluginLoader> pluginLoaders = new HashMap<String, PluginLoader>();

	public void setPluginLoader(String type, PluginLoader pluginLoader) {
		Assert.notNull(type, "type cannot be null");
		Assert.notNull(pluginLoader);
		pluginLoaders.put(type.toLowerCase(), pluginLoader);
	}

	public PluginLoader getPluginLoader(String type) {
		Assert.notNull(type, "type cannot be null");
		return pluginLoaders.get(type.toLowerCase());
	}
}
