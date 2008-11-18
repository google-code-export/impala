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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.dag.CyclicDependencyException;
import org.impalaframework.dag.GraphHelper;
import org.impalaframework.dag.Vertex;
import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.graph.GraphModuleDefinition;
import org.impalaframework.module.definition.graph.GraphRootModuleDefinition;

//FIXME figure out the concurrency and life cycle rules for this
//FIXME do we want this class to be mutable. Probably not.
public class DependencyRegistry {

	private static final Log logger = LogFactory.getLog(DependencyRegistry.class);

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
		
		if (logger.isDebugEnabled()) {
			logger.debug("Vertices after build dependency registry");
			dump();
		}
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
		populateDefinition(addedVertices, moduleDefinition);
		
		populateVertexDependencies(addedVertices);
		
		//rebuild the sorted vertex list
		resort();
		
		if (logger.isInfoEnabled()) {
			logger.info("Added module " + moduleDefinition);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("Vertices after adding module");
			dump();
		}
	}

	/* ********************* Methods to sort dependencies  ********************* */
	
	/**
	 * Sorts in <i>reverse</i> order the collection of module definitions. Order determined by the 
	 * topological sort order of all {@link ModuleDefinition}s known to this {@link DependencyRegistry} instance.
	 * @see #sort(Collection)
	 */
	public List<ModuleDefinition> reverseSort(Collection<ModuleDefinition> sortable) {
		final List<ModuleDefinition> sorted = sort(sortable);
		Collections.reverse(sorted);
		return sorted;
	}
	
	/**
	 * Sorts in order the collection of module definitions. Order determined by the 
	 * topological sort order of all {@link ModuleDefinition}s known to this {@link DependencyRegistry} instance.
	 * @see #reverseSort(Collection)
	 */
	public List<ModuleDefinition> sort(Collection<ModuleDefinition> sortable) {
		
		//convert module definitions to vertices
		List<Vertex> vertices = this.getVerticesForModules(sortable);
		
		//set ordered list
		List<Vertex> ordered = new ArrayList<Vertex>();
		
		//sort these based in order
		populatedOrderedVertices(ordered, vertices);
		
		//reconvert back to vertices
		return getVerticesForModuleDefinitions(ordered);
	}
	
	/* ********************* Methods to show dependencies and dependees  ********************* */

	/**
	 * Gets ordered list of modules definitions on which a particular named module depends
	 */
	public List<ModuleDefinition> getOrderedModuleDependencies(String name) {
		final List<Vertex> vertices = getVertexDependencyList(name);
		return getVerticesForModuleDefinitions(vertices);
	}
	
	/**
	 * Gets subgraph of named module plus dependees
	 */
	public List<ModuleDefinition> getOrderedModuleDependees(String name) {
		List<Vertex> vertices = getVertexAndOrderedDependees(name);
		return getVerticesForModuleDefinitions(vertices);
	}

	/**
	 * Gets the {@link ModuleDefinition} which are direct dependees of the {@link ModuleDefinition} argument.
	 */
	public Collection<ModuleDefinition> getDirectDependees(ModuleDefinition definition) {
		
		final Collection<Vertex> vertices = dependees.get(definition.getName());
		
		if (vertices == null) 
			return Collections.emptySet();
		
		return getVerticesForModuleDefinitions(vertices);
	}
	
	/* ********************* returns all the modules known by the dependency registry **************** */

	/**
	 * Returns all {@link ModuleDefinition} known to this {@link DependencyRegistry} instance.
	 */
	public Collection<ModuleDefinition> getAllModules() {
		final Collection<Vertex> vertices = this.vertexMap.values();
		
		List<Vertex> list = new ArrayList<Vertex>();
		populatedOrderedVertices(list, vertices);
		
		final LinkedHashSet<ModuleDefinition> definitions = new LinkedHashSet<ModuleDefinition>();
		for (Vertex vertex : list) {
			definitions.add(vertex.getModuleDefinition());
		}
		return definitions;
	}
	
	/* ********************* Methods to remove vertices ********************* */
	
	/**
	 * Removes the current module as well as any of it's dependees
	 */
	public void remove(String name) {
		List<Vertex> orderedToRemove = getVertexAndOrderedDependees(name);
		removeVertexInOrder(orderedToRemove);
	}

	/* ********************* Private utility methods ********************* */
	
	private void buildVertexMap(List<ModuleDefinition> definitions) {
		
		List<Vertex> addedVertices = new ArrayList<Vertex>();
		for (ModuleDefinition moduleDefinition : definitions) {
			populateDefinition(addedVertices, moduleDefinition);
		}
		
		System.out.println("Added vertices: " + addedVertices);
		
		//FIXME robustify
		System.out.println(vertexMap);
		
		//add the dependency relationships between the added vertices
		populateVertexDependencies(addedVertices);
		
		//rebuild the sorted vertex list
		resort();
	}

	/**
	 * Gets the vertices for the modules for which the named module is a dependency
	 */
	private List<Vertex> getVertexDependees(String name) {
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
    private List<Vertex> getVertexAndOrderedDependees(String name) {
		final Vertex current = vertexMap.get(name);
		
		if (current == null) throw new IllegalStateException();
		
		//get all dependees
		final List<Vertex> dependees = getVertexDependees(name);
		List<Vertex> ordered = getOrderedDependees(current, dependees);
		return ordered;
	}
    
	/**
	 * Gets vertices representing the current and its dependees, topologically sorted
	 */
	private List<Vertex> getOrderedDependees(final Vertex current, final List<Vertex> dependees) {
		List<Vertex> ordered = new ArrayList<Vertex>();
		ordered.add(current);

		return populatedOrderedVertices(ordered, dependees);
	}

	/**
	 * Returns the {@link List} of {@link Vertex} instances on which the
	 * {@link Vertex} corresponding with the name parameter depends.
	 */
	private List<Vertex> getVertexDependencyList(String name) {
		Vertex vertex = vertexMap.get(name);
		
		if (vertex == null) {
			throw new InvalidStateException("No entry in dependency registry for module named '" + name + '"');
		}
		
		//list the vertices in the correct order
		final List<Vertex> vertextList = GraphHelper.list(vertex);
		return vertextList;
	}

	/**
	 * Returns the vertices contained in <code>sortable</code> according to the topological
	 * sort order of vertices known to this {@link DependencyRegistry} instance.
	 */
	private List<Vertex> populatedOrderedVertices(List<Vertex> ordered, Collection<Vertex> sortable) {
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
	 * Get the list of {@link ModuleDefinition} corresponding with the vertex {@link Collection}
	 */
	private static List<ModuleDefinition> getVerticesForModuleDefinitions(Collection<Vertex> moduleVertices) {
		
		List<ModuleDefinition> moduleDefinitions = new ArrayList<ModuleDefinition>();
		
		for (Vertex vertex : moduleVertices) {
			moduleDefinitions.add(vertex.getModuleDefinition());
		}
		
		return moduleDefinitions;
	}
	
	private List<Vertex> getVerticesForModules(Collection<ModuleDefinition> definitions) {
	
		List<Vertex> vertices = new ArrayList<Vertex>();
		for (ModuleDefinition moduleDefinition : definitions) {
			final Vertex vertex = vertexMap.get(moduleDefinition.getName());
			
			if (vertex == null) {
				//FIXME thro
				throw new RuntimeException("No entry in vertexMap for " + moduleDefinition.getName());
			}
			
			vertices.add(vertex);
		}
		return vertices;
	}

	/**
	 * Sets up the dependency relationship between a vertex and its dependency
	 * @param vertex the vertex (dependee)
	 * @param dependentVertex the dependency, that is the vertex the dependee depends on
	 */
	private void populateVertexDependency(Vertex vertex, Vertex dependentVertex) {
		vertex.addDependency(dependentVertex);
		
		final String dependeeName = dependentVertex.getName();
		
		Set<Vertex> list = dependees.get(dependeeName);
		if (list == null) {
			list = new HashSet<Vertex>();
			dependees.put(dependeeName, list);
		}
		list.add(vertex);
	}

	/**
	 * Stores vertex for current module definition. Assumes none present
	 * @return 
	 */
	private Vertex populateVertex(ModuleDefinition moduleDefinition) {
		String name = moduleDefinition.getName();
		final Vertex vertex = new Vertex(moduleDefinition);
		vertexMap.put(name, vertex);
		return vertex;
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
	 * Recursive method to add module definition.
	 * @param addedVertices 
	 */
	private void populateDefinition(List<Vertex> addedVertices, ModuleDefinition moduleDefinition) {
		
		addedVertices.add(populateVertex(moduleDefinition));
	
		final Collection<ModuleDefinition> childDefinitions = moduleDefinition.getChildDefinitions();
		
		for (ModuleDefinition childDefinition : childDefinitions) {
			populateDefinition(addedVertices, childDefinition);
		}
	}

	/**
	 * Sets up the dependency relationships between vertices based on the 
	 * dependency module names of the ModuleDefinitions
	 * @param addedVertices 
	 */
	private void populateVertexDependencies(List<Vertex> addedVertices) {
		for (Vertex vertex : addedVertices) {
			populateVertexDependencies(vertex);
		}
	}

	/**
	 * Sets up the dependencies for a particular named module
	 */
	private void populateVertexDependencies(Vertex vertex) {
		
		final ModuleDefinition moduleDefinition = vertex.getModuleDefinition();
		
		if (moduleDefinition instanceof GraphModuleDefinition) {
			
			GraphModuleDefinition graphDefinition = (GraphModuleDefinition) moduleDefinition;
			final String[] dependentModuleNames = graphDefinition.getDependentModuleNames();
			for (String dependent : dependentModuleNames) {
				
				final Vertex dependentVertex = vertexMap.get(dependent);
				
				//FIXME check not null
				if (dependentVertex == null) {
					
					throw new InvalidStateException("Unable to find module definition corresponding module '" + moduleDefinition.getName() + "' with dependency named '" + dependent + "'");
					
				} else {
					//register the vertex dependency
					populateVertexDependency(vertex, dependentVertex);
				}
			}
		}
	}

	/**
	 * Deregister from the dependencies list of dependees and the vertex map
	 */
	private void removeVertexInOrder(List<Vertex> vertices) {
		
		for (Vertex vertex : vertices) {
			removeVertex(vertex);
		}
	}

	private void removeVertex(Vertex vertex) {
		
		final List<Vertex> dependencies = vertex.getDependencies();
		for (Vertex dependency : dependencies) {
			final String dependencyName = dependency.getName();
			final Set<Vertex> dependees = this.dependees.get(dependencyName);
			dependees.remove(dependency);
		}
		
		System.out.println("Removing vertex " + vertex.getName());
		
		this.sorted.remove(vertex);
		this.vertexMap.remove(vertex.getName());
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
	
	private void dump() {
		if (!logger.isDebugEnabled()) return;
		logger.debug("Dependency registry state. Sorted vertices:");
		for (Vertex vertex : this.sorted) {
			logger.debug(vertex);
		}
	}

}
