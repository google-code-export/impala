package org.impalaframework.classloader.graph;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.impalaframework.dag.GraphHelper;
import org.impalaframework.dag.Vertex;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.util.ResourceUtils;
import org.springframework.core.io.Resource;

public class DependencyRegistry {

	private ConcurrentHashMap<String, Vertex> vertexMap = new ConcurrentHashMap<String, Vertex>();
	private ConcurrentHashMap<String, ModuleDefinition> definitionMap = new ConcurrentHashMap<String, ModuleDefinition>();
	private ConcurrentHashMap<String, CustomClassLoader> classLoaders = new ConcurrentHashMap<String, CustomClassLoader>();

	private ModuleLocationResolver resolver;

	public DependencyRegistry(ModuleLocationResolver resolver) {
		super();
		this.resolver = resolver;
	}

	public void buildVertexMap(List<ModuleDefinition> definitions) {
		for (ModuleDefinition moduleDefinition : definitions) {
			addDefinition(moduleDefinition);
		}
		System.out.println(vertexMap);
		System.out.println(definitionMap);
		System.out.println(classLoaders);
	}

	private void addDefinition(ModuleDefinition moduleDefinition) {
		
		String name = moduleDefinition.getName();
		vertexMap.put(name, new Vertex(name));
		definitionMap.put(name, moduleDefinition);
		final List<Resource> classLocations = resolver.getApplicationModuleClassLocations(name);
		final File[] files = ResourceUtils.getFiles(classLocations);
		CustomClassLoader classLoader = new CustomClassLoader(files);
		classLoaders.put(name, classLoader);

		final Collection<ModuleDefinition> childDefinitions = moduleDefinition
				.getChildDefinitions();
		for (ModuleDefinition childDefinition : childDefinitions) {
			addDefinition(childDefinition);
		}
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
