package org.impalaframework.plugin.operation;

import org.impalaframework.plugin.bootstrap.ModuleManagementSource;
import org.impalaframework.plugin.modification.ModificationCalculationType;
import org.impalaframework.plugin.modification.PluginTransitionSet;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.transition.PluginStateManager;
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
		ParentSpec oldPluginSpec = pluginStateManager.getParentSpec();
		ParentSpec newPluginSpec = newPluginSpec();

		PluginTransitionSet transitions = factory.getPluginModificationCalculatorRegistry()
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

	protected ParentSpec newPluginSpec() {
		ParentSpec newPluginSpec = factory.getPluginStateManager().cloneParentSpec();
		return newPluginSpec;
	}
}
