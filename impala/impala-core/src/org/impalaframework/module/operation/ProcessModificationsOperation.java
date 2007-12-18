package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.manager.ModuleStateManager;
import org.impalaframework.module.modification.ModificationExtractorType;
import org.impalaframework.module.modification.ModuleModificationExtractor;
import org.impalaframework.module.modification.ModuleTransitionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class ProcessModificationsOperation implements ModuleOperation {

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
		
		ModuleStateManager moduleStateManager = factory.getPluginStateManager();
		RootModuleDefinition oldPluginSpec = moduleStateManager.cloneParentSpec();
		RootModuleDefinition newPluginSpec = pluginSpecBuilder.getModuleDefinition();

		ModuleModificationExtractor calculator = factory.getPluginModificationCalculatorRegistry()
				.getPluginModificationCalculator(ModificationExtractorType.STRICT);
		ModuleTransitionSet transitions = calculator.getTransitions(oldPluginSpec, newPluginSpec);
		moduleStateManager.processTransitions(transitions);
		return true;
	}
}
