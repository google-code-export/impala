package org.impalaframework.spring.jmx;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.manager.ModuleStateHolder;
import org.impalaframework.module.modification.ModuleModificationExtractor;
import org.impalaframework.module.modification.ModuleTransitionSet;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.util.Assert;

@ManagedResource(objectName = "impala:service=JMXPluginOperations", description = "MBean exposing configuration operations Impala application")
public class JMXPluginOperations {

	private ModuleModificationExtractor moduleModificationExtractor;

	private ModuleStateHolder moduleStateHolder;

	public void init() {
		Assert.notNull(moduleModificationExtractor);
		Assert.notNull(moduleStateHolder);
	}

	@ManagedOperation(description = "Operation to reload a plugin")
	@ManagedOperationParameters( { @ManagedOperationParameter(name = "Plugin name", description = "Name of plugin to reload") })
	public String reloadPlugin(String pluginName) {
		
		//FIXME use ModuleOperation
		
		RootModuleDefinition originalSpec = moduleStateHolder.getParentSpec();
		RootModuleDefinition newSpec = moduleStateHolder.cloneParentSpec();

		ModuleDefinition found = newSpec.findPlugin(pluginName, true);

		if (found != null) {

			try {
				ModuleTransitionSet transitions = moduleModificationExtractor
						.reload(originalSpec, newSpec, pluginName);
				moduleStateHolder.processTransitions(transitions);
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

	public void setPluginStateManager(ModuleStateHolder moduleStateHolder) {
		this.moduleStateHolder = moduleStateHolder;
	}

	public void setPluginModificationCalculator(ModuleModificationExtractor moduleModificationExtractor) {
		this.moduleModificationExtractor = moduleModificationExtractor;
	}

}