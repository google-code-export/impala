package org.impalaframework.classloader.graph;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.util.ResourceUtils;
import org.springframework.core.io.Resource;

public class DelegateClassLoaderFactory {

	private DependencyRegistry dependencyRegistry;
	private ModuleLocationResolver moduleLocationResolver;
	
	private Map<String,GraphClassLoader> graphClassLoaders = new HashMap<String, GraphClassLoader>();
	
	public DelegateClassLoaderFactory(DependencyRegistry dependencyRegistry,
			ModuleLocationResolver moduleLocationResolver) {
		super();
		this.dependencyRegistry = dependencyRegistry;
		this.moduleLocationResolver = moduleLocationResolver;
	}
	
	public GraphClassLoader newClassLoader(ModuleDefinition moduleDefinition) {
		
		GraphClassLoader graphClassLoader = graphClassLoaders.get(moduleDefinition.getName());
		if (graphClassLoader != null) {
			return graphClassLoader;
		}
		
		CustomClassLoader resourceLoader = newResourceLoader(moduleDefinition);
		List<ModuleDefinition> dependencies = dependencyRegistry.getOrderedModuleDependencies(moduleDefinition.getName());
		
		List<GraphClassLoader> dcls = new ArrayList<GraphClassLoader>();
		for (ModuleDefinition dependency : dependencies) {
			if (dependency.getName().equals(moduleDefinition.getName())) continue;
			dcls.add(newClassLoader(dependency));
		}
		
		GraphClassLoader gcl = new GraphClassLoader(new DelegateClassLoader(dcls), resourceLoader, moduleDefinition);
		graphClassLoaders.put(moduleDefinition.getName(), gcl);
		return gcl;
	}
	
	public CustomClassLoader newResourceLoader(ModuleDefinition moduleDefinition) {
		final List<Resource> classLocations = moduleLocationResolver.getApplicationModuleClassLocations(moduleDefinition.getName());
		final File[] files = ResourceUtils.getFiles(classLocations);
		CustomClassLoader classLoader = new CustomClassLoader(files);
		return classLoader;
	}
	
}
