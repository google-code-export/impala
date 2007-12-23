package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementFactory;

public class DefaultModuleOperationRegistry extends SimpleModuleOperationRegistry {

	public DefaultModuleOperationRegistry(ModuleManagementFactory factory) {
		super();

		putOperation(ModuleOperationConstants.AddModuleOperation, new AddModuleOperation(factory));
		putOperation(ModuleOperationConstants.CloseRootModuleOperation, new CloseRootModuleOperation(factory));
		putOperation(ModuleOperationConstants.ReloadNamedModuleOperation, new ReloadNamedModuleOperation(factory));
		putOperation(ModuleOperationConstants.RemoveModuleOperation, new RemoveModuleOperation(factory));
		putOperation(ModuleOperationConstants.UpdateRootModuleOperation, new UpdateRootModuleOperation(factory));
		putOperation(ModuleOperationConstants.ReloadRootModuleOperation, new ReloadRootModuleOperation(factory));
		putOperation(ModuleOperationConstants.IncrementalUpdateRootModuleOperation,
				new IncrementalUpdateRootModuleOperation(factory));
		putOperation(ModuleOperationConstants.ReloadModuleNamedLikeOperation, new ReloadModuleNamedLikeOperation(factory));
		
	}

}
