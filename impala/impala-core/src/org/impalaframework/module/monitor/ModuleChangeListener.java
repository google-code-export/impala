package org.impalaframework.module.monitor;

/**
 * @author Phil Zoio
 */
public interface ModuleChangeListener {
	void pluginModified(ModuleChangeEvent event);
}
