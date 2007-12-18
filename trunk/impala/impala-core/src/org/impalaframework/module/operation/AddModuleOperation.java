package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.manager.ModuleStateHolder;
import org.impalaframework.module.modification.ModificationExtractorType;
import org.impalaframework.module.modification.ModuleModificationExtractor;
import org.impalaframework.module.modification.ModuleTransitionSet;
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
		
		ModuleStateHolder moduleStateHolder = factory.getPluginStateManager();
		ModuleModificationExtractor calculator = factory.getPluginModificationCalculatorRegistry().getPluginModificationCalculator(ModificationExtractorType.STICKY);
		addPlugin(moduleStateHolder, calculator, pluginToAdd);
		return true;
	}
	
	public static void addPlugin(ModuleStateHolder moduleStateHolder, ModuleModificationExtractor calculator,
			ModuleDefinition moduleDefinition) {

		RootModuleDefinition oldSpec = moduleStateHolder.getParentSpec();
		RootModuleDefinition newSpec = moduleStateHolder.cloneParentSpec();

		ModuleDefinition parent = moduleDefinition.getParent();
		
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
				newParent = newSpec.findPlugin(parentName, true);

				if (newParent == null) {
					throw new IllegalStateException("Unable to find parent plugin " + parentName + " in " + newSpec);
				}
			}

			newParent.add(moduleDefinition);
			moduleDefinition.setParent(newParent);
		}

		ModuleTransitionSet transitions = calculator.getTransitions(oldSpec, newSpec);
		moduleStateHolder.processTransitions(transitions);
	}	
	
}
