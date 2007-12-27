package org.impalaframework.spring.jmx;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.impalaframework.module.operation.ModuleOperation;
import org.impalaframework.module.operation.ModuleOperationConstants;
import org.impalaframework.module.operation.ModuleOperationInput;
import org.impalaframework.module.operation.ModuleOperationRegistry;
import org.impalaframework.module.operation.ModuleOperationResult;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.util.Assert;

@ManagedResource(objectName = "impala:service=moduleManagementOperations", description = "MBean exposing configuration operations Impala application")
public class ModuleManagementOperations {

	private ModuleOperationRegistry moduleOperationRegistry;

	public void init() {
		Assert.notNull(moduleOperationRegistry);
	}

	@ManagedOperation(description = "Operation to reload a module")
	@ManagedOperationParameters( { @ManagedOperationParameter(name = "Module name", description = "Name of module to reload") })
	public String reloadPlugin(String moduleName) {

		ModuleOperation operation = moduleOperationRegistry.getOperation(ModuleOperationConstants.ReloadModuleNamedLikeOperation);
		
		try {
			ModuleOperationResult execute = operation.execute(new ModuleOperationInput(null, null, moduleName));
			if (execute.isSuccess()) {
				return "Successfully reloaded " + execute.getOutputParameters().get("moduleName");
			} else {
				return "Could not find plugin " + moduleName;
			}
		
		}
		catch (Throwable e) {
			return ExceptionUtils.getStackTrace(e);
		}
	}

	public void setModuleOperationRegistry(ModuleOperationRegistry moduleOperationRegistry) {
		this.moduleOperationRegistry = moduleOperationRegistry;
	}

}