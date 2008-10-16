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

import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.module.ModificationExtractor;
import org.impalaframework.module.ModificationExtractorType;
import org.impalaframework.module.ModuleStateHolder;
import org.impalaframework.module.TransitionSet;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.springframework.util.Assert;

public class AddModuleOperation extends BaseModuleOperation implements ModuleOperation {

	protected AddModuleOperation() {
		super();
	}

	public ModuleOperationResult execute(ModuleOperationInput moduleOperationInput) {
		
		Assert.notNull(moduleOperationInput, "moduleOperationInput cannot be null");
		ModuleDefinition moduleToAdd = moduleOperationInput.getModuleDefinition();
		Assert.notNull(moduleToAdd, "moduleName is required as it specifies the name of the module to add in " + this.getClass().getName());
		
		ModuleStateHolder moduleStateHolder = getModuleStateHolder();
		ModificationExtractor calculator = getModificationExtractorRegistry().getModificationExtractor(ModificationExtractorType.STICKY);
		
		addModule(moduleStateHolder, calculator, moduleToAdd);
		return new ModuleOperationResult(true);
	}
	
	protected void addModule(ModuleStateHolder moduleStateHolder, ModificationExtractor calculator,
			ModuleDefinition moduleDefinition) {

		RootModuleDefinition oldRootDefinition = moduleStateHolder.getRootModuleDefinition();
		RootModuleDefinition newRootDefinition = moduleStateHolder.cloneRootModuleDefinition();

		ModuleDefinition parent = moduleDefinition.getParentDefinition();
		
		if (moduleDefinition instanceof RootModuleDefinition) {
			newRootDefinition = (RootModuleDefinition) moduleDefinition;
		}
		else {

			ModuleDefinition newParent = null;

			if (parent == null) {
				newParent = newRootDefinition;
			}
			else {
				String parentName = parent.getName();
				newParent = newRootDefinition.findChildDefinition(parentName, true);

				if (newParent == null) {
					throw new InvalidStateException("Unable to find parent module '" + parentName + "' in " + newRootDefinition);
				}
			}

			newParent.add(moduleDefinition);
			moduleDefinition.setParentDefinition(newParent);
		}

		TransitionSet transitions = calculator.getTransitions(oldRootDefinition, newRootDefinition);
		moduleStateHolder.processTransitions(transitions);
	}	
	
}
