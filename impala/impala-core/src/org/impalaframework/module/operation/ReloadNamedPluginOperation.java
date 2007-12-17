package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.modification.ModificationCalculationType;
import org.impalaframework.module.modification.ModuleTransitionSet;
import org.impalaframework.module.spec.RootModuleDefinition;
import org.impalaframework.module.transition.PluginStateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class ReloadNamedPluginOperation implements PluginOperation {

	// FIXME unit test

	final Logger logger = LoggerFactory.getLogger(ReloadNamedPluginOperation.class);

	private final ModuleManagementSource factory;

	private final String pluginToReload;

	public ReloadNamedPluginOperation(final ModuleManagementSource factory, final String pluginName) {
		super();
		Assert.notNull(factory);
		Assert.notNull(pluginName);

		this.factory = factory;
		this.pluginToReload = pluginName;
	}

	public boolean execute() {
		
		PluginStateManager pluginStateManager = factory.getPluginStateManager();
		RootModuleDefinition oldPluginSpec = pluginStateManager.getParentSpec();
		RootModuleDefinition newPluginSpec = newPluginSpec();

		ModuleTransitionSet transitions = factory.getPluginModificationCalculatorRegistry()
				.getPluginModificationCalculator(ModificationCalculationType.STRICT).reload(oldPluginSpec,
						newPluginSpec, pluginToReload);
		pluginStateManager.processTransitions(transitions);

		return !transitions.getPluginTransitions().isEmpty();
	}

	protected ModuleManagementSource getFactory() {
		return factory;
	}

	protected String getPluginToReload() {
		return pluginToReload;
	}

	protected RootModuleDefinition newPluginSpec() {
		RootModuleDefinition newPluginSpec = factory.getPluginStateManager().cloneParentSpec();
		return newPluginSpec;
	}
}
