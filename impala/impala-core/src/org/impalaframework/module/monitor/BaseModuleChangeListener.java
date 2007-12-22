package org.impalaframework.module.monitor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author Phil Zoio
 */
public class BaseModuleChangeListener {

	protected Set<String> getModifiedPlugins(ModuleChangeEvent event) {
		Set<String> modified = new HashSet<String>();
		final List<ModuleChangeInfo> modifiedPlugins = event.getModifiedModules();
		for (ModuleChangeInfo info : modifiedPlugins) {
			modified.add(info.getModuleName());
		}
		return modified;
	}

}
