package net.java.impala.spring.plugin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.java.impala.spring.monitor.PluginModificationEvent;
import net.java.impala.spring.monitor.PluginModificationInfo;

public class BasePluginModificationListener {


	protected Set<String> getModifiedPlugins(PluginModificationEvent event) {
		Set<String> modified = new HashSet<String>();
		final List<PluginModificationInfo> modifiedPlugins = event.getModifiedPlugins();
		for (PluginModificationInfo info : modifiedPlugins) {
			modified.add(info.getPluginName());
		}
		return modified;
	}

}
