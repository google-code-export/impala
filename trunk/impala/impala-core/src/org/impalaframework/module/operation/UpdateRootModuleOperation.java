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
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.springframework.util.Assert;

public class UpdateRootModuleOperation  extends BaseModuleOperation {

	protected UpdateRootModuleOperation() {
		super();
	}

	public ModuleOperationResult doExecute(ModuleOperationInput moduleOperationInput) {

		Assert.notNull(moduleOperationInput, "moduleOperationInput cannot be null");
		ModuleStateHolder moduleStateHolder = getModuleStateHolder();
		
		//note that the module definition source is externally supplied
		ModuleDefinitionSource newModuleDefinitionSource = moduleOperationInput.getModuleDefinitionSource();
		Assert.notNull(newModuleDefinitionSource, "moduleDefinitionSource is required as it specifies the new module definition to apply in " + this.getClass().getName());
		
		RootModuleDefinition newModuleDefinition = newModuleDefinitionSource.getModuleDefinition();
		RootModuleDefinition oldModuleDefinition = getExistingModuleDefinitionSource();
		
		ModificationExtractorType modificationExtractorType = getModificationExtractorType();
		
		// figure out the modules to reload
		ModificationExtractor calculator = getModificationExtractorRegistry()
				.getModificationExtractor(modificationExtractorType);
		TransitionSet transitions = calculator.getTransitions(oldModuleDefinition, newModuleDefinition);
		moduleStateHolder.processTransitions(transitions);
		return ModuleOperationResult.TRUE;
	}

	protected ModificationExtractorType getModificationExtractorType() {
		return ModificationExtractorType.STRICT;
	}

	protected RootModuleDefinition getExistingModuleDefinitionSource() {
		return null;
	}
}
