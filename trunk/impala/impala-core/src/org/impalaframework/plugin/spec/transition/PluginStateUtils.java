package org.impalaframework.plugin.spec.transition;

import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.plugin.spec.modification.PluginModificationCalculator;
import org.impalaframework.plugin.spec.modification.PluginTransitionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PluginStateUtils {

	static final Logger logger = LoggerFactory.getLogger(PluginStateUtils.class);

	public static void addPlugin(PluginStateManager pluginStateManager, PluginModificationCalculator calculator,
			PluginSpec pluginSpec) {

		ParentSpec oldSpec = pluginStateManager.getParentSpec();
		ParentSpec newSpec = pluginStateManager.cloneParentSpec();

		PluginSpec parent = pluginSpec.getParent();
		if (pluginSpec instanceof ParentSpec) {
			// FIXME test
			newSpec = (ParentSpec) pluginSpec;
		}
		else {

			PluginSpec newParent = null;

			if (parent == null) {
				newParent = newSpec;
			}
			else {
				String parentName = parent.getName();
				newParent = newSpec.findPlugin(parentName, true);

				if (newParent == null) {
					// FIXME throw illegalstate and test
					throw new IllegalStateException("could not find name in new spec ... this shouldn't happen");
				}
			}

			newParent.add(pluginSpec);
			pluginSpec.setParent(newParent);
		}

		PluginTransitionSet transitions = calculator.getTransitions(oldSpec, newSpec);
		pluginStateManager.processTransitions(transitions);
	}

	public static boolean removePlugin(PluginStateManager pluginStateManager, PluginModificationCalculator calculator,
			String plugin) {

		ParentSpec oldSpec = pluginStateManager.getParentSpec();
		ParentSpec newSpec = pluginStateManager.cloneParentSpec();
		PluginSpec pluginToRemove = newSpec.findPlugin(plugin, true);

		if (pluginToRemove != null) {
			if (pluginToRemove instanceof ParentSpec) {
				// FIXME test
				logger.warn("Plugin " + plugin + " is a parent plugin. Cannot remove this");
			}
			else {
				PluginSpec parent = pluginToRemove.getParent();
				if (parent != null) {
					parent.remove(plugin);
					pluginToRemove.setParent(null);

					PluginTransitionSet transitions = calculator.getTransitions(oldSpec, newSpec);
					pluginStateManager.processTransitions(transitions);
					return true;
				}
				else {
					// FIXME test
					logger.warn("Plugin to remove does not have a parent plugin. "
							+ "This is unexpected state and may indicate a bug");
				}
			}
		}
		return false;
	}

}
