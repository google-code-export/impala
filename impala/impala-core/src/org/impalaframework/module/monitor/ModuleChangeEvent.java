package org.impalaframework.module.monitor;

import java.util.Collections;
import java.util.List;

/**
 * Used to indicate that a plugin has been modifiedPlugins
 * @author Phil Zoio
 */
public class ModuleChangeEvent {

	private List<ModuleChangeInfo> modifiedPlugins;

	public ModuleChangeEvent(List<ModuleChangeInfo> modified) {
		super();
		this.modifiedPlugins = modified;
	}

	public List<ModuleChangeInfo> getModifiedPlugins() {
		return Collections.unmodifiableList(modifiedPlugins);
	}

}
