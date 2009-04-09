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

package org.impalaframework.module.transition;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.ModuleState;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionUtils;
import org.impalaframework.module.spi.ModuleRuntimeManager;
import org.impalaframework.module.spi.TransitionProcessor;

public class LoadTransitionProcessor implements TransitionProcessor {

	private static final Log logger = LogFactory.getLog(LoadTransitionProcessor.class);

	private ModuleRuntimeManager moduleRuntimeManager;

	public LoadTransitionProcessor() {
		super();
	}

	public boolean process(RootModuleDefinition rootDefinition, ModuleDefinition currentDefinition) {

		final String definitionName = currentDefinition.getName();
		logger.info("Loading definition " + definitionName);
		
		if (ModuleState.DEPENDENCY_FAILED.equals(currentDefinition.getState()))
		{
			logger.info("Not loading moduel '" + definitionName + "' as one or more of its dependencies fails to load.");
			return false;
		}

		boolean success = moduleRuntimeManager.initModule(currentDefinition);
		
		if (!success) {
			//FIXME test
			final Collection<ModuleDefinition> dependents = ModuleDefinitionUtils.getDependentModules(rootDefinition, currentDefinition.getName());
			for (ModuleDefinition moduleDefinition : dependents) {
				moduleDefinition.setState(ModuleState.DEPENDENCY_FAILED);
			}
		}
		return success;
	}

	public void setModuleRuntimeManager(ModuleRuntimeManager moduleRuntimeManager) {
		this.moduleRuntimeManager = moduleRuntimeManager;
	}

}
