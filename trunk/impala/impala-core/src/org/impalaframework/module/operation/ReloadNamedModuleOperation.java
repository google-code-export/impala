package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.ModuleStateHolder;
import org.impalaframework.module.modification.ModificationExtractorType;
import org.impalaframework.module.modification.TransitionSet;
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
		
		ModuleStateHolder moduleStateHolder = factory.getModuleStateHolder();
		RootModuleDefinition oldPluginSpec = moduleStateHolder.getRootModuleDefinition();
		RootModuleDefinition newPluginSpec = newPluginSpec();

		TransitionSet transitions = factory.getPluginModificationCalculatorRegistry()
				.getPluginModificationCalculator(ModificationExtractorType.STRICT).reload(oldPluginSpec,
						newPluginSpec, pluginToReload);
		moduleStateHolder.processTransitions(transitions);

		return !transitions.getPluginTransitions().isEmpty();
	}

	protected ModuleManagementSource getFactory() {
		return factory;
	}

	protected String getPluginToReload() {
		return pluginToReload;
	}

	protected RootModuleDefinition newPluginSpec() {
		RootModuleDefinition newPluginSpec = factory.getModuleStateHolder().cloneRootModuleDefinition();
		return newPluginSpec;
	}
}
