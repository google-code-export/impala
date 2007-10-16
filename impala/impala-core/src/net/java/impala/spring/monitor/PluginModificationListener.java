package net.java.impala.spring.monitor;

/**
 * @author Phil Zoio
 */
public interface PluginModificationListener {
	void pluginModified(PluginModificationEvent event);
}
