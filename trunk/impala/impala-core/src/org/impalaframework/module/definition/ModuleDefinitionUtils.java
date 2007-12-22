package org.impalaframework.module.definition;

import java.util.Collection;

public class ModuleDefinitionUtils {
	
	public static ModuleDefinition findDefinition(String moduleName, final ModuleDefinition moduleDefinition, boolean exactMatch) {

		if (exactMatch) {
			if (moduleName.equals(moduleDefinition.getName()))
				return moduleDefinition;
		}
		else {
			if (moduleDefinition.getName().contains(moduleName))
				return moduleDefinition;
		}

		final Collection<ModuleDefinition> childDefinitions = moduleDefinition.getChildDefinitions();
		for (ModuleDefinition childDefinition : childDefinitions) {
			final ModuleDefinition findPlugin = findDefinition(moduleName, childDefinition, exactMatch);
			if (findPlugin != null) {
				return findPlugin;
			}
		}
		return null;
	}
}
