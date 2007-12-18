package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.modification.ModificationCalculationType;
import org.impalaframework.module.modification.ModuleModificationCalculator;
import org.impalaframework.module.modification.ModuleTransitionSet;
import org.impalaframework.module.transition.PluginStateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class AddPluginOperation implements PluginOperation {

	final Logger logger = LoggerFactory.getLogger(AddPluginOperation.class);

	private final ModuleManagementSource factory;

	private final ModuleDefinition pluginToAdd;

	public AddPluginOperation(final ModuleManagementSource factory, final ModuleDefinition pluginToAdd) {
		super();
		Assert.notNull(factory);
		Assert.notNull(pluginToAdd);
		this.factory = factory;
		this.pluginToAdd = pluginToAdd;
	}

	public boolean execute() {
		
		//FIXME comment and test
		
		PluginStateManager pluginStateManager = factory.getPluginStateManager();
		ModuleModificationCalculator calculator = factory.getPluginModificationCalculatorRegistry().getPluginModificationCalculator(ModificationCalculationType.STICKY);
		addPlugin(pluginStateManager, calculator, pluginToAdd);
		return true;
	}
	
	public static void addPlugin(PluginStateManager pluginStateManager, ModuleModificationCalculator calculator,
			ModuleDefinition moduleDefinition) {

		RootModuleDefinition oldSpec = pluginStateManager.getParentSpec();
		RootModuleDefinition newSpec = pluginStateManager.cloneParentSpec();

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
		pluginStateManager.processTransitions(transitions);
	}	
	
}
