package org.impalaframework.module.loader;

import java.util.HashMap;
import java.util.Map;

import org.impalaframework.exception.NoServiceException;
import org.springframework.util.Assert;

/**
 * @author Phil Zoio
 */
public class ModuleLoaderRegistry {

	private Map<String, ModuleLoader> moduleLoaders = new HashMap<String, ModuleLoader>();

	private Map<String, DelegatingContextLoader> delegatingLoaders = new HashMap<String, DelegatingContextLoader>();

	public void setModuleLoader(String type, ModuleLoader moduleLoader) {
		Assert.notNull(type, "type cannot be null");
		Assert.notNull(moduleLoader);
		moduleLoaders.put(type.toLowerCase(), moduleLoader);
	}

	public ModuleLoader getPluginLoader(String type) {
		return getPluginLoader(type, true);
	}

	public ModuleLoader getPluginLoader(String type, boolean failIfNotFound) {
		Assert.notNull(type, "type cannot be null");
		ModuleLoader moduleLoader = moduleLoaders.get(type.toLowerCase());

		if (failIfNotFound) {
			if (moduleLoader == null) {
				throw new NoServiceException("No " + ModuleLoader.class.getName()
						+ " instance available for plugin type " + type);
			}
		}

		return moduleLoader;
	}

	public boolean hasPluginLoader(String type) {
		Assert.notNull(type, "type cannot be null");
		return (moduleLoaders.get(type.toLowerCase()) != null);
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
		return (moduleLoaders.get(type.toLowerCase()) != null);
	}

	public void setDelegatingLoaders(Map<String, DelegatingContextLoader> delegatingLoaders) {
		this.delegatingLoaders.clear();
		this.delegatingLoaders.putAll(delegatingLoaders);
	}

	public void setModuleLoaders(Map<String, ModuleLoader> moduleLoaders) {
		this.moduleLoaders.clear();
		this.moduleLoaders.putAll(moduleLoaders);
	}

}
