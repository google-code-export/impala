package org.impalaframework.module.transition;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.manager.ModuleStateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;

public class UnloadTransitionProcessor implements TransitionProcessor {
	
	final Logger logger = LoggerFactory.getLogger(UnloadTransitionProcessor.class);

	public boolean process(ModuleStateManager moduleStateManager, RootModuleDefinition existingSpec, RootModuleDefinition newSpec, ModuleDefinition moduleDefinition) {

		logger.info("Unloading plugin " + moduleDefinition.getName());
		
		boolean success = true;
		
		ConfigurableApplicationContext appContext = moduleStateManager.removePlugin(moduleDefinition.getName());
		if (appContext != null) {
			try {
				appContext.close();
			}
			catch (RuntimeException e) {
				logger.error("Failed to handle unloading of application plugin " + moduleDefinition.getName(), e);
				success = false;
			}
		}
		return success;
	}
}
