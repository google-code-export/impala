package org.impalaframework.plugin.web;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.impalaframework.plugin.spec.PluginSpec;
import org.springframework.util.Assert;

public class WebPlaceholderPluginSpec implements PluginSpec {

	//FIXME test
	
	private static final long serialVersionUID = 1L;
	private PluginSpec pluginSpec;
	private String name;
	
	public WebPlaceholderPluginSpec(PluginSpec pluginSpec, String name) {
		Assert.notNull(pluginSpec);
		Assert.notNull(name);
		this.pluginSpec = pluginSpec;
		this.name = name;
	}

	public void add(PluginSpec pluginSpec) {
		throw new UnsupportedOperationException("Cannot add plugin to placeholder");
	}

	public PluginSpec findPlugin(String pluginName, boolean exactMatch) {
		return null;
	}

	public List<String> getContextLocations() {
		return Collections.emptyList();
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return WebPluginTypes.WEB_PLACEHOLDER;
	}

	public PluginSpec getParent() {
		return this.pluginSpec;
	}

	public PluginSpec getPlugin(String pluginName) {
		return null;
	}

	public Collection<String> getPluginNames() {
		return Collections.emptyList();
	}

	public Collection<PluginSpec> getPlugins() {
		return Collections.emptyList();
	}

	public boolean hasPlugin(String pluginName) {
		return false;
	}

	public PluginSpec remove(String pluginName) {
		return null;
	}

	public void setParent(PluginSpec parent) {
		this.pluginSpec = parent;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((name == null) ? 0 : name.hashCode());
		result = PRIME * result + ((pluginSpec == null) ? 0 : pluginSpec.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final WebPlaceholderPluginSpec other = (WebPlaceholderPluginSpec) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		if (pluginSpec == null) {
			if (other.pluginSpec != null)
				return false;
		}
		else if (!pluginSpec.equals(other.pluginSpec))
			return false;
		return true;
	}
	
	

}
