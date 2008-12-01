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

public class RemoveModuleOperation  extends BaseModuleOperation {

	protected RemoveModuleOperation() {
		super();
	}

	public ModuleOperationResult doExecute(ModuleOperationInput moduleOperationInput) {
		
		Assert.notNull(moduleOperationInput, "moduleOperationInput cannot be null");
		String moduleToRemove = moduleOperationInput.getModuleName();
		Assert.notNull(moduleToRemove, "moduleName is required as it specifies the name of the module to remove in " + this.getClass().getName());
		
		ModuleStateHolder moduleStateHolder = getModuleStateHolder();
		ModificationExtractor calculator = getModificationExtractorRegistry().getModificationExtractor(ModificationExtractorType.STRICT);
		boolean result = removeModule(moduleStateHolder, calculator, moduleToRemove);
		return result ? ModuleOperationResult.TRUE : ModuleOperationResult.FALSE;
	}
	
	protected boolean removeModule(ModuleStateHolder moduleStateHolder, ModificationExtractor calculator,
			String moduleToRemove) {
		
		RootModuleDefinition oldRootDefinition = moduleStateHolder.getRootModuleDefinition();
		
		if (oldRootDefinition == null) {
			return false;
		}
		
		RootModuleDefinition newRootDefinition = moduleStateHolder.cloneRootModuleDefinition();
		ModuleDefinition definitionToRemove = newRootDefinition.findChildDefinition(moduleToRemove, true);

		if (definitionToRemove != null) {
			if (definitionToRemove instanceof RootModuleDefinition) {
				//we're removing the rootModuleDefinition
				TransitionSet transitions = calculator.getTransitions(oldRootDefinition, null);
				moduleStateHolder.processTransitions(transitions);
				return true;
			}
			else {
				ModuleDefinition parent = definitionToRemove.getParentDefinition();
				if (parent != null) {
					parent.remove(moduleToRemove);
					
					definitionToRemove.setParentDefinition(null);

					TransitionSet transitions = calculator.getTransitions(oldRootDefinition, newRootDefinition);
					moduleStateHolder.processTransitions(transitions);
					return true;
				}
				else {
					throw new InvalidStateException("Module to remove does not have a parent module. "
							+ "This is unexpected state and may indicate a bug");
				}
			}
		}
		return false;
	}
	
}
