/*
 * Copyright 2007-2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.module.definition;

import java.util.Collection;
import java.util.List;

import org.springframework.util.Assert;

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
	
	public static ModuleDefinition getModuleFromCollection(Collection<ModuleDefinition> moduleDefinitions, String name) {
		//FIXME add test
		Assert.notNull(moduleDefinitions);
		Assert.notNull(name);
		
		for (ModuleDefinition moduleDefinition : moduleDefinitions) {
			if (name.equals(moduleDefinition.getName())) return moduleDefinition;
		}
		return null;
	}

	public static String[] defaultContextLocations(String name) {
		return new String[] { name + "-context.xml" };
	}
}
