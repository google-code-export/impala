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

public class AddModuleOperation implements ModuleOperation {

	final Logger logger = LoggerFactory.getLogger(AddModuleOperation.class);

	private final ModuleManagementFactory factory;

	private final ModuleDefinition modulesToAdd;

	public AddModuleOperation(final ModuleManagementFactory factory, final ModuleDefinition modulesToAdd) {
		super();
		Assert.notNull(factory);
		Assert.notNull(modulesToAdd);
		this.factory = factory;
		this.modulesToAdd = modulesToAdd;
	}

	public ModuleOperationResult execute() {
		
		//FIXME comment and test
		
		ModuleStateHolder moduleStateHolder = factory.getModuleStateHolder();
		ModificationExtractor calculator = factory.getModificationExtractorRegistry().getModificationExtractor(ModificationExtractorType.STICKY);
		addPlugin(moduleStateHolder, calculator, modulesToAdd);
		return new ModuleOperationResult(true);
	}
	
	public static void addPlugin(ModuleStateHolder moduleStateHolder, ModificationExtractor calculator,
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
					throw new IllegalStateException("Unable to find parent module '" + parentName + "' in " + newRootDefinition);
				}
			}

			newParent.add(moduleDefinition);
			moduleDefinition.setParentDefinition(newParent);
		}

		TransitionSet transitions = calculator.getTransitions(oldRootDefinition, newRootDefinition);
		moduleStateHolder.processTransitions(transitions);
	}	
	
}
