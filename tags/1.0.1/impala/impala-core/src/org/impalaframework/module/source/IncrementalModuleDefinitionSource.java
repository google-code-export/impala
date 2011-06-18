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

package org.impalaframework.module.source;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.ModuleDefinitionSource;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionUtils;
import org.impalaframework.module.type.TypeReaderRegistry;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.springframework.util.Assert;

/**
 * Implementation of <code>ModuleDefinitionSource</code> which adds module to an existing <code>RootModuleDefinition</code>
 * @author Phil Zoio
 */
public class IncrementalModuleDefinitionSource extends BaseInternalModuleDefinitionSource implements ModuleDefinitionSource {
    
    private String moduleName;
    private RootModuleDefinition existingDefinition;
    private List<String> modulesToLoad = Collections.emptyList();
    private ModuleDefinition parentDefinition;
    private TypeReaderRegistry typeReaderRegistry;
    
    public IncrementalModuleDefinitionSource(ModuleLocationResolver resolver, TypeReaderRegistry typeReaders, RootModuleDefinition existingDefinition, String moduleName) {
        this(resolver, typeReaders, existingDefinition, moduleName, true);
    }

    public IncrementalModuleDefinitionSource(ModuleLocationResolver resolver, TypeReaderRegistry typeReaders, RootModuleDefinition existingDefinition, String moduleName, boolean loadDependendentModules) {
        super(resolver, loadDependendentModules);
        Assert.notNull(moduleName, "moduleName cannot be null");
        Assert.notNull(existingDefinition, "existingDefiniton cannot be null");
        Assert.notNull(typeReaders, "typeReaderRegistry cannot be null");
        this.moduleName = moduleName;
        this.existingDefinition = ModuleDefinitionUtils.cloneAndUnfreeze(existingDefinition);
        this.typeReaderRegistry = typeReaders;
    }

    /**
     * Returns new <code>RootModuleDefinition</code>
     */
    public RootModuleDefinition getModuleDefinition() {
        
        if (existingDefinition.findChildDefinition(moduleName, true) != null) {
            return existingDefinition;
        }
        
        buildMaps();
        ModuleDefinition parent = null;
        
        String childModule = moduleName;
        
        //start with parent module
        String parentModule = null;
        
        modulesToLoad = new ArrayList<String>();
        while (parent == null && childModule != null) {
            parent = existingDefinition.getChildModuleDefinition(childModule);
            
            if (parent == null) { 
                modulesToLoad.add(childModule); 
            }
            
            parentModule = childModule;
            childModule = getParents().get(childModule);
        }
        
        if (parent == null) {
            
            //we're now down to either the root module or one of its siblings
            
            //if parent is root module
            if (existingDefinition.getName().equals(parentModule)) {        
                parent = existingDefinition;
                
                // remove the last entry, as this belongs to the root
                modulesToLoad.remove(modulesToLoad.size() - 1);
            } else {
                
                //the parent module will be the sibling, if it already exists
                parent = existingDefinition.getSiblingModule(parentModule);
                
                //if the sibling root is already present, we don't need to load this
                if (parent != null) {
                    modulesToLoad.remove(modulesToLoad.size() - 1);
                }
                
            }

        }

        Collections.reverse(modulesToLoad);
        this.parentDefinition = parent;
        
        ModuleDefinitionSource internalModuleBuilder = getModuleBuilder();
        return internalModuleBuilder.getModuleDefinition();
    }

    protected ModuleDefinitionSource getModuleBuilder() {
        return new IncrementalPropertiesModuleDefinitionSource(typeReaderRegistry, existingDefinition, parentDefinition, getModuleProperties(), modulesToLoad);
    }

    void buildMaps() {
        String[] moduleNames = new String[]{this.moduleName};
        while (moduleNames.length != 0) {
            loadProperties(moduleNames);
            extractParentsAndChildren(moduleNames);
            String[] added = addDependentModuleProperties(moduleNames);
            extractParentsAndChildren(added);
            moduleNames = buildMissingModules();
        }
    }

    List<String> getModulesToLoad() {
        return modulesToLoad;
    }

    void setModulesToLoad(List<String> modulesToLoad) {
        this.modulesToLoad = modulesToLoad;
    }

}
