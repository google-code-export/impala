package org.impalaframework.module.operation;

import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.ModuleStateHolder;
import org.impalaframework.module.modification.ModificationExtractor;
import org.impalaframework.module.modification.ModificationExtractorRegistry;
import org.impalaframework.module.modification.ModificationExtractorType;
import org.impalaframework.module.modification.TransitionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloseRootModuleOperation extends BaseModuleOperation implements ModuleOperation {

	final Logger logger = LoggerFactory.getLogger(CloseRootModuleOperation.class);

	protected CloseRootModuleOperation() {
		super();
	}

	public ModuleOperationResult execute(ModuleOperationInput moduleOperationInput) {
		
		ModuleStateHolder moduleStateHolder = getModuleStateHolder();
		ModificationExtractorRegistry modificationExtractorRegistry = getModificationExtractorRegistry();
		ModificationExtractor calculator = modificationExtractorRegistry
				.getModificationExtractor(ModificationExtractorType.STRICT);
		RootModuleDefinition rootModuleDefinition = moduleStateHolder.getRootModuleDefinition();
		
		if (rootModuleDefinition != null) {
			logger.info("Shutting down application context");
			TransitionSet transitions = calculator.getTransitions(rootModuleDefinition, null);
			moduleStateHolder.processTransitions(transitions);
			return ModuleOperationResult.TRUE;
		}
		else {
			return ModuleOperationResult.FALSE;
		}
	}

}
