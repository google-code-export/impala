package net.java.impala.testrun;

import java.util.Set;

import net.java.impala.spring.monitor.PluginModificationEvent;
import net.java.impala.spring.monitor.PluginModificationListener;
import net.java.impala.spring.plugin.BasePluginModificationListener;

public class DynamicPluginModificationListener extends BasePluginModificationListener implements PluginModificationListener {

	public void pluginModified(PluginModificationEvent event) {
		Set<String> modified = getModifiedPlugins(event);
		
		for (String pluginName : modified) {
			DynamicContextHolder.reload(pluginName);
		}
	}
}
