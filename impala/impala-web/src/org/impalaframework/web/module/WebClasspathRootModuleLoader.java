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

import java.util.List;

import javax.servlet.ServletContext;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.loader.ModuleUtils;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.springframework.core.io.Resource;

public class WebClasspathRootModuleLoader extends BaseWebModuleLoader {

	public WebClasspathRootModuleLoader(ModuleLocationResolver moduleLocationResolver) {
		super(moduleLocationResolver);
	}
	
	public WebClasspathRootModuleLoader(ModuleLocationResolver moduleLocationResolver, ServletContext servletContext) {
		super(moduleLocationResolver, servletContext);
	}
	
	@Override
	public Resource[] getClassLocations(ModuleDefinition moduleDefinition) {
		List<String> projectNames = getModuleDefinitions(moduleDefinition);
		return ModuleUtils.getRootClassLocations(getClassLocationResolver(), projectNames);
	}

	private List<String> getModuleDefinitions(ModuleDefinition moduleDefinition) {
		if (!(moduleDefinition instanceof RootModuleDefinition)) {
			throw new ConfigurationException("FIXME");//FIXME test and deduplicate
		}

		RootModuleDefinition rootModuleDefinition = (RootModuleDefinition) moduleDefinition;
		List<String> projectNameList = rootModuleDefinition.getRootProjectNames();
		return projectNameList;
	}

}
