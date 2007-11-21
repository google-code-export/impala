package org.impalaframework.plugin.spec.transition;

import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;

public class UnloadTransitionProcessor implements TransitionProcessor {
	
	final Logger logger = LoggerFactory.getLogger(DefaultPluginStateManager.class);

	public void process(DefaultPluginStateManager pluginStateManager, ParentSpec existingSpec, ParentSpec newSpec, PluginSpec pluginSpec) {

		logger.info("Unloading plugin " + pluginSpec.getName());
		
		ConfigurableApplicationContext appContext = pluginStateManager.removePlugin(pluginSpec.getName());
		if (appContext != null) {
			appContext.close();
		}
	}
}
