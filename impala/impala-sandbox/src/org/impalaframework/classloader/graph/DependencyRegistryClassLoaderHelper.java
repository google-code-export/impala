package org.impalaframework.classloader.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.impalaframework.classloader.ClassLoaderFactory;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.util.ObjectUtils;

public class DependencyRegistryClassLoaderHelper  {

	private DependencyRegistry dependencyRegistry;
	
	//FIXME should use resource loader factory rather than class loader factory
	private ClassLoaderFactory classLoaderFactory;
	
	private ConcurrentHashMap<String, CustomClassLoader> classLoaders = new ConcurrentHashMap<String, CustomClassLoader>();

	public DependencyRegistryClassLoaderHelper(
			ClassLoaderFactory classLoaderFactory,
			DependencyRegistry dependencyRegistry) {
		super();
		this.dependencyRegistry = dependencyRegistry;
		this.classLoaderFactory = classLoaderFactory;
		
		final Collection<ModuleDefinition> allModules = this.dependencyRegistry.getAllModules();
		for (ModuleDefinition moduleDefinition : allModules) {
			addClassLoader(moduleDefinition);
		}
	}

	public ClassLoader newClassLoader(ClassLoader parent, Object data) {
		return null;
	}
	
	/**
	 * Creates and stores class loader for current module definition. Assumes none present
	 */
	private void addClassLoader(ModuleDefinition moduleDefinition) {
		//TODO move class loader creation mechanism to ClassLoaderFactory instance
		ClassLoader classLoader = classLoaderFactory.newClassLoader(null, moduleDefinition);
		
		//FIXME should have interface which we can use
		classLoaders.put(moduleDefinition.getName(), ObjectUtils.cast(classLoader, CustomClassLoader.class));
	}
	
	/**
	 * Gets class loaders for a particular named module
	 */
	public List<CustomClassLoader> getLoadersFor(String name) {

		//FIXME should return list of ResourceLoaders
		
		final List<ModuleDefinition> moduleList = dependencyRegistry.getModuleDependencyList(name);
		List<CustomClassLoader> classLoader = new ArrayList<CustomClassLoader>();
		
		for (ModuleDefinition module : moduleList) {
			classLoader.add(classLoaders.get(module.getName()));
		}
		
		return classLoader;
		
	}
	
}
