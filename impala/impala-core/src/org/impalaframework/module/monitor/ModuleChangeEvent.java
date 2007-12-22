package org.impalaframework.module.monitor;

import java.util.Collections;
import java.util.List;

/**
 * Used to indicate that a module has been modifiedModules
 * @author Phil Zoio
 */
public class ModuleChangeEvent {

	private List<ModuleChangeInfo> modifiedModules;

	public ModuleChangeEvent(List<ModuleChangeInfo> modified) {
		super();
		this.modifiedModules = modified;
	}

	public List<ModuleChangeInfo> getModifiedModules() {
		return Collections.unmodifiableList(modifiedModules);
	}

}
