package org.impalaframework.module.operation;

public abstract class LockingModuleOperation implements ModuleOperation {

	public ModuleOperationResult execute(
			ModuleOperationInput moduleOperationInput) {
		ModuleOperationResult execute = null;
		try {
			execute = doExecute(moduleOperationInput);
		} finally {
		}
		return execute;
	}

	protected abstract ModuleOperationResult doExecute(
			ModuleOperationInput moduleOperationInput);

}
