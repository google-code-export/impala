package org.impalaframework.module.monitor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author Phil Zoio
 */
public class BaseModuleChangeListener {

	protected Set<String> getModifiedModules(ModuleChangeEvent event) {
		Set<String> modified = new HashSet<String>();
		final List<ModuleChangeInfo> modifiedModules = event.getModifiedModules();
		for (ModuleChangeInfo info : modifiedModules) {
			modified.add(info.getModuleName());
		}
		return modified;
	}

}
