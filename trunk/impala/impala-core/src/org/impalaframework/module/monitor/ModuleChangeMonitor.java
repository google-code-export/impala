package org.impalaframework.module.monitor;

import org.springframework.core.io.Resource;

/**
 * Represents contract for detecting changes in resources used in an Impala plugin
 * @author Phil Zoio
 */
public interface ModuleChangeMonitor {
	public void start();
	public void setResourcesToMonitor(String pluginName, Resource[] resources);
	public void addModificationListener(ModuleChangeListener listener);
	public void stop();
}
