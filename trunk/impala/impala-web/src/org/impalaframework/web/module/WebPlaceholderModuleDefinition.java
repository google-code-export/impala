package org.impalaframework.web.module;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.impalaframework.module.definition.ModuleDefinition;
import org.springframework.util.Assert;

public class WebPlaceholderModuleDefinition implements ModuleDefinition {

	private static final long serialVersionUID = 1L;

	private ModuleDefinition parent;

	private String name;

	public WebPlaceholderModuleDefinition(ModuleDefinition parent, String name) {
		Assert.notNull(parent);
		Assert.notNull(name);
		this.parent = parent;
		this.name = name;
		this.parent.add(this);
	}

	public void add(ModuleDefinition moduleDefinition) {
		throw new UnsupportedOperationException("Cannot add plugin '" + moduleDefinition.getName()
				+ "' to web placeholder plugin spec '" + this.getName() + "', as this cannot contain other plugins");
	}

	public ModuleDefinition findPlugin(String pluginName, boolean exactMatch) {
		return null;
	}

	public List<String> getContextLocations() {
		return Collections.emptyList();
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return WebModuleTypes.WEB_PLACEHOLDER;
	}

	public ModuleDefinition getParent() {
		return this.parent;
	}

	public ModuleDefinition getPlugin(String pluginName) {
		return null;
	}

	public Collection<String> getPluginNames() {
		return Collections.emptyList();
	}

	public Collection<ModuleDefinition> getPlugins() {
		return Collections.emptyList();
	}

	public boolean hasPlugin(String pluginName) {
		return false;
	}

	public ModuleDefinition remove(String pluginName) {
		return null;
	}

	public void setParent(ModuleDefinition parent) {
		this.parent = parent;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((name == null) ? 0 : name.hashCode());
		result = PRIME * result + ((parent == null) ? 0 : parent.hashCode());
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
		final WebPlaceholderModuleDefinition other = (WebPlaceholderModuleDefinition) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		}
		else if (!parent.equals(other.parent))
			return false;
		return true;
	}

}
