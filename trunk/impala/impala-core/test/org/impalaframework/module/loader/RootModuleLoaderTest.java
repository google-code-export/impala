package org.impalaframework.module.loader;

import junit.framework.TestCase;

import org.impalaframework.classloader.ModuleClassLoader;
import org.impalaframework.module.builder.SimpleModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.loader.RootModuleLoader;
import org.impalaframework.resolver.LocationConstants;
import org.impalaframework.resolver.StandaloneModuleLocationResolver;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * @author Phil Zoio
 */
public class RootModuleLoaderTest extends TestCase {

	private static final String plugin1 = "impala-sample-dynamic-plugin1";

	private static final String plugin2 = "impala-sample-dynamic-plugin2";

	private RootModuleLoader pluginLoader;

	private ModuleDefinitionSource source;

	public void setUp() {
		System.setProperty(LocationConstants.ROOT_PROJECTS_PROPERTY, "impala-core");
		StandaloneModuleLocationResolver locationResolver = new StandaloneModuleLocationResolver();
		pluginLoader = new RootModuleLoader(locationResolver);
		source = new SimpleModuleDefinitionSource("parentTestContext.xml", new String[] { plugin1, plugin2 });
	}

	public void tearDown() {
		System.clearProperty(LocationConstants.ROOT_PROJECTS_PROPERTY);
	}

	public final void testGetClassLocations() {
		final Resource[] classLocations = pluginLoader.getClassLocations(source.getModuleDefinition());
		for (Resource resource : classLocations) {
			assertTrue(resource instanceof FileSystemResource);
			assertTrue(resource.exists());
		}
	}

	public final void testGetClassLoader() {
		final ClassLoader classLoader = pluginLoader.newClassLoader(source.getModuleDefinition(), null);
		assertTrue(classLoader instanceof ModuleClassLoader);
		assertTrue(classLoader.getParent().getClass().equals(this.getClass().getClassLoader().getClass()));
	}

	public void testGetSpringLocations() {
		final ClassLoader classLoader = pluginLoader.newClassLoader(source.getModuleDefinition(), null);
		final Resource[] springConfigResources = pluginLoader.getSpringConfigResources(source.getModuleDefinition(),
				classLoader);

		assertEquals(1, springConfigResources.length);
		assertEquals(ClassPathResource.class, springConfigResources[0].getClass());
		assertTrue(springConfigResources[0].exists());

	}
	
	public void testGetParentLocations() {

		System.setProperty(LocationConstants.ROOT_PROJECTS_PROPERTY, "impala-core, impala-interactive");
		StandaloneModuleLocationResolver locationResolver = new StandaloneModuleLocationResolver();
		pluginLoader = new RootModuleLoader(locationResolver);
		
		Resource[] parentClassLocations = pluginLoader.getRootClassLocations();
		assertEquals(2, parentClassLocations.length);
		assertTrue(parentClassLocations[0].getDescription().contains("impala-core"));
		assertTrue(parentClassLocations[1].getDescription().contains("impala-interactive"));
	}

}
