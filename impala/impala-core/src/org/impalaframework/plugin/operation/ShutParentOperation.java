package org.impalaframework.plugin.operation;

import org.impalaframework.plugin.bootstrap.ModuleManagementSource;
import org.impalaframework.plugin.modification.ModificationCalculationType;
import org.impalaframework.plugin.modification.PluginModificationCalculator;
import org.impalaframework.plugin.modification.PluginTransitionSet;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.transition.PluginStateManager;
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
		PluginModificationCalculator calculator = factory.getPluginModificationCalculatorRegistry()
				.getPluginModificationCalculator(ModificationCalculationType.STRICT);
		ParentSpec parentSpec = pluginStateManager.getParentSpec();

		if (parentSpec != null) {
			logger.info("Shutting down application context");
			PluginTransitionSet transitions = calculator.getTransitions(parentSpec, null);
			pluginStateManager.processTransitions(transitions);
			return true;
		}
		else {
			return false;
		}
	}

}
