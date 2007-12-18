package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.modification.ModificationExtractorType;
import org.impalaframework.module.modification.ModuleTransitionSet;
import org.impalaframework.module.transition.ModuleStateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class ReloadNamedModuleOperation implements ModuleOperation {

	// FIXME unit test

	final Logger logger = LoggerFactory.getLogger(ReloadNamedModuleOperation.class);

	private final ModuleManagementSource factory;

	private final String pluginToReload;

	public ReloadNamedModuleOperation(final ModuleManagementSource factory, final String pluginName) {
		super();
		Assert.notNull(factory);
		Assert.notNull(pluginName);

		this.factory = factory;
		this.pluginToReload = pluginName;
	}

	public boolean execute() {
		
		ModuleStateManager moduleStateManager = factory.getPluginStateManager();
		RootModuleDefinition oldPluginSpec = moduleStateManager.getParentSpec();
		RootModuleDefinition newPluginSpec = newPluginSpec();

		ModuleTransitionSet transitions = factory.getPluginModificationCalculatorRegistry()
				.getPluginModificationCalculator(ModificationExtractorType.STRICT).reload(oldPluginSpec,
						newPluginSpec, pluginToReload);
		moduleStateManager.processTransitions(transitions);

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
