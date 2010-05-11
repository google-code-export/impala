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

package org.impalaframework.module.definition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.impalaframework.module.ModuleContainer;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.ModuleState;
import org.impalaframework.util.ArrayUtils;
import org.springframework.util.Assert;

/**
 * Common base class shared by {@link SimpleModuleDefinition} and
 * {@link SimpleRootModuleDefinition}
 * 
 * @author Phil Zoio
 */
public abstract class BaseModuleDefinition implements ModuleDefinition, ToStringAppendable {

    private static final long serialVersionUID = 1L;

    private String name;
    
    private String type;

    private String state;
    
    private String runtime;

    private ModuleContainer childContainer;

    private ModuleDefinition parentDefinition;
    
    private Map<String,String> attributes;

    private List<String> configLocations;

    private List<String> dependencies;
    
    private boolean frozen;

    /* ********************* constructor ******************** */
    
    public BaseModuleDefinition(ModuleDefinition parent, 
            String name, 
            String type, 
            String[] dependencies, 
            String[] configLocations, 
            Map<String, String> attributes, String runtime) {
        
        Assert.notNull(name);

        //use the default context locations if none supplied
        if (ArrayUtils.isNullOrEmpty(configLocations)) {
            configLocations = new String[0];
        }
        
        //if dependencies null just use empty array
        if (dependencies == null) {
            dependencies = new String[0];
        }
        
        if (attributes == null) {
            attributes = Collections.emptyMap();
        }
        
        if (runtime == null) {
            runtime = "spring";
        }
        
        if (type == null) {
            type = ModuleTypes.APPLICATION;
        }
        
        this.name = name;
        this.type = type;
        this.configLocations = Arrays.asList(configLocations);
        this.childContainer = new ModuleContainerImpl();
        this.dependencies = ArrayUtils.toList(dependencies);
        this.parentDefinition = parent;
        this.attributes = attributes;
        this.runtime = runtime;
        
        if (this.parentDefinition != null) {
            this.parentDefinition.addChildModuleDefinition(this);
        }
    }

    /* ********************* modification methods ******************** */   
    
    public List<String> getConfigLocations() {
        return Collections.unmodifiableList(configLocations);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getRuntimeFramework() {
        return runtime;
    }
    
    public ModuleDefinition getParentDefinition() {
        return parentDefinition;
    }

    public Collection<String> getChildModuleNames() {
        return childContainer.getChildModuleNames();
    }

    public ModuleDefinition getChildModuleDefinition(String moduleName) {
        return childContainer.getChildModuleDefinition(moduleName);
    }

    public Collection<ModuleDefinition> getChildModuleDefinitions() {
        return childContainer.getChildModuleDefinitions();
    }

    public boolean hasChildModuleDefinition(String moduleName) {
        return getChildModuleDefinition(moduleName) != null;
    }

    public String getState() {
        return state;
    }
    
    public List<String> getDependentModuleNames() { 
        
        List<String> dependencies = new ArrayList<String>(this.dependencies);
        if (this.parentDefinition != null) {
            
            //add parent as dependency if not already in list
            final String parentName = parentDefinition.getName();
            if (!dependencies.contains(parentName))
            {
                dependencies.add(0, parentName);
            }
        }
        return Collections.unmodifiableList(dependencies);
    }

    public Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    public List<String> getDependencies() {
        return dependencies;
    }

    public boolean isFrozen() {
        return frozen;
    }
    
    /* ********************* mutation methods ******************** */

    public void setParentDefinition(ModuleDefinition parentDefinition) {
        ModuleDefinitionUtils.ensureNotFrozen(this);
        this.parentDefinition = parentDefinition;
    }

    public void addChildModuleDefinition(ModuleDefinition moduleDefinition) {
        ModuleDefinitionUtils.ensureNotFrozen(this);
        childContainer.addChildModuleDefinition(moduleDefinition);
    }

    public ModuleDefinition removeChildModuleDefinition(String moduleName) {
        ModuleDefinitionUtils.ensureNotFrozen(this);
        
        return childContainer.removeChildModuleDefinition(moduleName);
    }

    public void setState(String state) {
        if (!(ModuleState.ERROR.equals(state) 
                || ModuleState.LOADED.equals(state)
                || ModuleState.UNLOADING.equals(state)
                || ModuleState.UNKNOWN.equals(state)
                || ModuleState.DEPENDENCY_FAILED.equals(state))) {
            ModuleDefinitionUtils.ensureNotFrozen(this);
        }
        this.state = state;
    }

    public void freeze() {
        this.frozen = true;
    }

    public void unfreeze() {
        this.frozen = false;
    }
    
    /* ********************* object override methods ******************** */    

    /*
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((configLocations == null) ? 0 : configLocations.hashCode());
        result = prime * result
                + ((dependencies == null) ? 0 : dependencies.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        final BaseModuleDefinition other = (BaseModuleDefinition) obj;
        if (configLocations == null) {
            if (other.configLocations != null)
                return false;
        } else if (!configLocations.equals(other.configLocations))
            return false;
        if (dependencies == null) {
            if (other.dependencies != null)
                return false;
        } else if (!dependencies.equals(other.dependencies))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
    */
    
    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((attributes == null) ? 0 : attributes.hashCode());
        result = prime * result
                + ((configLocations == null) ? 0 : configLocations.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((runtime == null) ? 0 : runtime.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        BaseModuleDefinition other = (BaseModuleDefinition) obj;
        if (attributes == null) {
            if (other.attributes != null)
                return false;
        } else if (!attributes.equals(other.attributes))
            return false;
        if (dependencies == null) {
            if (other.dependencies != null)
                return false;
        } else if (!dependencies.equals(other.dependencies))
            return false;
        if (configLocations == null) {
            if (other.configLocations != null)
                return false;
        } else if (!configLocations.equals(other.configLocations))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (runtime == null) {
            if (other.runtime != null)
                return false;
        } else if (!runtime.equalsIgnoreCase(other.runtime))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equalsIgnoreCase(other.type))
            return false;
        return true;
    }

    @Override
    public String toString() {
        ToStringCallback callback = new ToStringCallback();
        ModuleDefinitionWalker.walkModuleDefinition(this, callback);
        return callback.toString();
    }

    public void toString(StringBuffer buffer) {
        buffer.append("name=" + getName());
        buffer.append(", configLocations=" + getConfigLocations());
        buffer.append(", type=" + getType());
        buffer.append(", dependencies=" + getDependentModuleNames());
        if (!attributes.isEmpty()) {
            buffer.append(", attributes=" + getDependentModuleNames());
        }
        buffer.append(", runtime=" + getRuntimeFramework());
        if (getState() != null) {
            buffer.append(", state=" + getState());
        }
    }
    
}
