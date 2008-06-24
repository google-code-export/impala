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

package org.impalaframework.web.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.springframework.util.Assert;

public class WebRootModuleDefinition extends SimpleModuleDefinition {

	private static final long serialVersionUID = 1L;

	private List<String> contextLocations;

	@Override
	public String getType() {
		return WebModuleTypes.WEB_ROOT;
	}

	public WebRootModuleDefinition(ModuleDefinition moduleDefinition, String name, String[] contextLocations) {
		super(moduleDefinition, name);
		Assert.notEmpty(contextLocations, "contextLocations cannot be empty");

		this.contextLocations = new ArrayList<String>();
		for (int i = 0; i < contextLocations.length; i++) {
			this.contextLocations.add(contextLocations[i]);
		}
	}
	
	public WebRootModuleDefinition(ModuleDefinition moduleDefinition, String name, List<String> contextLocations) {
		super(moduleDefinition, name);
		Assert.notEmpty(contextLocations);

		this.contextLocations = new ArrayList<String>(contextLocations);
	}

	public List<String> getContextLocations() {
		return Collections.unmodifiableList(contextLocations);
	}

}
