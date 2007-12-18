package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.modification.ModificationExtractorType;
import org.impalaframework.module.modification.ModuleModificationExtractor;
import org.impalaframework.module.modification.ModuleTransitionSet;
import org.impalaframework.module.transition.ModuleStateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class CloseRootModuleOperation implements ModuleOperation {

	//FIXME unit test
	
	final Logger logger = LoggerFactory.getLogger(CloseRootModuleOperation.class);

	private final ModuleManagementSource factory;

	public CloseRootModuleOperation(final ModuleManagementSource factory) {
		super();
		Assert.notNull(factory);
		this.factory = factory;
	}

	public boolean execute() {
		ModuleStateManager moduleStateManager = factory.getPluginStateManager();
		ModuleModificationExtractor calculator = factory.getPluginModificationCalculatorRegistry()
				.getPluginModificationCalculator(ModificationExtractorType.STRICT);
		RootModuleDefinition rootModuleDefinition = moduleStateManager.getParentSpec();

		if (rootModuleDefinition != null) {
			logger.info("Shutting down application context");
			ModuleTransitionSet transitions = calculator.getTransitions(rootModuleDefinition, null);
			moduleStateManager.processTransitions(transitions);
			return true;
		}
		else {
			return false;
		}
	}

}
