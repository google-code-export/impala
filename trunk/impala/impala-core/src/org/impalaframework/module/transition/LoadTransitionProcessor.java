/*
 * Copyright 2007-2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.module.transition;

import org.impalaframework.module.ApplicationContextLoader;
import org.impalaframework.module.ModuleStateHolder;
import org.impalaframework.module.TransitionProcessor;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

public class LoadTransitionProcessor implements TransitionProcessor {

	private static final Log logger = LogFactory.getLog(LoadTransitionProcessor.class);

	private ApplicationContextLoader contextLoader;

	public LoadTransitionProcessor(ApplicationContextLoader contextLoader) {
		super();
		Assert.notNull(contextLoader, "contextLoader cannot be null");
		this.contextLoader = contextLoader;
	}

	public boolean process(ModuleStateHolder moduleStateHolder, RootModuleDefinition newRootDefinition,
			ModuleDefinition currentDefinition) {

		logger.info("Loading definition " + currentDefinition.getName());

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
