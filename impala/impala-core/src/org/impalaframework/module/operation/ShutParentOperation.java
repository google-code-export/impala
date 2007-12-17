package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.modification.ModificationCalculationType;
import org.impalaframework.module.modification.ModuleModificationCalculator;
import org.impalaframework.module.modification.ModuleTransitionSet;
import org.impalaframework.module.spec.RootModuleDefinition;
import org.impalaframework.module.transition.PluginStateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class ShutParentOperation implements PluginOperation {

	//FIXME unit test
	
	final Logger logger = LoggerFactory.getLogger(ShutParentOperation.class);

	private final ModuleManagementSource factory;

	public ShutParentOperation(final ModuleManagementSource factory) {
		super();
		Assert.notNull(factory);
		this.factory = factory;
	}

	public boolean execute() {
		PluginStateManager pluginStateManager = factory.getPluginStateManager();
		ModuleModificationCalculator calculator = factory.getPluginModificationCalculatorRegistry()
				.getPluginModificationCalculator(ModificationCalculationType.STRICT);
		RootModuleDefinition rootModuleDefinition = pluginStateManager.getParentSpec();

		if (rootModuleDefinition != null) {
			logger.info("Shutting down application context");
			ModuleTransitionSet transitions = calculator.getTransitions(rootModuleDefinition, null);
			pluginStateManager.processTransitions(transitions);
			return true;
		}
		else {
			return false;
		}
	}

}
