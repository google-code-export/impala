/*
 * Copyright 2007-2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.module.operation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.impalaframework.exception.NoServiceException;
import org.springframework.util.Assert;

/**
 * Implements a registry of {@link ModuleOperation} instances, keyed by name. 
 * The access point for {@link ModuleOperation}s, allowing a {@link ModuleOperation} to retrieved by operation name.
 *
 * @author Phil Zoio
 */
public class ModuleOperationRegistry {

	private final Map<String, ModuleOperation> operations = new HashMap<String, ModuleOperation>();

	protected ModuleOperationRegistry() {
		super();
	}

	protected void putOperation(String operationName, ModuleOperation operation) {
		this.operations.put(operationName, operation);
	}

	public void setOperations(Map<String, ModuleOperation> contributedOperations) {
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