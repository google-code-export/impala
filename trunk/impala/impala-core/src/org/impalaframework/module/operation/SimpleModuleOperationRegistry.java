package org.impalaframework.module.operation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.impalaframework.exception.NoServiceException;
import org.springframework.util.Assert;

public class SimpleModuleOperationRegistry implements ModuleOperationRegistry {

	private final Map<String, ModuleOperation> operations = new HashMap<String, ModuleOperation>();

	protected SimpleModuleOperationRegistry() {
		super();
	}

	protected void putOperation(String operationName, ModuleOperation operation) {
		this.operations.put(operationName, operation);
	}

	public void setContributedOperations(Map<String, ModuleOperation> contributedOperations) {
		Assert.notNull(contributedOperations, "contributedOperations cannot be null");

		// overrides existing operations in registry
		this.operations.putAll(contributedOperations);
	}

	public ModuleOperation getOperation(String name) {
		ModuleOperation moduleOperation = operations.get(name);

		if (moduleOperation == null) {
			throw new NoServiceException("No instance of " + ModuleOperation.class.getName()
					+ " available for operation named '" + name + "'");
		}

		return moduleOperation;
	}

	public Map<String, ModuleOperation> getOperations() {
		return Collections.unmodifiableMap(operations);
	}

}
