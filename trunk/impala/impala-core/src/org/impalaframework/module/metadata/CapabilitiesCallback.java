/*
 * Copyright 2007-2012 the original author or authors.
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

package org.impalaframework.module.metadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionCallback;

/**
 * Implementation of {@link ModuleDefinitionCallback} used to collect capabilities on system
 * @author Phil Zoio
 */
public class CapabilitiesCallback implements ModuleDefinitionCallback {
	
	private final LinkedHashSet<String> capabilities;

	public CapabilitiesCallback() {
		capabilities = new LinkedHashSet<String>();
	}
	
	public boolean matches(ModuleDefinition moduleDefinition) {
		Collection<String> moduleCapabilities = moduleDefinition.getCapabilities();
		if (!moduleCapabilities.isEmpty()) {
			capabilities.addAll(moduleCapabilities);
		}
		return false;
	}
	
	public Collection<String> getSortedCapabilities() {
		ArrayList<String> list = new ArrayList<String>(capabilities);
		Collections.sort(list);
		return list;
	}

}
