package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementFactory;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.ModuleStateHolder;
import org.impalaframework.module.modification.ModificationExtractorType;
import org.impalaframework.module.modification.ModificationExtractor;
import org.impalaframework.module.modification.TransitionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class ProcessModificationsOperation implements ModuleOperation {

	//FIXME unit test
	
	final Logger logger = LoggerFactory.getLogger(ProcessModificationsOperation.class);

	private final ModuleManagementFactory factory;

	private final ModuleDefinitionSource moduleDefinitionSource;

	public ProcessModificationsOperation(final ModuleManagementFactory factory, final ModuleDefinitionSource moduleDefinitionSource) {
		super();
		Assert.notNull(factory);
		Assert.notNull(moduleDefinitionSource);

		this.factory = factory;
		this.moduleDefinitionSource = moduleDefinitionSource;
	}

	public ModuleOperationResult execute(ModuleOperationInput moduleOperationInput) {
		
		ModuleStateHolder moduleStateHolder = factory.getModuleStateHolder();
		RootModuleDefinition oldPluginSpec = moduleStateHolder.cloneRootModuleDefinition();
		RootModuleDefinition newPluginSpec = moduleDefinitionSource.getModuleDefinition();

		ModificationExtractor calculator = factory.getModificationExtractorRegistry()
				.getModificationExtractor(ModificationExtractorType.STRICT);
		TransitionSet transitions = calculator.getTransitions(oldPluginSpec, newPluginSpec);
		moduleStateHolder.processTransitions(transitions);
		return ModuleOperationResult.TRUE;
	}
}
