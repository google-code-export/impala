package net.java.impala.spring.plugin;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.util.Assert;

public class ChildSpecContainerImpl implements ChildSpecContainer {
	
	private Map<String, PluginSpec> plugins = new LinkedHashMap<String, PluginSpec>();

	public ChildSpecContainerImpl(PluginSpec[] plugins) {
		super();
		Assert.notNull(plugins);
		for (PluginSpec spec : plugins) {
			final String name = spec.getName();
			this.plugins.put(name, spec);
		}
	}

	public ChildSpecContainerImpl() {
	}

	public Collection<String> getPluginNames() {
		return plugins.keySet();
	}

	public PluginSpec getPlugin(String pluginName) {
		return plugins.get(pluginName);
	}

	public boolean hasPlugin(String pluginName) {
		return getPlugin(pluginName) != null;
	}

	public Collection<PluginSpec> getPlugins() {
		return plugins.values();
	}
}
