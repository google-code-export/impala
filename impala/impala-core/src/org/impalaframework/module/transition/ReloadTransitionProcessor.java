package org.impalaframework.module.transition;

import org.impalaframework.module.spec.RootModuleDefinition;
import org.impalaframework.module.spec.ModuleDefinition;
import org.springframework.util.Assert;

public class ReloadTransitionProcessor implements TransitionProcessor {

	private TransitionProcessor loadTransitionProcessor;

	private TransitionProcessor unloadTransitionProcessor;

	public boolean process(PluginStateManager pluginStateManager, RootModuleDefinition existingSpec, RootModuleDefinition newSpec,
			ModuleDefinition plugin) {
		Assert.notNull(loadTransitionProcessor);
		Assert.notNull(unloadTransitionProcessor);

		boolean success = true;

		success = unloadTransitionProcessor.process(pluginStateManager, existingSpec, newSpec, plugin);
		if (success) {
			success = loadTransitionProcessor.process(pluginStateManager, existingSpec, newSpec, plugin);
		}

		return success;
	}

	public void setLoadTransitionProcessor(TransitionProcessor loadTransitionProcessor) {
		this.loadTransitionProcessor = loadTransitionProcessor;
	}

	public void setUnloadTransitionProcessor(TransitionProcessor unloadTransitionProcessor) {
		this.unloadTransitionProcessor = unloadTransitionProcessor;
	}

}
