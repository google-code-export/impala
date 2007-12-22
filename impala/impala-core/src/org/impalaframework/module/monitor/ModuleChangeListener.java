package org.impalaframework.module.monitor;

/**
 * @author Phil Zoio
 */
public interface ModuleChangeListener {
	void moduleContentsModified(ModuleChangeEvent event);
}
