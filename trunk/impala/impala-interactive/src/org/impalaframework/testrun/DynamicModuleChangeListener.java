package org.impalaframework.testrun;

import java.util.Set;

import org.impalaframework.facade.DynamicContextHolder;
import org.impalaframework.module.monitor.BaseModuleChangeListener;
import org.impalaframework.module.monitor.ModuleChangeEvent;
import org.impalaframework.module.monitor.ModuleContentChangeListener;


public class DynamicModuleChangeListener extends BaseModuleChangeListener implements ModuleContentChangeListener {

	public void moduleContentsModified(ModuleChangeEvent event) {
		Set<String> modified = getModifiedModules(event);
		
		for (String pluginName : modified) {
			DynamicContextHolder.reload(pluginName);
		}
	}
}
