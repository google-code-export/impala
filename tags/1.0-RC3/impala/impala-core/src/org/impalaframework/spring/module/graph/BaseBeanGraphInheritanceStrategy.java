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

package org.impalaframework.spring.module.graph;

import java.util.ArrayList;
import java.util.List;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.RuntimeModule;
import org.impalaframework.module.definition.DependencyManager;
import org.impalaframework.module.holder.graph.GraphModuleStateHolder;
import org.impalaframework.spring.module.SpringRuntimeModule;
import org.springframework.context.ApplicationContext;

public abstract class BaseBeanGraphInheritanceStrategy implements BeanGraphInheritanceStrategy {

    public ApplicationContext getParentApplicationContext(
            GraphModuleStateHolder graphModuleStateHolder,
            ApplicationContext parentApplicationContext,
            ModuleDefinition definition) {
        List<ApplicationContext> nonAncestorDependentContexts = getDependentApplicationContexts(definition, parentApplicationContext, graphModuleStateHolder);      
        final boolean delegatingParentToParents = getDelegateGetBeanCallsToParent();
        return new GraphDelegatingApplicationContext(parentApplicationContext, nonAncestorDependentContexts, delegatingParentToParents);
    }

    protected abstract boolean getDelegateGetBeanCallsToParent();

    protected abstract List<ApplicationContext> getDependentApplicationContexts(
            ModuleDefinition definition,
            ApplicationContext parentApplicationContext,
            GraphModuleStateHolder graphModuleStateHolder);

    protected List<ApplicationContext> getDependentApplicationContexts(
            ModuleDefinition definition,
            GraphModuleStateHolder graphModuleStateHolder) {
        
        DependencyManager dependencyManager = graphModuleStateHolder.getDependencyManager();
        
        //get the dependencies in correct order
        final List<ModuleDefinition> dependencies = dependencyManager.getOrderedModuleDependencies(definition.getName());   
        
        //remove the current definition from this list
        dependencies.remove(definition);
        
        final List<ApplicationContext> applicationContexts = new ArrayList<ApplicationContext>();
        
        for (ModuleDefinition moduleDefinition : dependencies) {
            
            final String currentName = moduleDefinition.getName();
            final RuntimeModule runtimeModule = graphModuleStateHolder.getModule(currentName);
            if (runtimeModule instanceof SpringRuntimeModule) {
                SpringRuntimeModule spr = (SpringRuntimeModule) runtimeModule;
                applicationContexts.add(spr.getApplicationContext());
            }
        }
        
        maybeAddRootParent(applicationContexts);
        return applicationContexts;
    }

    void maybeAddRootParent(final List<ApplicationContext> applicationContexts) {
        if (applicationContexts.size() > 0) {
            
            //try get parent of first, which will have been published in Spring's root application context.            
            final ApplicationContext applicationContext = applicationContexts.get(0);
            final ApplicationContext parent = applicationContext.getParent();
            if (parent != null) {
                applicationContexts.add(0, parent);
            }
        }
    }
}
