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
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.module.Freezable;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.util.SerializationUtils;
import org.springframework.util.Assert;

/**
 * Class with static module definition related utility methods
 * @author Phil Zoio
 */
public abstract class ModuleDefinitionUtils {
    
    public static ModuleDefinition getModuleFromCollection(Collection<ModuleDefinition> moduleDefinitions, String name) {
        
        Assert.notNull(moduleDefinitions);
        Assert.notNull(name);
        
        for (ModuleDefinition moduleDefinition : moduleDefinitions) {
            if (name.equals(moduleDefinition.getName())) return moduleDefinition;
        }
        return null;
    }
    
    /**
     * Returns a collection of module names for all the modules explicitly
     * marked as non-reloadable. Does not implicitly mark modules as reloadable
     */
    public static Collection<String> getModulesMarkedNonReloadable(RootModuleDefinition root) {
    	
    	final Collection<String> nonReloadableModules = new ArrayList<String>();
    	
        ModuleDefinitionCallback callback = new ModuleDefinitionCallback() {
			
			@Override
			public boolean matches(ModuleDefinition moduleDefinition) {
				if (!moduleDefinition.isReloadable()) {
					nonReloadableModules.add(moduleDefinition.getName());
				}
				return false;
			}
		};
		ModuleDefinitionWalker.walkRootDefinition(root, callback);
		return nonReloadableModules;
    }
    
    /**
     * Implicitly marks non-reloadable all modules which are dependents of the named modules
     */
    public static int implicitlyMarkNonReloadable(RootModuleDefinition root, String name) {
    	int marked = 0;
        DependencyManager manager  = new DependencyManager(root);
        Collection<ModuleDefinition> dependentModules = manager.getOrderedModuleDependencies(name);
    	for (ModuleDefinition moduleDefinition : dependentModules) {
    		if (moduleDefinition.isReloadable()) {
				moduleDefinition.setNonReloadable();
				marked++;
    		}
		}
    	return marked;
    }
    
    public static Collection<ModuleDefinition> getDependentModules(RootModuleDefinition root, String name) {
        
        DependencyManager manager  = new DependencyManager(root);
        Collection<ModuleDefinition> directDependants = manager.getOrderedModuleDependants(name);
        return new LinkedList<ModuleDefinition>(directDependants).subList(1, directDependants.size());
    }

    public static List<String> getModuleNamesFromCollection(Collection<ModuleDefinition> moduleDefinitions) {
        Assert.notNull(moduleDefinitions);
        List<String> names = new ArrayList<String>();
        for (ModuleDefinition moduleDefinition : moduleDefinitions) {
            names.add(moduleDefinition.getName());
        }
        return names;
    }
    
    public static RootModuleDefinition cloneAndUnfreeze(RootModuleDefinition definition) {
        if (definition == null) {
            return null;
        }
        RootModuleDefinition newDefinition = (RootModuleDefinition) SerializationUtils.clone(definition);
        unfreeze(newDefinition);
        return newDefinition;
    }
    
    public static void ensureNotFrozen(Freezable freezable) {
        Assert.notNull(freezable);
        if (freezable.isFrozen()) {
            throw new InvalidStateException("Cannot change object '" + freezable + "' as this has been frozen");
        }
    }
    
    public static void freeze(RootModuleDefinition definition) {
        if (definition != null) {
            ModuleDefinitionWalker.walkRootDefinition(definition, new ModuleFreezeCallback(true));
        }
    }
    
    private static void unfreeze(RootModuleDefinition definition) {
        if (definition != null) {
            ModuleDefinitionWalker.walkRootDefinition(definition, new ModuleFreezeCallback(false));
        }
    }

    public static List<String> defaultContextLocations(String name) {
        return Collections.singletonList(name + "-context.xml");
    }
}
