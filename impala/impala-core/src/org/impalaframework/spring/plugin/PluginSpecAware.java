package org.impalaframework.spring.plugin;

import org.impalaframework.module.definition.ModuleDefinition;

public interface PluginSpecAware {
	public void setPluginSpec(ModuleDefinition moduleDefinition);
}
