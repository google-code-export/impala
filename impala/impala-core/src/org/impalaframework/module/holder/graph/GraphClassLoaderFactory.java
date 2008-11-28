/*
 * Copyright 2007-2008 the original author or authors.
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
import org.impalaframework.classloader.graph.CustomClassLoader;
import org.impalaframework.classloader.graph.DelegateClassLoader;
import org.impalaframework.classloader.graph.DependencyManager;
import org.impalaframework.classloader.graph.GraphClassLoader;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.util.ResourceUtils;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * {@link ClassLoaderFactory} implementation which returns a class loader representing the module
 * as per the second argument to {@link #newClassLoader(ClassLoader, Object)}. Returns
 * {@link GraphClassLoader} instance, that is, a class loader instance designed specifically
 * to be used when modules are arranged in a graph, rather than in a hierarchy.
 */
public class GraphClassLoaderFactory implements ClassLoaderFactory {

	private ModuleLocationResolver moduleLocationResolver;
	
	private GraphClassLoaderRegistry classLoaderRegistry;

	private GraphModuleStateHolder moduleStateHolder;	
	
	public void init() {
		Assert.notNull(moduleLocationResolver, "moduleLocationResolver cannot be null");
		Assert.notNull(classLoaderRegistry, "classLoaderRegistry cannot be null");
		Assert.notNull(moduleStateHolder, "moduleStateHolder cannot be null");
	}
	
	public ClassLoader newClassLoader(ClassLoader parent, ModuleDefinition moduleDefinition) {
		
		Assert.notNull(moduleDefinition, "moduleDefinition cannot be null");
		DependencyManager newDependencyManager = moduleStateHolder.getNewDependencyManager();
		
		Assert.notNull(newDependencyManager, "new dependency manager not available. Cannot create graph based class loader");
		
		return newClassLoader(newDependencyManager, moduleDefinition);
	}
	
	public GraphClassLoader newClassLoader(DependencyManager dependencyManager, ModuleDefinition moduleDefinition) {
		
		String moduleName = moduleDefinition.getName();
		GraphClassLoader classLoader = classLoaderRegistry.getClassLoader(moduleName);
		if (classLoader != null) {
			return classLoader;
		}
		
		CustomClassLoader resourceLoader = newResourceLoader(moduleDefinition);
		List<ModuleDefinition> dependencies = dependencyManager.getOrderedModuleDependencies(moduleDefinition.getName());
		
		List<GraphClassLoader> classLoaders = new ArrayList<GraphClassLoader>();
		for (ModuleDefinition dependency : dependencies) {
			if (dependency.getName().equals(moduleDefinition.getName())) continue;
			classLoaders.add(newClassLoader(dependencyManager, dependency));
		}
		
		GraphClassLoader gcl = new GraphClassLoader(new DelegateClassLoader(classLoaders), resourceLoader, moduleDefinition, false);
		classLoaderRegistry.addClassLoader(moduleDefinition.getName(), gcl);
		return gcl;
	}
	

    CustomClassLoader newResourceLoader(ModuleDefinition moduleDefinition) {
		final List<Resource> classLocations = moduleLocationResolver.getApplicationModuleClassLocations(moduleDefinition.getName());
		final File[] files = ResourceUtils.getFiles(classLocations);
		CustomClassLoader classLoader = new CustomClassLoader(files);
		return classLoader;
	}
    
	public void setModuleStateHolder(GraphModuleStateHolder graphModuleStateHolder) {
		this.moduleStateHolder = graphModuleStateHolder;
	}

	public void setClassLoaderRegistry(GraphClassLoaderRegistry classLoaderRegistry) {
		this.classLoaderRegistry = classLoaderRegistry;
	}

	public void setModuleLocationResolver(ModuleLocationResolver moduleLocationResolver) {
		this.moduleLocationResolver = moduleLocationResolver;
	}
}
