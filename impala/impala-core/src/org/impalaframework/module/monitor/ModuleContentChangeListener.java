package org.impalaframework.module.monitor;

/**
 * @author Phil Zoio
 */
public interface ModuleContentChangeListener {
	void moduleContentsModified(ModuleChangeEvent event);
}
