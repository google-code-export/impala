package org.impalaframework.plugin.transition;

import org.impalaframework.plugin.loader.ApplicationContextLoader;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;
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

	public void process(PluginStateManager pluginStateManager, ParentSpec existingSpec, ParentSpec newSpec, PluginSpec plugin) {

		if (pluginStateManager.getPlugin(plugin.getName()) == null) {

			logger.info("Loading plugin " + plugin.getName());

			ConfigurableApplicationContext parent = null;
			PluginSpec parentSpec = plugin.getParent();
			if (parentSpec != null) {
				parent = pluginStateManager.getPlugin(parentSpec.getName());
			}

			ConfigurableApplicationContext loadContext = contextLoader.loadContext(plugin, parent);
			pluginStateManager.putPlugin(plugin.getName(), loadContext);

		}
		else {
			logger.warn("Attempted to load plugin " + plugin.getName()
					+ " which was already loaded. Suggest calling unload first.");
		}

	}
}
