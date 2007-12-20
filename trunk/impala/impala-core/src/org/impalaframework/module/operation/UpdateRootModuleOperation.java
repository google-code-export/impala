package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.ModuleStateHolder;
import org.impalaframework.module.modification.ModificationExtractorType;
import org.impalaframework.module.modification.ModificationExtractor;
import org.impalaframework.module.modification.TransitionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class UpdateRootModuleOperation implements ModuleOperation {
	
	//FIXME unit test
	
	final Logger logger = LoggerFactory.getLogger(CloseRootModuleOperation.class);

	private final ModuleManagementSource factory;

	private final ModuleDefinitionSource pluginSpecBuilder;

	public UpdateRootModuleOperation(final ModuleManagementSource factory, final ModuleDefinitionSource pluginSpecBuilder) {
		super();
		Assert.notNull(factory);
		Assert.notNull(pluginSpecBuilder);

		this.factory = factory;
		this.pluginSpecBuilder = pluginSpecBuilder;
	}

	public boolean execute() {
		
		ModuleStateHolder moduleStateHolder = factory.getPluginStateManager();
		RootModuleDefinition pluginSpec = pluginSpecBuilder.getModuleDefinition();
		RootModuleDefinition existingSpec = getExistingParentSpec(factory);
		
		ModificationExtractorType modificationExtractorType = getPluginModificationType();
		// figure out the plugins to reload
		ModificationExtractor calculator = factory.getPluginModificationCalculatorRegistry()
				.getPluginModificationCalculator(modificationExtractorType);
		TransitionSet transitions = calculator.getTransitions(existingSpec, pluginSpec);
		moduleStateHolder.processTransitions(transitions);
		return true;
	}

	protected ModificationExtractorType getPluginModificationType() {
		return ModificationExtractorType.STRICT;
	}

	protected RootModuleDefinition getExistingParentSpec(ModuleManagementSource factory) {
		return null;
	}
}
