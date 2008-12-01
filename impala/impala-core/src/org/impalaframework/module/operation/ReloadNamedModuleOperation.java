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

package org.impalaframework.module.operation;

import org.impalaframework.module.ModificationExtractor;
import org.impalaframework.module.ModificationExtractorType;
import org.impalaframework.module.ModuleStateHolder;
import org.impalaframework.module.TransitionSet;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleState;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.modification.ModificationExtractorRegistry;
import org.springframework.util.Assert;

public class ReloadNamedModuleOperation  extends BaseModuleOperation {

	protected ReloadNamedModuleOperation() {
		super();
	}

	public ModuleOperationResult doExecute(ModuleOperationInput moduleOperationInput) {

		Assert.notNull(moduleOperationInput, "moduleOperationInput cannot be null");
		String moduleToReload = moduleOperationInput.getModuleName();
		Assert.notNull(moduleToReload, "moduleName is required as it specifies the name of the module to reload in "
				+ this.getClass().getName());

		ModuleStateHolder moduleStateHolder = getModuleStateHolder();
		RootModuleDefinition oldRootDefinition = moduleStateHolder.getRootModuleDefinition();
		RootModuleDefinition newRootDefinition = moduleStateHolder.cloneRootModuleDefinition();

		ModificationExtractorRegistry modificationExtractor = getModificationExtractorRegistry();
		ModificationExtractor calculator = modificationExtractor
				.getModificationExtractor(ModificationExtractorType.STRICT);

		ModuleDefinition childDefinition = newRootDefinition.findChildDefinition(moduleToReload, true);

		if (childDefinition != null) {
			childDefinition.setState(ModuleState.STALE);

			TransitionSet transitions = calculator.getTransitions(oldRootDefinition, newRootDefinition);
			moduleStateHolder.processTransitions(transitions);

			boolean result = !transitions.getModuleTransitions().isEmpty();
			return result ? ModuleOperationResult.TRUE : ModuleOperationResult.FALSE;
		}
		
		return ModuleOperationResult.FALSE;
	}
}
