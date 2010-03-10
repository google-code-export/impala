/*
 * Copyright 2007-2010 the original author or authors.
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

import java.util.Map;

import org.impalaframework.registry.Registry;
import org.impalaframework.registry.RegistrySupport;

/**
 * Implements a registry of {@link ModuleOperation} instances, keyed by name. 
 * The access point for {@link ModuleOperation}s, allowing a {@link ModuleOperation} to retrieved by operation name.
 *
 * @author Phil Zoio
 */
public class ModuleOperationRegistry extends RegistrySupport implements Registry<ModuleOperation> {

    protected ModuleOperationRegistry() {
        super();
    }

    public void addItem(String operationName, ModuleOperation operation) {
        super.addRegistryItem(operationName, operation);
    }

    public void setOperations(Map<String, ModuleOperation> operations) {
        super.setEntries(operations);
    }

    public ModuleOperation getOperation(String name) {
        return super.getEntry(name, ModuleOperation.class);
    }

    @SuppressWarnings("unchecked")
    public Map<String, ModuleOperation> getOperations() {
        final Map entries = super.getEntries();
        return entries;
    }

}
