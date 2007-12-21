package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
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

	private final ModuleManagementSource factory;

	private final ModuleDefinition pluginToAdd;

	public AddModuleOperation(final ModuleManagementSource factory, final ModuleDefinition pluginToAdd) {
		super();
		Assert.notNull(factory);
		Assert.notNull(pluginToAdd);
		this.factory = factory;
		this.pluginToAdd = pluginToAdd;
	}

	public boolean execute() {
		
		//FIXME comment and test
		
		ModuleStateHolder moduleStateHolder = factory.getModuleStateHolder();
		ModificationExtractor calculator = factory.getPluginModificationCalculatorRegistry().getPluginModificationCalculator(ModificationExtractorType.STICKY);
		addPlugin(moduleStateHolder, calculator, pluginToAdd);
		return true;
	}
	
	public static void addPlugin(ModuleStateHolder moduleStateHolder, ModificationExtractor calculator,
			ModuleDefinition moduleDefinition) {

		RootModuleDefinition oldSpec = moduleStateHolder.getRootModuleDefinition();
		RootModuleDefinition newSpec = moduleStateHolder.cloneRootModuleDefinition();

		ModuleDefinition parent = moduleDefinition.getRootDefinition();
		
		if (moduleDefinition instanceof RootModuleDefinition) {
			newSpec = (RootModuleDefinition) moduleDefinition;
		}
		else {

			ModuleDefinition newParent = null;

			if (parent == null) {
				newParent = newSpec;
			}
			else {
				String parentName = parent.getName();
				newParent = newSpec.findChildDefinition(parentName, true);

				if (newParent == null) {
					throw new IllegalStateException("Unable to find parent plugin " + parentName + " in " + newSpec);
				}
			}

			newParent.add(moduleDefinition);
			moduleDefinition.setParentDefinition(newParent);
		}

		TransitionSet transitions = calculator.getTransitions(oldSpec, newSpec);
		moduleStateHolder.processTransitions(transitions);
	}	
	
}
