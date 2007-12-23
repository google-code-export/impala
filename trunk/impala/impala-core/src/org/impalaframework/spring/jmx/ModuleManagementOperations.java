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

@ManagedResource(objectName = "impala:service=moduleManagementOperations", description = "MBean exposing configuration operations Impala application")
public class ModuleManagementOperations {
	
	private ModificationExtractor modificationExtractor;

	private ModuleStateHolder moduleStateHolder;

	public void init() {
		Assert.notNull(modificationExtractor);
		Assert.notNull(moduleStateHolder);
	}

	@ManagedOperation(description = "Operation to reload a module")
	@ManagedOperationParameters( { @ManagedOperationParameter(name = "Module name", description = "Name of module to reload") })
	public String reloadPlugin(String moduleName) {
		
		//FIXME use ModuleOperation
		
		
		
		RootModuleDefinition originalDefinition = moduleStateHolder.getRootModuleDefinition();
		RootModuleDefinition newDefinition = moduleStateHolder.cloneRootModuleDefinition();

		ModuleDefinition found = newDefinition.findChildDefinition(moduleName, true);

		if (found != null) {

			try {
				TransitionSet transitions = modificationExtractor
						.reload(originalDefinition, newDefinition, moduleName);
				moduleStateHolder.processTransitions(transitions);
				return "Successfully reloaded " + moduleName;
			}
			catch (Throwable e) {
				return ExceptionUtils.getStackTrace(e);
			}
		}
		else {
			return "Could not find plugin " + moduleName;
		}

	}

	public void setModuleStateHolder(ModuleStateHolder moduleStateHolder) {
		this.moduleStateHolder = moduleStateHolder;
	}

	public void setPluginModificationCalculator(ModificationExtractor modificationExtractor) {
		this.modificationExtractor = modificationExtractor;
	}

}