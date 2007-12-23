package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementFactory;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.ModuleStateHolder;
import org.impalaframework.module.modification.ModificationExtractor;
import org.impalaframework.module.modification.ModificationExtractorType;
import org.impalaframework.module.modification.TransitionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class UpdateRootModuleOperation implements ModuleOperation {
	
	//FIXME unit test
	
	final Logger logger = LoggerFactory.getLogger(CloseRootModuleOperation.class);

	private final ModuleManagementFactory factory;
	
	public UpdateRootModuleOperation(final ModuleManagementFactory factory) {
		super();
		Assert.notNull(factory);
		this.factory = factory;
	}

	public ModuleOperationResult execute(ModuleOperationInput moduleOperationInput) {

		Assert.notNull(moduleOperationInput, "moduleOperationInput cannot be null");
		ModuleStateHolder moduleStateHolder = factory.getModuleStateHolder();
		
		//note that the module definition source is externally supplied
		ModuleDefinitionSource newModuleDefinitionSource = moduleOperationInput.getModuleDefinitionSource();
		Assert.notNull(newModuleDefinitionSource, "moduleDefinitionSource is required as it specifies the new module definition to apply in " + this.getClass().getName());
		//FIXME test
		
		RootModuleDefinition newModuleDefinition = newModuleDefinitionSource.getModuleDefinition();
		RootModuleDefinition oldModuleDefinition = getExistingModuleDefinitionSource(factory);
		
		ModificationExtractorType modificationExtractorType = getPluginModificationType();
		
		// figure out the modules to reload
		ModificationExtractor calculator = factory.getModificationExtractorRegistry()
				.getModificationExtractor(modificationExtractorType);
		TransitionSet transitions = calculator.getTransitions(oldModuleDefinition, newModuleDefinition);
		moduleStateHolder.processTransitions(transitions);
		return ModuleOperationResult.TRUE;
	}

	protected ModificationExtractorType getPluginModificationType() {
		return ModificationExtractorType.STRICT;
	}

	protected RootModuleDefinition getExistingModuleDefinitionSource(ModuleManagementFactory factory) {
		return null;
	}
}
