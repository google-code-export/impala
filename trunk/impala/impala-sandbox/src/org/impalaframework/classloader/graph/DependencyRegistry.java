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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.impalaframework.classloader.ClassLoaderFactory;
import org.impalaframework.dag.CyclicDependencyException;
import org.impalaframework.dag.GraphHelper;
import org.impalaframework.dag.Vertex;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.graph.GraphModuleDefinition;
import org.impalaframework.util.ObjectUtils;

//FIXME figure out the concurrency and life cycle rules for this
public class DependencyRegistry {

	private ConcurrentHashMap<String, Vertex> vertexMap = new ConcurrentHashMap<String, Vertex>();
	private ConcurrentHashMap<String, CustomClassLoader> classLoaders = new ConcurrentHashMap<String, CustomClassLoader>();
	private ConcurrentHashMap<String, Set<Vertex>> dependees = new ConcurrentHashMap<String, Set<Vertex>>();
	private List<Vertex> sorted;

	private ClassLoaderFactory resolver;

	public DependencyRegistry(ClassLoaderFactory resolver) {
		super();
		this.resolver = resolver;
	}
	
	/* ****************** methods to populate dependency registry from initial set of definitions ***************** */

	public void buildVertexMap(List<ModuleDefinition> definitions) {
		
		List<Vertex> addedVertices = new ArrayList<Vertex>();
		for (ModuleDefinition moduleDefinition : definitions) {
			addDefinition(addedVertices, moduleDefinition);
		}
		
		System.out.println("Added vertices: " + addedVertices);
		
		//FIXME robustify
		System.out.println(vertexMap);
		System.out.println(classLoaders);
		
		//add the dependency relationships between the added vertices
		addVertexDependencies(addedVertices);
		
		//rebuild the sorted vertex list
		resort();
	}

	private void resort() {
		final List<Vertex> vertices = new ArrayList<Vertex>(vertexMap.values());
		for (Vertex vertex : vertices) {
			vertex.reset();
		}
		try {
			GraphHelper.topologicalSort(vertices);
		} catch (CyclicDependencyException e) {
			for (Vertex vertex : vertices) {
				System.out.print(vertex.getName() + ": ");
				final List<Vertex> dependencies = vertex.getDependencies();
				for (Vertex dependency : dependencies) {
					System.out.print(dependency.getName() + ",");
				}
				System.out.println();
			}
			throw e;
		}
		this.sorted = vertices;
	}

	/**
	 * Sets up the dependency relationships between vertices based on the 
	 * dependeny module names of the ModuleDefinitions
	 * @param addedVertices 
	 */
	private void addVertexDependencies(List<Vertex> addedVertices) {
		for (Vertex vertex : addedVertices) {
			addVertexDependencies(vertex);
		}
	}

	/**
	 * Sets up the dependencies for a particular named module
	 */
	private void addVertexDependencies(Vertex vertex) {
		
		final ModuleDefinition moduleDefinition = (ModuleDefinition) vertex.getNode();
		
		if (moduleDefinition instanceof GraphModuleDefinition) {
			
			GraphModuleDefinition graphDefinition = (GraphModuleDefinition) moduleDefinition;
			final String[] dependentModuleNames = graphDefinition.getDependentModuleNames();
			for (String dependent : dependentModuleNames) {
				
				final Vertex dependentVertex = vertexMap.get(dependent);
				
				//FIXME check not null
				if (dependentVertex == null) {
					
					//FIXME should this be an error. Need to distinguish between required and optional
					System.err.println("No entry found for dependent: " + dependent);
					
				} else {
					//register the vertex dependency
					addVertexDependency(vertex, dependentVertex);
				}
			}
		}
	}
	
	/**
	 * Recursive method to add module definition. Also adds classloader
	 * @param addedVertices 
	 */
	private void addDefinition(List<Vertex> addedVertices, ModuleDefinition moduleDefinition) {
		
		addedVertices.add(addVertex(moduleDefinition));
		addClassLoader(moduleDefinition);

		final Collection<ModuleDefinition> childDefinitions = moduleDefinition.getChildDefinitions();
		
		for (ModuleDefinition childDefinition : childDefinitions) {
			addDefinition(addedVertices, childDefinition);
		}
	}

	/**
	 * Stores vertex for current module definition. Assumes none present
	 * @return 
	 */
	private Vertex addVertex(ModuleDefinition moduleDefinition) {
		String name = moduleDefinition.getName();
		final Vertex vertex = new Vertex(name, moduleDefinition);
		vertexMap.put(name, vertex);
		return vertex;
	}
	
	/**
	 * Creates and stores class loader for current module definition. Assumes none present
	 */
	private void addClassLoader(ModuleDefinition moduleDefinition) {
		//TODO move class loader creation mechanism to ClassLoaderFactory instance
		ClassLoader classLoader = resolver.newClassLoader(null, moduleDefinition);
		
		//FIXME should have interface which we can use
		classLoaders.put(moduleDefinition.getName(), ObjectUtils.cast(classLoader, CustomClassLoader.class));
	}

	/**
	 * Sets up the dependency relationship between a vertex and its dependency
	 * @param vertex the vertex (dependee)
	 * @param dependentVertex the dependency, that is the vertex the dependee depends on
	 */
	private void addVertexDependency(Vertex vertex, Vertex dependentVertex) {
		vertex.addDependency(dependentVertex);
		
		final String dependeeName = dependentVertex.getName();
		
		Set<Vertex> list = dependees.get(dependeeName);
		if (list == null) {
			list = new HashSet<Vertex>();
			dependees.put(dependeeName, list);
		}
		list.add(vertex);
	}
	
	/* ********************* Methods to add subgraph of vertices ********************* */
	
	public void addModule(String parent, ModuleDefinition moduleDefinition) {
		final Vertex parentVertex = vertexMap.get(parent);
		
		if (parentVertex == null) {
			throw new IllegalStateException();
		}
		
		ModuleDefinition parentDefinition = (ModuleDefinition) parentVertex.getNode();
		parentDefinition.add(moduleDefinition);
		moduleDefinition.setParentDefinition(parentDefinition);
		
		//now recursively add definitions
		List<Vertex> addedVertices = new ArrayList<Vertex>();
		addDefinition(addedVertices, moduleDefinition);
		System.out.println(addedVertices);
		
		addVertexDependencies(addedVertices);
		
		//rebuild the sorted vertex list
		resort();
	}
	
	/* ********************* Methods to show dependees  ********************* */
	
	/**
	 * Returns the modules which depend on the named module
	 */
	public List<ModuleDefinition> getDependees(String name) {
		
		List<Vertex> moduleVertices = getVertexDependees(name);
		return getVertexModuleDefinitions(moduleVertices);
	}
	
	/**
	 * Gets the vertices for the modules dependent on the named module
	 */
	List<Vertex> getVertexDependees(String name) {
		final List<Vertex> fullList = new ArrayList<Vertex>(sorted);
		
		List<Vertex> targetList = new ArrayList<Vertex>();
		populateDependees(targetList, name);

		List<Vertex> moduleVertices = new ArrayList<Vertex>();
		
		//iterate over the full list to get the order, but pick out only the module definitions which are dependees
		for (Vertex vertex : fullList) {
			if (targetList.contains(vertex)) {
				moduleVertices.add(vertex);
			}
		}
		return moduleVertices;
	}
	
	/**
	 * Get the list of module definitions corresponding with the vertex list
	 */
	private static List<ModuleDefinition> getVertexModuleDefinitions(List<Vertex> moduleVertices) {
		
		List<ModuleDefinition> moduleDefinitions = new ArrayList<ModuleDefinition>();
		
		for (Vertex vertex : moduleVertices) {
			moduleDefinitions.add((ModuleDefinition) vertex.getNode());
		}
		
		return moduleDefinitions;
	}
	
	/**
	 * Gets a list of vertices including the one corresponding with the name, plus its dependees
	 * topologically sorted
	 */
    public List<Vertex> getSortedVertexAndDependees(String name) {
		final Vertex current = vertexMap.get(name);
		
		if (current == null) throw new IllegalStateException();
		
		//get all dependees
		final List<Vertex> dependees = getVertexDependees(name);
		List<Vertex> orderedToRemove = getOrderedDependeeVertices(current, dependees);
		return orderedToRemove;
	}

	/**
	 * Gets vertices representing the current and its dependees, topologically sorted
	 */
	List<Vertex> getOrderedDependeeVertices(final Vertex current, final List<Vertex> dependees) {
		List<Vertex> orderedToRemove = new ArrayList<Vertex>();
		orderedToRemove.add(current);

		//get the ordered to remove list
		List<Vertex> sorted = this.sorted;
		for (Vertex vertex : sorted) {
			if (dependees.contains(vertex)) {
				orderedToRemove.add(vertex);
			}
		}
		return orderedToRemove;
	}
	
	/* ********************* Methods to remove vertices ********************* */
	
	/**
	 * Removes the current module as well as any of it's dependees
	 */
	public void remove(String name) {
		List<Vertex> orderedToRemove = getSortedVertexAndDependees(name);
		removeVertexInOrder(orderedToRemove);
	}

	private void removeVertexInOrder(List<Vertex> orderedToRemove) {
		//deregister from the dependencies list of dependees, classloaders and the vertex map
		for (Vertex vertex : orderedToRemove) {
			removeVertex(vertex);
		}
	}

	private void removeVertex(Vertex vertex) {
		
		final List<Vertex> dependencies = vertex.getDependencies();
		for (Vertex dependency : dependencies) {
			final String dependencyName = dependency.getName();
			final Set<Vertex> dependeeSet = this.dependees.get(dependencyName);
			dependeeSet.remove(dependency);
		}
		
		System.out.println("Removing vertex " + vertex.getName());
		
		this.sorted.remove(vertex);
		this.classLoaders.remove(vertex.getName());
		this.vertexMap.remove(vertex.getName());
	}
	
	/**
	 * Recursive method to build the list of dependees for a particular named module.
	 * Does not order the dependencies in any way
	 */
	private void populateDependees(List<Vertex> targetList, String name) {
		//recursively build the dependee list
		Set<Vertex> depList = dependees.get(name);
		if (depList != null) {
			targetList.addAll(depList);
			for (Vertex vertex : depList) {
				populateDependees(targetList, vertex.getName());
			}
		}
	}

	/**
	 * Gets class loaders for a particular named module
	 */
	public List<CustomClassLoader> getLoadersFor(String name) {
		
		Vertex vertex = vertexMap.get(name);
		
		if (vertex == null) {
			throw new IllegalStateException();
		}
		
		final List<Vertex> vertextList = GraphHelper.list(vertex);
		List<CustomClassLoader> classLoader = new ArrayList<CustomClassLoader>();
		
		for (Vertex vert : vertextList) {
			classLoader.add(classLoaders.get(vert.getName()));
		}
		
		return classLoader;
		
	}

}
