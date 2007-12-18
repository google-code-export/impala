package org.impalaframework.module.transition;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.manager.ModuleStateHolder;
import org.springframework.util.Assert;

public class ReloadTransitionProcessor implements TransitionProcessor {

	private TransitionProcessor loadTransitionProcessor;

	private TransitionProcessor unloadTransitionProcessor;

	public boolean process(ModuleStateHolder moduleStateHolder, RootModuleDefinition existingSpec, RootModuleDefinition newSpec,
			ModuleDefinition plugin) {
		Assert.notNull(loadTransitionProcessor);
		Assert.notNull(unloadTransitionProcessor);

		boolean success = true;

		success = unloadTransitionProcessor.process(moduleStateHolder, existingSpec, newSpec, plugin);
		if (success) {
			success = loadTransitionProcessor.process(moduleStateHolder, existingSpec, newSpec, plugin);
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
