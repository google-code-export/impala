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

package org.impalaframework.web.module.type;

import java.util.Map;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.type.ApplicationModuleTypeReader;
import org.impalaframework.web.module.WebRootModuleDefinition;

public class WebRootModuleTypeReader extends ApplicationModuleTypeReader {

	@Override
	protected ModuleDefinition newDefinition(ModuleDefinition parent,
			String moduleName, 
			String type, 
			String[] locationsArray, String[] dependencyNames, Map<String, String> attributes, String runtime) {
		return new WebRootModuleDefinition(parent, moduleName, locationsArray, dependencyNames, attributes, null);
	}

}
