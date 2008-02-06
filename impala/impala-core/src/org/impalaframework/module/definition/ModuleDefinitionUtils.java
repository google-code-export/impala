package org.impalaframework.module.definition;

import java.util.Collection;
import java.util.List;

public class ModuleDefinitionUtils {

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	public static void addAttributes(int spaces, StringBuffer buffer, ModuleDefinition moduleDefinition) {
		String name = moduleDefinition.getName();
		List<String> contextLocations = moduleDefinition.getContextLocations();
		String type = moduleDefinition.getType();

		buffer.append("name=" + name);
		buffer.append(", contextLocations=" + contextLocations);
		buffer.append(", type=" + type);

		addChildDefinitions(moduleDefinition, spaces, buffer);
	}

	private static void addChildDefinitions(ModuleDefinition moduleDefinition, int spaces, StringBuffer buffer) {
		Collection<ModuleDefinition> childDefinitions = moduleDefinition.getChildDefinitions();

		if (!childDefinitions.isEmpty()) {

			spaces += 2;
			
			for (ModuleDefinition childDefinition : childDefinitions) {
				
				buffer.append(LINE_SEPARATOR);
				
				for (int i = 0; i < spaces; i++) {
					buffer.append(" ");
				}
				if (childDefinition instanceof PrettyPrintable) {
					PrettyPrintable m = (PrettyPrintable) childDefinition;
					m.toString(buffer, spaces);
				}
				else {
					buffer.append(childDefinition.toString());
				}
			}
		}
	}

	public static ModuleDefinition findDefinition(String moduleName, final ModuleDefinition moduleDefinition,
			boolean exactMatch) {

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
			final ModuleDefinition found = findDefinition(moduleName, childDefinition, exactMatch);
			if (found != null) {
				return found;
			}
		}
		return null;
	}
}
