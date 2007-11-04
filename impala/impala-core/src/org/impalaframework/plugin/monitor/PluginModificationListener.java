package org.impalaframework.plugin.monitor;

/**
 * @author Phil Zoio
 */
public interface PluginModificationListener {
	void pluginModified(PluginModificationEvent event);
}
