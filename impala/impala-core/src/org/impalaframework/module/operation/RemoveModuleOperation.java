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

	private final String moduleToRemove;

	public RemoveModuleOperation(final ModuleManagementFactory factory, final String moduleToRemove) {
		super();
		Assert.notNull(factory);
		Assert.notNull(moduleToRemove);
		this.factory = factory;
		this.moduleToRemove = moduleToRemove;
	}

	public ModuleOperationResult execute(ModuleOperationInput moduleOperationInput) {
		
		//FIXME comment and test
		
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
