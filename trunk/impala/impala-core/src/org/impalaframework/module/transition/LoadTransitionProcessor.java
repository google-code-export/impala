package org.impalaframework.module.transition;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.loader.ApplicationContextLoader;
import org.impalaframework.module.manager.ModuleStateHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

public class LoadTransitionProcessor implements TransitionProcessor {

	final Logger logger = LoggerFactory.getLogger(LoadTransitionProcessor.class);

	private ApplicationContextLoader contextLoader;

	public LoadTransitionProcessor(ApplicationContextLoader contextLoader) {
		super();
		Assert.notNull(contextLoader, "contextLoader cannot be null");
		this.contextLoader = contextLoader;
	}

	public boolean process(ModuleStateHolder moduleStateHolder, RootModuleDefinition existingSpec, RootModuleDefinition newSpec, ModuleDefinition plugin) {

		logger.info("Loading plugin " + plugin.getName());
		
		boolean success = true;
		
		if (moduleStateHolder.getPlugin(plugin.getName()) == null) {


			ConfigurableApplicationContext parent = null;
			ModuleDefinition parentSpec = plugin.getParent();
			if (parentSpec != null) {
				parent = moduleStateHolder.getPlugin(parentSpec.getName());
			}

			try {
				ConfigurableApplicationContext loadContext = contextLoader.loadContext(plugin, parent);
				moduleStateHolder.putPlugin(plugin.getName(), loadContext);
			}
			catch (RuntimeException e) {
				logger.error("Failed to handle loading of application plugin " + plugin.getName(), e);
				success = false;
			}

		}
		else {
			logger.warn("Attempted to load plugin " + plugin.getName()
					+ " which was already loaded. Suggest calling unload first.");
		}
		
		return success;

	}
}
