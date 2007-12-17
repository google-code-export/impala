package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.modification.ModificationCalculationType;
import org.impalaframework.module.modification.ModuleModificationCalculator;
import org.impalaframework.module.modification.ModuleTransitionSet;
import org.impalaframework.module.spec.RootModuleDefinition;
import org.impalaframework.module.spec.ModuleDefinitionSource;
import org.impalaframework.module.transition.PluginStateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class ProcessModificationsOperation implements PluginOperation {

	//FIXME unit test
	
	final Logger logger = LoggerFactory.getLogger(ProcessModificationsOperation.class);

	private final ModuleManagementSource factory;

	private final ModuleDefinitionSource pluginSpecBuilder;

	public ProcessModificationsOperation(final ModuleManagementSource factory, final ModuleDefinitionSource pluginSpecBuilder) {
		super();
		Assert.notNull(factory);
		Assert.notNull(pluginSpecBuilder);

		this.factory = factory;
		this.pluginSpecBuilder = pluginSpecBuilder;
	}

	public boolean execute() {
		
		PluginStateManager pluginStateManager = factory.getPluginStateManager();
		RootModuleDefinition oldPluginSpec = pluginStateManager.cloneParentSpec();
		RootModuleDefinition newPluginSpec = pluginSpecBuilder.getModuleDefintion();

		ModuleModificationCalculator calculator = factory.getPluginModificationCalculatorRegistry()
				.getPluginModificationCalculator(ModificationCalculationType.STRICT);
		ModuleTransitionSet transitions = calculator.getTransitions(oldPluginSpec, newPluginSpec);
		pluginStateManager.processTransitions(transitions);
		return true;
	}
}
