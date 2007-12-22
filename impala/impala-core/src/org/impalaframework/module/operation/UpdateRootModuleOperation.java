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
	private final ModuleDefinitionSource moduleDefinitionSource;

	public UpdateRootModuleOperation(final ModuleManagementSource factory, final ModuleDefinitionSource moduleDefinitionSource) {
		super();
		Assert.notNull(factory);
		Assert.notNull(moduleDefinitionSource);

		this.factory = factory;
		this.moduleDefinitionSource = moduleDefinitionSource;
	}

	public boolean execute() {
		
		ModuleStateHolder moduleStateHolder = factory.getModuleStateHolder();
		RootModuleDefinition moduleDefinition = moduleDefinitionSource.getModuleDefinition();
		RootModuleDefinition oldModuleDefinition = getExistingParentSpec(factory);
		
		ModificationExtractorType modificationExtractorType = getPluginModificationType();
		
		// figure out the modules to reload
		ModificationExtractor calculator = factory.getModificationExtractorRegistry()
				.getModificationExtractor(modificationExtractorType);
		TransitionSet transitions = calculator.getTransitions(oldModuleDefinition, moduleDefinition);
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
