package org.impalaframework.testrun;

import java.util.Set;

import org.impalaframework.module.monitor.BaseModuleChangeListener;
import org.impalaframework.module.monitor.ModuleChangeEvent;
import org.impalaframework.module.monitor.ModuleChangeListener;


public class DynamicPluginModificationListener extends BaseModuleChangeListener implements ModuleChangeListener {

	public void pluginModified(ModuleChangeEvent event) {
		Set<String> modified = getModifiedPlugins(event);
		
		for (String pluginName : modified) {
			DynamicContextHolder.reload(pluginName);
		}
	}
}
