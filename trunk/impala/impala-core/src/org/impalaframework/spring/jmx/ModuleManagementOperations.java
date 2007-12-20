package org.impalaframework.spring.jmx;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.ModuleStateHolder;
import org.impalaframework.module.modification.ModificationExtractor;
import org.impalaframework.module.modification.TransitionSet;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.util.Assert;

@ManagedResource(objectName = "impala:service=ModuleManagementOperations", description = "MBean exposing configuration operations Impala application")
public class ModuleManagementOperations {

	private ModificationExtractor modificationExtractor;

	private ModuleStateHolder moduleStateHolder;

	public void init() {
		Assert.notNull(modificationExtractor);
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
				TransitionSet transitions = modificationExtractor
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

	public void setPluginModificationCalculator(ModificationExtractor modificationExtractor) {
		this.modificationExtractor = modificationExtractor;
	}

}