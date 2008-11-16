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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.impalaframework.dag.CyclicDependencyException;
import org.impalaframework.dag.GraphHelper;
import org.impalaframework.dag.Vertex;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.graph.GraphModuleDefinition;
import org.impalaframework.module.definition.graph.GraphRootModuleDefinition;

//FIXME figure out the concurrency and life cycle rules for this
//FIXME do we want this class to be mutable. Probably not.
public class DependencyRegistry {

	private ConcurrentHashMap<String, Vertex> vertexMap = new ConcurrentHashMap<String, Vertex>();
	private ConcurrentHashMap<String, Set<Vertex>> dependees = new ConcurrentHashMap<String, Set<Vertex>>();
	private List<Vertex> sorted;

	public DependencyRegistry(List<ModuleDefinition> definitions) {
		super();
		this.buildVertexMap(definitions);
	}
	
	public DependencyRegistry(GraphRootModuleDefinition rootDefinition) {
		super();
		List<ModuleDefinition> definitions = new ArrayList<ModuleDefinition>();
		definitions.add(rootDefinition);
		definitions.addAll(Arrays.asList(rootDefinition.getSiblings()));
		this.buildVertexMap(definitions);
	}
	
	/* ****************** methods to populate dependency registry from initial set of definitions ***************** */

	private void buildVertexMap(List<ModuleDefinition> definitions) {
		
		List<Vertex> addedVertices = new ArrayList<Vertex>();
		for (ModuleDefinition moduleDefinition : definitions) {
			addDefinition(addedVertices, moduleDefinition);
		}
		
		System.out.println("Added vertices: " + addedVertices);
		
		//FIXME robustify
		System.out.println(vertexMap);
		
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
	 * dependency module names of the ModuleDefinitions
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
		
		final ModuleDefinition moduleDefinition = vertex.getModuleDefinition();
		
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
	 * Recursive method to add module definition.
	 * @param addedVertices 
	 */
	private void addDefinition(List<Vertex> addedVertices, ModuleDefinition moduleDefinition) {
		
		addedVertices.add(addVertex(moduleDefinition));

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
		final Vertex vertex = new Vertex(moduleDefinition);
		vertexMap.put(name, vertex);
		return vertex;
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
		
		ModuleDefinition parentDefinition = parentVertex.getModuleDefinition();
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
	
	/* ********************* Methods to show dependencies and dependees  ********************* */
	
	public List<ModuleDefinition> reverseSort(Collection<ModuleDefinition> sortable) {
		final List<ModuleDefinition> sorted = sort(sortable);
		Collections.reverse(sorted);
		return sorted;
	}
	
	public List<ModuleDefinition> sort(Collection<ModuleDefinition> sortable) {
		
		//convert module definitions to vertices
		List<Vertex> vertices = this.getVerticesForModules(sortable);
		
		//set ordered list
		List<Vertex> ordered = new ArrayList<Vertex>();
		
		//sort these based in order
		getOrderedVertices(ordered, vertices);
		
		//reconvert back to vertices
		return getVertexModuleDefinitions(ordered);
	}
	
	/* ********************* Methods to show dependencies and dependees  ********************* */

	/**
	 * Gets ordered list of modules definitions on which a particular named module depends
	 */
	public List<ModuleDefinition> getOrderedModuleDependencies(String name) {
		final List<Vertex> vertexDependencyList = getVertexDependencyList(name);
		return getVertexModuleDefinitions(vertexDependencyList);
	}
	
	/**
	 * Gets subgraph of named module plus dependees
	 */
	public List<ModuleDefinition> getOrderedModuleDependees(String name) {
		List<Vertex> vertexDependeeList = getSortedVertexAndDependees(name);
		return getVertexModuleDefinitions(vertexDependeeList);
	}
	
	/* ********************* returns all the modules known by the dependency registry **************** */

	public Collection<ModuleDefinition> getAllModules() {
		final Collection<Vertex> vertices = this.vertexMap.values();
		
		final LinkedHashSet<ModuleDefinition> modules = new LinkedHashSet<ModuleDefinition>();
		for (Vertex vertex : vertices) {
			modules.add(vertex.getModuleDefinition());
		}
		return modules;
	}
	
	/* ********************* Methods to remove vertices ********************* */
	
	/**
	 * Removes the current module as well as any of it's dependees
	 */
	public void remove(String name) {
		List<Vertex> orderedToRemove = getSortedVertexAndDependees(name);
		removeVertexInOrder(orderedToRemove);
	}

	/* ********************* Private utility methods ********************* */
	
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
		this.vertexMap.remove(vertex.getName());
	}
	
	/**
	 * Gets the vertices for the modules for which the named module is a dependency
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
	 * Gets a list of vertices including the one corresponding with the name, plus its dependees
	 * topologically sorted
	 */
    List<Vertex> getSortedVertexAndDependees(String name) {
		final Vertex current = vertexMap.get(name);
		
		if (current == null) throw new IllegalStateException();
		
		//get all dependees
		final List<Vertex> dependees = getVertexDependees(name);
		List<Vertex> ordered = getOrderedDependeeVertices(current, dependees);
		return ordered;
	}
    
	/**
	 * Gets vertices representing the current and its dependees, topologically sorted
	 */
	private List<Vertex> getOrderedDependeeVertices(final Vertex current, final List<Vertex> dependees) {
		List<Vertex> ordered = new ArrayList<Vertex>();
		ordered.add(current);

		return getOrderedVertices(ordered, dependees);
	}

	private List<Vertex> getOrderedVertices(List<Vertex> ordered, final List<Vertex> sortable) {
		//get the ordered to remove list
		List<Vertex> sorted = this.sorted;
		for (Vertex vertex : sorted) {
			if (sortable.contains(vertex)) {
				ordered.add(vertex);
			}
		}
		
		//FIXME should we do a check to ensure that all sortables are present in ordered list
		
		return ordered;
	}
	
	/**
	 * Get the list of module definitions corresponding with the vertex list
	 */
	private static List<ModuleDefinition> getVertexModuleDefinitions(Collection<Vertex> moduleVertices) {
		
		List<ModuleDefinition> moduleDefinitions = new ArrayList<ModuleDefinition>();
		
		for (Vertex vertex : moduleVertices) {
			moduleDefinitions.add(vertex.getModuleDefinition());
		}
		
		return moduleDefinitions;
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
	
	List<Vertex> getVertexDependencyList(String name) {
		Vertex vertex = vertexMap.get(name);
		
		if (vertex == null) {
			throw new IllegalStateException();
		}
		
		final List<Vertex> vertextList = GraphHelper.list(vertex);
		return vertextList;
	}

	public Collection<ModuleDefinition> getDirectDependees(ModuleDefinition definitions) {
		
		final Collection<Vertex> set = dependees.get(definitions.getName());
		
		if (set == null) 
			return Collections.emptySet();
		
		return getVertexModuleDefinitions(set);
	}
	
	private List<Vertex> getVerticesForModules(Collection<ModuleDefinition> sortable) {

		List<Vertex> vertices = new ArrayList<Vertex>();
		for (ModuleDefinition moduleDefinition : sortable) {
			final Vertex vertex = vertexMap.get(moduleDefinition.getName());
			
			if (vertex == null) {
				//FIXME thro
				throw new RuntimeException("No entry in vertexMap for " + moduleDefinition.getName());
			}
			
			vertices.add(vertex);
		}
		return vertices;
	}

}
