package org.impalaframework.module.transition;

import org.impalaframework.module.spec.RootModuleDefinition;
import org.impalaframework.module.spec.ModuleDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;

public class UnloadTransitionProcessor implements TransitionProcessor {
	
	final Logger logger = LoggerFactory.getLogger(UnloadTransitionProcessor.class);

	public boolean process(PluginStateManager pluginStateManager, RootModuleDefinition existingSpec, RootModuleDefinition newSpec, ModuleDefinition moduleDefinition) {

		logger.info("Unloading plugin " + moduleDefinition.getName());
		
		boolean success = true;
		
		ConfigurableApplicationContext appContext = pluginStateManager.removePlugin(moduleDefinition.getName());
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
