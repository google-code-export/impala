package org.impalaframework.spring.plugin;

import org.impalaframework.module.spec.ModuleDefinition;

public interface PluginSpecAware {
	public void setPluginSpec(ModuleDefinition moduleDefinition);
}
