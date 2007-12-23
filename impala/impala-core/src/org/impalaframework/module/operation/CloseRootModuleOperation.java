package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementFactory;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.ModuleStateHolder;
import org.impalaframework.module.modification.ModificationExtractorType;
import org.impalaframework.module.modification.ModificationExtractor;
import org.impalaframework.module.modification.TransitionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class CloseRootModuleOperation implements ModuleOperation {

	//FIXME unit test
	
	final Logger logger = LoggerFactory.getLogger(CloseRootModuleOperation.class);

	private final ModuleManagementFactory factory;

	public CloseRootModuleOperation(final ModuleManagementFactory factory) {
		super();
		Assert.notNull(factory);
		this.factory = factory;
	}

	public ModuleOperationResult execute(ModuleOperationInput moduleOperationInput) {
		ModuleStateHolder moduleStateHolder = factory.getModuleStateHolder();
		ModificationExtractor calculator = factory.getModificationExtractorRegistry()
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
