package org.impalaframework.module.transition;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.ModuleStateHolder;
import org.impalaframework.module.loader.ApplicationContextLoader;
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

	public boolean process(ModuleStateHolder moduleStateHolder, RootModuleDefinition existingRootDefinition,
			RootModuleDefinition newRootDefinition, ModuleDefinition currentDefinition) {

		logger.info("Loading definition {}", currentDefinition.getName());

		boolean success = true;

		if (moduleStateHolder.getModule(currentDefinition.getName()) == null) {

			ConfigurableApplicationContext parentContext = null;
			ModuleDefinition parentDefinition = currentDefinition.getParentDefinition();
			if (parentDefinition != null) {
				parentContext = moduleStateHolder.getModule(parentDefinition.getName());
			}

			try {
				ConfigurableApplicationContext loadContext = contextLoader.loadContext(currentDefinition, parentContext);
				moduleStateHolder.putModule(currentDefinition.getName(), loadContext);
			}
			catch (RuntimeException e) {
				logger.error("Failed to handle loading of application module " + currentDefinition.getName(), e);
				success = false;
			}

		}
		else {
			logger.warn("Attempted to load module " + currentDefinition.getName()
					+ " which was already loaded. Suggest calling unload first.");
		}

		return success;

	}
}
