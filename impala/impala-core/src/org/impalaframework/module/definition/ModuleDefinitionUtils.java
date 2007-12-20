package org.impalaframework.module.definition;

import java.util.Collection;

public class ModuleDefinitionUtils {
	
	public static ModuleDefinition findPlugin(String pluginName, final ModuleDefinition moduleDefinition, boolean exactMatch) {

		if (exactMatch) {
			if (pluginName.equals(moduleDefinition.getName()))
				return moduleDefinition;
		}
		else {
			if (moduleDefinition.getName().contains(pluginName))
				return moduleDefinition;
		}

		final Collection<ModuleDefinition> childPlugins = moduleDefinition.getModules();
		for (ModuleDefinition childSpec : childPlugins) {
			final ModuleDefinition findPlugin = findPlugin(pluginName, childSpec, exactMatch);
			if (findPlugin != null) {
				return findPlugin;
			}
		}
		return null;
	}
}
