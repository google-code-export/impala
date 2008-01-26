package org.impalaframework.module.monitor;

import org.springframework.core.io.Resource;

/**
 * Represents contract for detecting changes in resources used in an Impala module
 * @author Phil Zoio
 */
public interface ModuleChangeMonitor {
	public void start();
	public void setResourcesToMonitor(String moduleName, Resource[] resources);
	public void addModificationListener(ModuleContentChangeListener listener);
	public void stop();
}
