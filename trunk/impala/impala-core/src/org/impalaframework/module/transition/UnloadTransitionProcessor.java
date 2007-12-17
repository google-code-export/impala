package org.impalaframework.module.transition;

import org.impalaframework.module.spec.ParentSpec;
import org.impalaframework.module.spec.PluginSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;

public class UnloadTransitionProcessor implements TransitionProcessor {
	
	final Logger logger = LoggerFactory.getLogger(UnloadTransitionProcessor.class);

	public boolean process(PluginStateManager pluginStateManager, ParentSpec existingSpec, ParentSpec newSpec, PluginSpec pluginSpec) {

		logger.info("Unloading plugin " + pluginSpec.getName());
		
		boolean success = true;
		
		ConfigurableApplicationContext appContext = pluginStateManager.removePlugin(pluginSpec.getName());
		if (appContext != null) {
			try {
				appContext.close();
			}
			catch (RuntimeException e) {
				logger.error("Failed to handle unloading of application plugin " + pluginSpec.getName(), e);
				success = false;
			}
		}
		return success;
	}
}
