package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementFactory;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.ModuleStateHolder;
import org.impalaframework.module.modification.ModificationExtractorType;
import org.impalaframework.module.modification.ModificationExtractor;
import org.impalaframework.module.modification.TransitionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class RemoveModuleOperation implements ModuleOperation {

	final Logger logger = LoggerFactory.getLogger(RemoveModuleOperation.class);

	private final ModuleManagementFactory factory;

	public RemoveModuleOperation(final ModuleManagementFactory factory) {
		super();
		Assert.notNull(factory);
		this.factory = factory;
	}

	public ModuleOperationResult execute(ModuleOperationInput moduleOperationInput) {
		
		Assert.notNull(moduleOperationInput, "moduleOperationInput cannot be null");
		
		//FIXME comment and test
		String moduleToRemove = moduleOperationInput.getModuleName();
		//FIXME verify that moduleName is not null
		
		ModuleStateHolder moduleStateHolder = factory.getModuleStateHolder();
		ModificationExtractor calculator = factory.getModificationExtractorRegistry().getModificationExtractor(ModificationExtractorType.STRICT);
		boolean result = removeModule(moduleStateHolder, calculator, moduleToRemove);
		return result ? ModuleOperationResult.TRUE : ModuleOperationResult.FALSE;
	}
	
	public static boolean removeModule(ModuleStateHolder moduleStateHolder, ModificationExtractor calculator,
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
					throw new IllegalStateException("Module to remove does not have a parent module. "
							+ "This is unexpected state and may indicate a bug");
				}
			}
		}
		return false;
	}
	
}
