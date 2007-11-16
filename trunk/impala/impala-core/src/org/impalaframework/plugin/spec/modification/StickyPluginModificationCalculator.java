package org.impalaframework.plugin.spec.modification;

import java.util.Collection;
import java.util.List;

import org.impalaframework.plugin.spec.PluginSpec;

public class StickyPluginModificationCalculator extends PluginModificationCalculator {

	@Override
	void checkOriginal(PluginSpec originalSpec, PluginSpec newSpec, List<PluginStateChange> transitions) {
		Collection<PluginSpec> oldPlugins = originalSpec.getPlugins();
		
		for (PluginSpec oldPlugin : oldPlugins) {
			PluginSpec newPlugin = newSpec.getPlugin(oldPlugin.getName());

			if (newPlugin == null) {
				//FIXME add test
				newSpec.add(oldPlugin);
				oldPlugin.setParent(newSpec);
			}
		}
	}

}
