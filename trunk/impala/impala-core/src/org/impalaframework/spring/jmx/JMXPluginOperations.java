package org.impalaframework.spring.jmx;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.impalaframework.module.modification.ModuleModificationCalculator;
import org.impalaframework.module.modification.ModuleTransitionSet;
import org.impalaframework.module.spec.RootModuleDefinition;
import org.impalaframework.module.spec.ModuleDefinition;
import org.impalaframework.module.transition.PluginStateManager;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.util.Assert;

@ManagedResource(objectName = "impala:service=JMXPluginOperations", description = "MBean exposing configuration operations Impala application")
public class JMXPluginOperations {

	private ModuleModificationCalculator moduleModificationCalculator;

	private PluginStateManager pluginStateManager;

	public void init() {
		Assert.notNull(moduleModificationCalculator);
		Assert.notNull(pluginStateManager);
	}

	@ManagedOperation(description = "Operation to reload a plugin")
	@ManagedOperationParameters( { @ManagedOperationParameter(name = "Plugin name", description = "Name of plugin to reload") })
	public String reloadPlugin(String pluginName) {
		
		//FIXME use PluginOperation
		
		RootModuleDefinition originalSpec = pluginStateManager.getParentSpec();
		RootModuleDefinition newSpec = pluginStateManager.cloneParentSpec();

		ModuleDefinition found = newSpec.findPlugin(pluginName, true);

		if (found != null) {

			try {
				ModuleTransitionSet transitions = moduleModificationCalculator
						.reload(originalSpec, newSpec, pluginName);
				pluginStateManager.processTransitions(transitions);
				return "Successfully reloaded " + pluginName;
			}
			catch (Throwable e) {
				return ExceptionUtils.getStackTrace(e);
			}
		}
		else {
			return "Could not find plugin " + pluginName;
		}

	}

	public void setPluginStateManager(PluginStateManager pluginStateManager) {
		this.pluginStateManager = pluginStateManager;
	}

	public void setPluginModificationCalculator(ModuleModificationCalculator moduleModificationCalculator) {
		this.moduleModificationCalculator = moduleModificationCalculator;
	}

}