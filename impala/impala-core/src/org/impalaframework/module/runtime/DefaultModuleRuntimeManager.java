package org.impalaframework.module.runtime;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.ModuleRuntime;
import org.impalaframework.module.ModuleRuntimeManager;
import org.impalaframework.module.ModuleStateHolder;
import org.impalaframework.module.RuntimeModule;
import org.impalaframework.module.transition.LoadTransitionProcessor;

public class DefaultModuleRuntimeManager implements ModuleRuntimeManager {

	private static final Log logger = LogFactory.getLog(LoadTransitionProcessor.class);
	
	private Map<String, ModuleRuntime> moduleRuntimes;
	
	private ModuleStateHolder moduleStateHolder;
	
	public boolean initModule(ModuleDefinition currentDefinition) {
		
		boolean success = true;

		ModuleRuntime moduleRuntime = moduleRuntimes.get("spring");
		logger.info("Loading definition " + currentDefinition.getName());
		
		if (moduleStateHolder.getModule(currentDefinition.getName()) == null) {

			try {
				RuntimeModule runtimeModule = moduleRuntime.loadRuntimeModule(currentDefinition);
				moduleStateHolder.putModule(currentDefinition.getName(), runtimeModule);
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

	public void setModuleStateHolder(ModuleStateHolder moduleStateHolder) {
		this.moduleStateHolder = moduleStateHolder;
	}

	public void setModuleRuntimes(Map<String, ModuleRuntime> moduleRuntimes) {
		this.moduleRuntimes = moduleRuntimes;
	}

}
