package org.impalaframework.testrun;

import java.util.Set;

import org.impalaframework.plugin.monitor.BasePluginModificationListener;
import org.impalaframework.plugin.monitor.PluginModificationEvent;
import org.impalaframework.plugin.monitor.PluginModificationListener;


public class DynamicPluginModificationListener extends BasePluginModificationListener implements PluginModificationListener {

	public void pluginModified(PluginModificationEvent event) {
		Set<String> modified = getModifiedPlugins(event);
		
		for (String pluginName : modified) {
			DynamicContextHolder.reload(pluginName);
		}
	}
}
