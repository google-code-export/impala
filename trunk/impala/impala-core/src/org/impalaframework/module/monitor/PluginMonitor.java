package org.impalaframework.module.monitor;

import org.springframework.core.io.Resource;

/**
 * Represents contract for detecting changes in resources used in an Impala plugin
 * @author Phil Zoio
 */
public interface PluginMonitor {
	public void start();
	public void setResourcesToMonitor(String pluginName, Resource[] resources);
	public void addModificationListener(PluginModificationListener listener);
	public void stop();
}
