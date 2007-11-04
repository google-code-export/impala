package org.impalaframework.spring.monitor;

/**
 * @author Phil Zoio
 */
public interface PluginModificationListener {
	void pluginModified(PluginModificationEvent event);
}
