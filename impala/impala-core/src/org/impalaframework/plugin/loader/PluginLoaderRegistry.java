package org.impalaframework.plugin.loader;

import java.util.HashMap;
import java.util.Map;

import org.impalaframework.exception.NoServiceException;
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
		return getPluginLoader(type, true);
	}

	public PluginLoader getPluginLoader(String type, boolean failIfNotFound) {
		Assert.notNull(type, "type cannot be null");
		PluginLoader pluginLoader = pluginLoaders.get(type.toLowerCase());

		if (failIfNotFound) {
			if (pluginLoader == null) {
				throw new NoServiceException("No " + PluginLoader.class.getName()
						+ " instance available for plugin type " + type);
			}
		}

		return pluginLoader;
	}

	public boolean hasPluginLoader(String type) {
		Assert.notNull(type, "type cannot be null");
		return (pluginLoaders.get(type.toLowerCase()) != null);
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

	public boolean hasDelegatingLoader(String type) {
		Assert.notNull(type, "type cannot be null");
		return (pluginLoaders.get(type.toLowerCase()) != null);
	}

	public void setDelegatingLoaders(Map<String, DelegatingContextLoader> delegatingLoaders) {
		this.delegatingLoaders.clear();
		this.delegatingLoaders.putAll(delegatingLoaders);
	}

	public void setPluginLoaders(Map<String, PluginLoader> pluginLoaders) {
		this.pluginLoaders.clear();
		this.pluginLoaders.putAll(pluginLoaders);
	}

}
