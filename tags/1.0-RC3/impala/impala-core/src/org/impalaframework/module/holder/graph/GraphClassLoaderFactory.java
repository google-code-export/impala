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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.impalaframework.classloader.ClassLoaderFactory;
import org.impalaframework.classloader.ClassRetriever;
import org.impalaframework.classloader.URLClassRetriever;
import org.impalaframework.classloader.graph.DelegateClassLoader;
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
        GraphClassLoader classLoader = classLoaderRegistry.getClassLoader(moduleName);
        if (classLoader != null) {
            return classLoader;
        }
        
        ClassRetriever resourceLoader = newResourceLoader(moduleDefinition);
        List<ModuleDefinition> dependencies = dependencyManager.getOrderedModuleDependencies(moduleDefinition.getName());
        
        List<GraphClassLoader> classLoaders = new ArrayList<GraphClassLoader>();
        for (ModuleDefinition dependency : dependencies) {
            if (dependency.getName().equals(moduleDefinition.getName())) continue;
            classLoaders.add(newClassLoader(classLoaderRegistry, dependencyManager, dependency));
        }
        
        ClassLoader parentClassLoader = classLoaderRegistry.getApplicationClassLoader();
        ClassLoader parentClassLoaderToUse = parentClassLoader != null ? parentClassLoader : GraphClassLoaderFactory.class.getClassLoader();
        
        GraphClassLoader gcl = newGraphClassLoader(moduleDefinition, resourceLoader, classLoaders, parentClassLoaderToUse);
        classLoaderRegistry.addClassLoader(moduleDefinition.getName(), gcl);
        return gcl;
    }

    protected GraphClassLoader newGraphClassLoader(
            ModuleDefinition moduleDefinition, 
            ClassRetriever resourceLoader,
            List<GraphClassLoader> classLoaders, 
            ClassLoader parentClassLoader) {
        return new GraphClassLoader(parentClassLoader, new DelegateClassLoader(classLoaders), resourceLoader, moduleDefinition, parentClassLoaderFirst);
    }
    
    ClassRetriever newResourceLoader(ModuleDefinition moduleDefinition) {
        final List<Resource> classLocations = moduleLocationResolver.getApplicationModuleClassLocations(moduleDefinition.getName());
        final File[] files = ResourceUtils.getFiles(classLocations);
        URLClassRetriever classLoader = new URLClassRetriever(files);
        return classLoader;
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
