package org.impalaframework.classloader.graph;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.graph.GraphModuleDefinition;
import org.impalaframework.module.definition.graph.SimpleGraphModuleDefinition;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

public class GraphBasedClassLoaderTest extends TestCase {

	public void testClassLoader() throws Exception {
		DependencyRegistry registry = new DependencyRegistry(new TestClassResolver());
		
		List<ModuleDefinition> list = new ArrayList<ModuleDefinition>();
		
		newDefinition(list, "a");
		newDefinition(list, "b", "a");
		newDefinition(list, "c");
		newDefinition(list, "d", "b");
		newDefinition(list, "e", "c,d");
		newDefinition(list, "f", "b,e");
		newDefinition(list, "g", "c,d,f");	
		
		/*
a
b depends on a
c
d depends on b
e depends on c, d
f depends on b, e
g on c, d, f
		 */
		
		registry.buildVertexMap(list);
		
		GraphBasedClassLoader classLoader = new GraphBasedClassLoader(registry, "module-e");
		System.out.println(classLoader.loadClass("E"));
		System.out.println(classLoader.loadClass("EImpl"));
		System.out.println(classLoader.loadClass("C"));
		System.out.println(classLoader.loadClass("B"));
		System.out.println(classLoader.loadClass("A"));
		
		try {
			classLoader.loadClass("F");
			fail();
		} catch (ClassNotFoundException e) {
		}
		
		printModuleDependees(registry, "module-a");
		printModuleDependees(registry, "module-b");
		printModuleDependees(registry, "module-c");
		printModuleDependees(registry, "module-d");
		printModuleDependees(registry, "module-e");
		printModuleDependees(registry, "module-f");
		printModuleDependees(registry, "module-g");
		
	}

	private void printModuleDependees(DependencyRegistry registry,
			final String moduleName) {
		System.out.println("--------------- Module dependees: " + moduleName);
		final List<ModuleDefinition> dependees = registry.getDependees(moduleName);
		for (ModuleDefinition moduleDefinition : dependees) {
			System.out.println(moduleDefinition.getName());
		}
		System.out.println("---------------------------------------------");
	}

	private void newDefinition(List<ModuleDefinition> list, final String name, final String dependencies) {
		final String[] split = dependencies.split(",");
		for (int i = 0; i < split.length; i++) {
			split[i] = "module-" + split[i];
		}
		final List<String> dependencyList = Arrays.asList(split);
		GraphModuleDefinition definition = new SimpleGraphModuleDefinition("module-" + name, dependencyList);
		list.add(definition);
	}
	
	private void newDefinition(List<ModuleDefinition> list, final String name) {
		GraphModuleDefinition definition = new SimpleGraphModuleDefinition("module-" + name);
		list.add(definition);
	}
	
}

class TestClassResolver implements ModuleLocationResolver {

	private String rootLocation = "../impala-core/files/impala-classloader";
	
	public List<Resource> getApplicationModuleClassLocations(String moduleName) {
		File root = rootFileLocation();
		File moduleDirectory = new File(root, moduleName);
		File classDirectory = new File(moduleDirectory, "bin");
		final Resource resource = new FileSystemResource(classDirectory);
		
		Assert.isTrue(resource.exists());
		
		return Collections.singletonList(resource);
	}

	public List<Resource> getModuleTestClassLocations(String moduleName) {
		throw new UnsupportedOperationException();
	}

	public Resource getRootDirectory() {
		File file = rootFileLocation();
		return new FileSystemResource(file);
	}

	private File rootFileLocation() {
		File file = new File(rootLocation);
		return file;
	}
	
}
