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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.util.Assert;

public class ModuleDefinitionUtils {

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
		
		Assert.notNull(moduleDefinitions);
		Assert.notNull(name);
		
		for (ModuleDefinition moduleDefinition : moduleDefinitions) {
			if (name.equals(moduleDefinition.getName())) return moduleDefinition;
		}
		return null;
	}

	public static List<String> getModuleNamesFromCollection(Collection<ModuleDefinition> moduleDefinitions) {
		Assert.notNull(moduleDefinitions);
		List<String> names = new ArrayList<String>();
		for (ModuleDefinition moduleDefinition : moduleDefinitions) {
			names.add(moduleDefinition.getName());
		}
		return names;
	}

	public static String[] defaultContextLocations(String name) {
		return new String[] { name + "-context.xml" };
	}
}
