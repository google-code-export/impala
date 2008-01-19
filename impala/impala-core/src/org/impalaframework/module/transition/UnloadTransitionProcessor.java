package org.impalaframework.module.transition;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.ModuleStateHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;

public class UnloadTransitionProcessor implements TransitionProcessor {

	final Logger logger = LoggerFactory.getLogger(UnloadTransitionProcessor.class);

	public boolean process(ModuleStateHolder moduleStateHolder, RootModuleDefinition newSpec,
			ModuleDefinition currentModuleDefinition) {

		logger.info("Unloading module " + currentModuleDefinition.getName());

		boolean success = true;

		ConfigurableApplicationContext appContext = moduleStateHolder.removeModule(currentModuleDefinition.getName());
		if (appContext != null) {
			try {
				appContext.close();
			}
			catch (RuntimeException e) {
				logger.error("Failed to handle unloading of application module " + currentModuleDefinition.getName(), e);
				success = false;
			}
		}
		return success;
	}
}
