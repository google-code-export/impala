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
import org.impalaframework.classloader.graph.DependencyRegistry;
import org.impalaframework.classloader.graph.GraphClassLoader;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.util.ObjectUtils;
import org.impalaframework.util.ResourceUtils;
import org.springframework.core.io.Resource;

//FIXME comment and test
public class GraphClassLoaderFactory implements ClassLoaderFactory {

	private ModuleLocationResolver moduleLocationResolver;
	
	private GraphClassLoaderRegistry classLoaderRegistry;

	private GraphModuleStateHolder graphModuleStateHolder;	

	public ClassLoader newClassLoader(ClassLoader parent, Object data) {
		
		ModuleDefinition moduleDefinition = ObjectUtils.cast(data, ModuleDefinition.class);
		return newClassLoader(graphModuleStateHolder.getNewDependencyRegistry(), moduleDefinition);
	}
	
	public GraphClassLoader newClassLoader(DependencyRegistry dependencyRegistry, ModuleDefinition moduleDefinition) {
		
		String moduleName = moduleDefinition.getName();
		GraphClassLoader classLoader = classLoaderRegistry.getClassLoader(moduleName);
		if (classLoader != null) {
			return classLoader;
		}
		
		CustomClassLoader resourceLoader = newResourceLoader(moduleDefinition);
		List<ModuleDefinition> dependencies = dependencyRegistry.getOrderedModuleDependencies(moduleDefinition.getName());
		
		List<GraphClassLoader> dcls = new ArrayList<GraphClassLoader>();
		for (ModuleDefinition dependency : dependencies) {
			if (dependency.getName().equals(moduleDefinition.getName())) continue;
			dcls.add(newClassLoader(dependencyRegistry, dependency));
		}
		
		GraphClassLoader gcl = new GraphClassLoader(new DelegateClassLoader(dcls), resourceLoader, moduleDefinition);
		classLoaderRegistry.addClassLoader(moduleDefinition.getName(), gcl);
		return gcl;
	}
	

    CustomClassLoader newResourceLoader(ModuleDefinition moduleDefinition) {
		final List<Resource> classLocations = moduleLocationResolver.getApplicationModuleClassLocations(moduleDefinition.getName());
		final File[] files = ResourceUtils.getFiles(classLocations);
		CustomClassLoader classLoader = new CustomClassLoader(files);
		return classLoader;
	}
    
	public void setGraphModuleStateHolder(GraphModuleStateHolder graphModuleStateHolder) {
		this.graphModuleStateHolder = graphModuleStateHolder;
	}

	public void setClassLoaderRegistry(GraphClassLoaderRegistry classLoaderRegistry) {
		this.classLoaderRegistry = classLoaderRegistry;
	}

	public void setModuleLocationResolver(ModuleLocationResolver moduleLocationResolver) {
		this.moduleLocationResolver = moduleLocationResolver;
	}

}
