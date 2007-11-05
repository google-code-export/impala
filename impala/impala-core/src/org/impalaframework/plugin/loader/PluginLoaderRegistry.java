package org.impalaframework.plugin.loader;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public class PluginLoaderRegistry {
	private Map<String, PluginLoader> pluginLoaders = new HashMap<String, PluginLoader>();
	private Map<String, DelegatingContextLoader> delegatingLoaders = new HashMap<String, DelegatingContextLoader>();

	public void setPluginLoader(String type, PluginLoader pluginLoader) {
		Assert.notNull(type, "type cannot be null");
		Assert.notNull(pluginLoader);
		pluginLoaders.put(type.toLowerCase(), pluginLoader);
	}

	public PluginLoader getPluginLoader(String type) {
		Assert.notNull(type, "type cannot be null");
		return pluginLoaders.get(type.toLowerCase());
	}
	
	public void setDelegatingLoader(String type, DelegatingContextLoader pluginLoader) {
		Assert.notNull(type, "type cannot be null");
		Assert.notNull(pluginLoader);
		delegatingLoaders.put(type.toLowerCase(), pluginLoader);
	}

	public DelegatingContextLoader getDelegatingLoader(String type) {
		Assert.notNull(type, "type cannot be null");
		return delegatingLoaders.get(type.toLowerCase());
	}
}
