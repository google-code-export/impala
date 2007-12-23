package org.impalaframework.module.operation;

public interface ModuleOperation {
	
	//FIXME take Map of arguments, and return boolean, Map of results, for partially
	//constrained interface
	
	public ModuleOperationResult execute();
}
