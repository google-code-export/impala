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

package org.impalaframework.module.spi;

import org.impalaframework.module.ModuleDefinition;
import org.springframework.util.Assert;

public final class ModuleStateChange {
    
    private final String transition;

    private final ModuleDefinition moduleDefinition;

    public ModuleStateChange(String transition, ModuleDefinition moduleDefinition) {
        super();
        Assert.notNull(transition);
        Assert.notNull(moduleDefinition);
        this.transition = transition;
        this.moduleDefinition = moduleDefinition;
    }

    public ModuleDefinition getModuleDefinition() {
        return moduleDefinition;
    }

    public String getTransition() {
        return transition;
    }
    
    @Override
    public String toString() {
        return new StringBuffer().append(getTransition()).append(" - ").append(getModuleDefinition().getName()).toString();
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((moduleDefinition.getName() == null) ? 0 : moduleDefinition.getName().hashCode());
        result = prime * result
                + ((transition == null) ? 0 : transition.hashCode());
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
        final ModuleStateChange other = (ModuleStateChange) obj;
        
        final String thisName = moduleDefinition.getName();
        final String otherName = other.moduleDefinition.getName();
        
        if (thisName == null) {
            if (otherName != null)
                return false;
        } else if (!thisName.equals(otherName))
            return false;
        if (transition == null) {
            if (other.transition != null)
                return false;
        } else if (!transition.equals(other.transition))
            return false;
        return true;
    }
    

}
