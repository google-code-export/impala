package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.modification.ModificationCalculationType;
import org.impalaframework.module.modification.PluginModificationCalculator;
import org.impalaframework.module.modification.PluginTransitionSet;
import org.impalaframework.module.spec.ParentSpec;
import org.impalaframework.module.spec.PluginSpecProvider;
import org.impalaframework.module.transition.PluginStateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class LoadParentOperation implements PluginOperation {
	
	//FIXME unit test
	
	final Logger logger = LoggerFactory.getLogger(ShutParentOperation.class);

	private final ModuleManagementSource factory;

	private final PluginSpecProvider pluginSpecBuilder;

	public LoadParentOperation(final ModuleManagementSource factory, final PluginSpecProvider pluginSpecBuilder) {
		super();
		Assert.notNull(factory);
		Assert.notNull(pluginSpecBuilder);

		this.factory = factory;
		this.pluginSpecBuilder = pluginSpecBuilder;
	}

	public boolean execute() {
		
		PluginStateManager pluginStateManager = factory.getPluginStateManager();
		ParentSpec pluginSpec = pluginSpecBuilder.getPluginSpec();
		ParentSpec existingSpec = getExistingParentSpec(factory);
		
		ModificationCalculationType modificationCalculationType = getPluginModificationType();
		// figure out the plugins to reload
		PluginModificationCalculator calculator = factory.getPluginModificationCalculatorRegistry()
				.getPluginModificationCalculator(modificationCalculationType);
		PluginTransitionSet transitions = calculator.getTransitions(existingSpec, pluginSpec);
		pluginStateManager.processTransitions(transitions);
		return true;
	}

	protected ModificationCalculationType getPluginModificationType() {
		return ModificationCalculationType.STRICT;
	}

	protected ParentSpec getExistingParentSpec(ModuleManagementSource factory) {
		return null;
	}
}
