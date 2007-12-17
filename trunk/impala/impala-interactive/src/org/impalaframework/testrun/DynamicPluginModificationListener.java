package org.impalaframework.testrun;

import java.util.Set;

import org.impalaframework.module.monitor.BasePluginModificationListener;
import org.impalaframework.module.monitor.PluginModificationEvent;
import org.impalaframework.module.monitor.PluginModificationListener;


public class DynamicPluginModificationListener extends BasePluginModificationListener implements PluginModificationListener {

	public void pluginModified(PluginModificationEvent event) {
		Set<String> modified = getModifiedPlugins(event);
		
		for (String pluginName : modified) {
			DynamicContextHolder.reload(pluginName);
		}
	}
}
