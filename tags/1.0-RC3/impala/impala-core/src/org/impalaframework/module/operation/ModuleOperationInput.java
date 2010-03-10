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

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.ModuleDefinitionSource;

/**
 * Encapsulates input to be supplied to a {@link ModuleOperation} instance.
 * 
 * @author Phil Zoio
 */
public class ModuleOperationInput {

    // the external module definition source to be operated on. Should not be
    // available via the
    // current ModuleStateHolder instance
    private final ModuleDefinitionSource moduleDefinitionSource;

    // the external module definition to be operated on. Should not be available
    // via the
    // current ModuleStateHolder instance
    private final ModuleDefinition moduleDefinition;

    // the module to be operated on
    private final String moduleName;

    public ModuleOperationInput(
            ModuleDefinitionSource moduleDefinitionSource, 
            ModuleDefinition moduleDefinition,
            String moduleName) {
        super();
        this.moduleDefinitionSource = moduleDefinitionSource;
        this.moduleDefinition = moduleDefinition;
        this.moduleName = moduleName;
    }

    public ModuleDefinition getModuleDefinition() {
        return moduleDefinition;
    }

    public ModuleDefinitionSource getModuleDefinitionSource() {
        return moduleDefinitionSource;
    }

    public String getModuleName() {
        return moduleName;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((moduleDefinition == null) ? 0 : moduleDefinition.hashCode());
        result = PRIME * result + ((moduleDefinitionSource == null) ? 0 : moduleDefinitionSource.hashCode());
        result = PRIME * result + ((moduleName == null) ? 0 : moduleName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ModuleOperationInput other = (ModuleOperationInput) obj;
        if (moduleDefinition == null) {
            if (other.moduleDefinition != null)
                return false;
        }
        else if (!moduleDefinition.equals(other.moduleDefinition))
            return false;
        if (moduleDefinitionSource == null) {
            if (other.moduleDefinitionSource != null)
                return false;
        }
        else if (!moduleDefinitionSource.equals(other.moduleDefinitionSource))
            return false;
        if (moduleName == null) {
            if (other.moduleName != null)
                return false;
        }
        else if (!moduleName.equals(other.moduleName))
            return false;
        return true;
    }
    
    

}
