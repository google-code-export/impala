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

package org.impalaframework.classloader.graph;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.impalaframework.dag.GraphHelper;
import org.impalaframework.dag.Vertex;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.graph.GraphModuleDefinition;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.util.ResourceUtils;
import org.springframework.core.io.Resource;

public class DependencyRegistry {

	private ConcurrentHashMap<String, Vertex> vertexMap = new ConcurrentHashMap<String, Vertex>();
	private ConcurrentHashMap<String, CustomClassLoader> classLoaders = new ConcurrentHashMap<String, CustomClassLoader>();
	private ConcurrentHashMap<String, List<Vertex>> dependees = new ConcurrentHashMap<String, List<Vertex>>();
	private List<Vertex> sorted;

	private ModuleLocationResolver resolver;

	public DependencyRegistry(ModuleLocationResolver resolver) {
		super();
		this.resolver = resolver;
	}

	public void buildVertexMap(List<ModuleDefinition> definitions) {
		for (ModuleDefinition moduleDefinition : definitions) {
			addDefinition(moduleDefinition);
		}
		
		//FIXME robustify
		System.out.println(vertexMap);
		System.out.println(classLoaders);
		
		final Set<String> definitionKeys = vertexMap.keySet();
		for (String key : definitionKeys) {
			final Vertex vertex = vertexMap.get(key);
			final ModuleDefinition moduleDefinition = (ModuleDefinition) vertex.getNode();
			
			if (moduleDefinition instanceof GraphModuleDefinition) {
				GraphModuleDefinition graphDefinition = (GraphModuleDefinition) moduleDefinition;
				final String[] dependentModuleNames = graphDefinition.getDependentModuleNames();
				for (String dependent : dependentModuleNames) {
					final Vertex dependentVertex = vertexMap.get(dependent);
					
					//FIXME check not null
					vertex.addDependency(dependentVertex);
					
					final String dependeeName = dependentVertex.getName();
					
					List<Vertex> list = dependees.get(dependeeName);
					if (list == null) {
						list = new ArrayList<Vertex>();
						dependees.put(dependeeName, list);
					}
					list.add(vertex);
				}
			}
		}
		
		final List<Vertex> vertex = new ArrayList<Vertex>(vertexMap.values());
		GraphHelper.topologicalSort(vertex);
		this.sorted = vertex;
	}

	/**
	 * Recursive method to add module definition
	 */
	private void addDefinition(ModuleDefinition moduleDefinition) {
		
		String name = moduleDefinition.getName();
		vertexMap.put(name, new Vertex(name, moduleDefinition));
		final List<Resource> classLocations = resolver.getApplicationModuleClassLocations(name);
		final File[] files = ResourceUtils.getFiles(classLocations);
		CustomClassLoader classLoader = new CustomClassLoader(files);
		classLoaders.put(name, classLoader);

		final Collection<ModuleDefinition> childDefinitions = moduleDefinition.getChildDefinitions();
		
		for (ModuleDefinition childDefinition : childDefinitions) {
			addDefinition(childDefinition);
		}
	}
	
	public List<ModuleDefinition> getDependees(String name) {
		
		final List<Vertex> fullList = new ArrayList<Vertex>(sorted);
		
		List<Vertex> targetList = new ArrayList<Vertex>();
		addDependees(targetList, name);

		List<ModuleDefinition> moduleDefinitions = new ArrayList<ModuleDefinition>();
		
		//iterate over the full list to get the order, but pick out only the module definitions which are dependees
		for (Vertex vertex : fullList) {
			if (targetList.contains(vertex)) {
				moduleDefinitions.add((ModuleDefinition) vertex.getNode());
			}
		}
		
		return moduleDefinitions;
	}

	private void addDependees(List<Vertex> targetList, String name) {
		//recursively build the dependee list
		List<Vertex> depList = dependees.get(name);
		if (depList != null) {
		targetList.addAll(depList);
		for (Vertex vertex : depList) {
			addDependees(targetList, vertex.getName());
		}}
	}

	public List<CustomClassLoader> getLoadersFor(String name) {
		Vertex vertex = vertexMap.get(name);
		final List<Vertex> vertextList = GraphHelper.list(vertex);
	
		List<CustomClassLoader> classLoader = new ArrayList<CustomClassLoader>();
		
		for (Vertex vert : vertextList) {
			classLoader.add(classLoaders.get(vert.getName()));
		}
		
		return classLoader;
		
	}

}
