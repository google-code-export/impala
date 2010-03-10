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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.RootModuleDefinition;

/**
 * @author Phil Zoio
 */
public class SimpleRootModuleDefinition extends BaseModuleDefinition implements RootModuleDefinition {
    
    private static final long serialVersionUID = 1L;
    
    private List<ModuleDefinition> siblings;

    /* ********************* constructors ******************** */
    
    public SimpleRootModuleDefinition(String name, String configLocations) {
        this(name, new String[]{ configLocations });
    }
    
    public SimpleRootModuleDefinition(String name, String[] configLocations) {
        this(name, configLocations, new String[0], null, new ModuleDefinition[0], null);
    }
    
    public SimpleRootModuleDefinition(
            String name,
            String[] configLocations,
            String[] dependencies,
            Map<String, String> attributes, 
            ModuleDefinition[] siblings, 
            String runtime) {
        
        super(null, name, ModuleTypes.ROOT, dependencies, configLocations, attributes, runtime);
        
        if (siblings == null) {
            siblings = new ModuleDefinition[0];
        }
        
        //not immutable, so needs to be backed by mutable List
        this.siblings = new ArrayList<ModuleDefinition>(Arrays.asList(siblings));
    }

    /* ********************* read-only methods ******************** */

    public ModuleDefinition getParentDefinition() {
        //by definition Parent does not have a parent of its own
        return null;
    }
    
    public List<ModuleDefinition> getSiblings() {
        return Collections.unmodifiableList(siblings);
    }
    
    public boolean hasSibling(String name) {
        return (getSiblingModule(name) != null);
    }

    public ModuleDefinition getSiblingModule(String name) {
        List<ModuleDefinition> newSibs = siblings;
        for (ModuleDefinition moduleDefinition : newSibs) {
            if (moduleDefinition.getName().equals(name)) {
                return moduleDefinition;
            }
        }
        return null;
    }   
    
    public ModuleDefinition findChildDefinition(String moduleName, boolean exactMatch) {
        return ModuleDefinitionWalker.walkRootDefinition(this, new ModuleMatchingCallback(moduleName, exactMatch));     
    }

    /* ********************* modification methods ******************** */   
    
    public void setParentDefinition(ModuleDefinition parentDefinition) {
        ModuleDefinitionUtils.ensureNotFrozen(this);
    }
    
    public void addSibling(ModuleDefinition siblingDefinition) {
        ModuleDefinitionUtils.ensureNotFrozen(this);
        
        this.siblings.add(siblingDefinition);
    }
    
    /* ********************* object override methods ******************** */    

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        
        //note that equals depends on dependencies, name and context locations
        //it does not depend on siblings and children, as it just a container for these, rather than 
        //using these as a source for its identity
        return super.equals(obj);
    }

    @Override
    public String toString() {
        ToStringCallback callback = new ToStringCallback();
        ModuleDefinitionWalker.walkRootDefinition(this, callback);
        return callback.toString();
    }
    
}
