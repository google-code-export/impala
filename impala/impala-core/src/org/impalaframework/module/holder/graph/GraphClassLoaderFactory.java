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

package org.impalaframework.module.holder.graph;

import java.util.ArrayList;
import java.util.List;

import org.impalaframework.classloader.ClassLoaderFactory;
import org.impalaframework.classloader.ClassRetriever;
import org.impalaframework.classloader.URLClassRetriever;
import org.impalaframework.classloader.graph.DelegateClassLoader;
import org.impalaframework.classloader.graph.EnhancedGraphClassLoader;
import org.impalaframework.classloader.graph.GraphClassLoader;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.definition.DependencyManager;
import org.impalaframework.module.spi.Application;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.util.ObjectUtils;
import org.impalaframework.util.ResourceUtils;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * {@link ClassLoaderFactory} implementation which returns a class loader representing the module
 * as per the second argument to {@link #newClassLoader(Application, ClassLoader, Object)}. Returns
 * {@link GraphClassLoader} instance, that is, a class loader instance designed specifically
 * to be used when modules are arranged in a graph, rather than in a hierarchy.
 */
public class GraphClassLoaderFactory implements ClassLoaderFactory {

    private ModuleLocationResolver moduleLocationResolver;
    
    private boolean parentClassLoaderFirst;
    
    public void init() {
        Assert.notNull(moduleLocationResolver, "moduleLocationResolver cannot be null");
    }
    
    public ClassLoader newClassLoader(Application application, ClassLoader parent, ModuleDefinition moduleDefinition) {

        final GraphModuleStateHolder moduleStateHolder = ObjectUtils.cast(application.getModuleStateHolder(), GraphModuleStateHolder.class);
        final GraphClassLoaderRegistry classLoaderRegistry = ObjectUtils.cast(application.getClassLoaderRegistry(), GraphClassLoaderRegistry.class);
        
        if (classLoaderRegistry.getApplicationClassLoader() == null) {
            classLoaderRegistry.setApplicationClassLoader(parent);
        }
        
        Assert.notNull(moduleDefinition, "moduleDefinition cannot be null");
        DependencyManager newDependencyManager = moduleStateHolder.getDependencyManager();
        
        Assert.notNull(newDependencyManager, "new dependency manager not available. Cannot create graph based class loader");
        
        return newClassLoader(classLoaderRegistry, newDependencyManager, moduleDefinition);
    }
    
    public GraphClassLoader newClassLoader(GraphClassLoaderRegistry classLoaderRegistry, DependencyManager dependencyManager, ModuleDefinition moduleDefinition) {
        
        String moduleName = moduleDefinition.getName();
        
        //use registered class loader if it already exists
        GraphClassLoader classLoader = classLoaderRegistry.getClassLoader(moduleName);
        if (classLoader != null) {
            return classLoader;
        }
        
        //create new resource loader for current module definition
        ClassRetriever moduleResourceRetriever = newModuleClassResourceRetriever(moduleDefinition);
        List<ModuleDefinition> dependencies = dependencyManager.getOrderedModuleDependencies(moduleDefinition.getName());
        
        //add list of dependent class loaders
        List<GraphClassLoader> classLoaders = new ArrayList<GraphClassLoader>();
        for (ModuleDefinition dependency : dependencies) {
            if (dependency.getName().equals(moduleDefinition.getName())) continue;
            classLoaders.add(newClassLoader(classLoaderRegistry, dependencyManager, dependency));
        }
        
        //set up parent class loader
        ClassLoader parentClassLoader = classLoaderRegistry.getApplicationClassLoader();
        ClassLoader parentClassLoaderToUse = parentClassLoader != null ? parentClassLoader : GraphClassLoaderFactory.class.getClassLoader();
        
        //get third party package to resource mapping. Question: do we need to restrict to packages
        ClassRetriever moduleJarResourceRetriever = newModuleJarResourceRetriever(moduleDefinition);
        
        GraphClassLoader gcl = newGraphClassLoader(moduleDefinition, moduleResourceRetriever, moduleJarResourceRetriever, classLoaders, parentClassLoaderToUse);
        classLoaderRegistry.addClassLoader(moduleDefinition.getName(), gcl);
        return gcl;
    }

    /**
     * Creates {@link GraphClassLoader} instance
     */
    protected GraphClassLoader newGraphClassLoader(
            ModuleDefinition moduleDefinition, 
            ClassRetriever moduleClassResourceRetriever,
            ClassRetriever moduleJarResourceRetriever, 
            List<GraphClassLoader> classLoaders, ClassLoader parentClassLoader) {
        return new EnhancedGraphClassLoader(parentClassLoader, new DelegateClassLoader(classLoaders), moduleClassResourceRetriever, moduleJarResourceRetriever, moduleDefinition, parentClassLoaderFirst);
    }
    
    /**
     * Gets class retriever for module classes and resources
     */
    ClassRetriever newModuleClassResourceRetriever(ModuleDefinition moduleDefinition) {
        final List<Resource> classLocations = moduleLocationResolver.getApplicationModuleClassLocations(moduleDefinition.getName());
        return resourcesRetriever(classLocations);
    }
    
    /**
     * Gets class retriever for internal jars
     */
    ClassRetriever newModuleJarResourceRetriever(ModuleDefinition moduleDefinition) {
        final List<Resource> classLocations = moduleLocationResolver.getModuleSpecificJarLocations(moduleDefinition.getName());
        if (classLocations != null && !classLocations.isEmpty()) {
            return resourcesRetriever(classLocations);
        }
        return null;
    }

    private ClassRetriever resourcesRetriever(List<Resource> classLocations) {
        return new URLClassRetriever(ResourceUtils.getFiles(classLocations));
    }
    
    protected final boolean isParentClassLoaderFirst() {
        return parentClassLoaderFirst;
    }

    public void setModuleLocationResolver(ModuleLocationResolver moduleLocationResolver) {
        this.moduleLocationResolver = moduleLocationResolver;
    }
    
    public void setParentClassLoaderFirst(boolean parentClassLoaderFirst) {
        this.parentClassLoaderFirst = parentClassLoaderFirst;
    }
}
