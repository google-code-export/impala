package org.impalaframework.module.loader;

import junit.framework.TestCase;

import org.impalaframework.classloader.ModuleClassLoader;
import org.impalaframework.module.builder.SimpleModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.loader.ApplicationModuleLoader;
import org.impalaframework.resolver.PropertyModuleLocationResolver;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

/**
 * @author Phil Zoio
 */
public class ApplicationModuleLoaderTest extends TestCase {

	private static final String plugin1 = "impala-sample-dynamic-plugin1";

	private static final String plugin2 = "impala-sample-dynamic-plugin2";

	private static final String plugin3 = "impala-sample-dynamic-plugin3";

	private ApplicationModuleLoader moduleLoader;

	private ModuleDefinitionSource source;

	private ModuleDefinition p2;

	private ModuleDefinition p3;

	public void setUp() {
		PropertyModuleLocationResolver locationResolver = new PropertyModuleLocationResolver();
		moduleLoader = new ApplicationModuleLoader(locationResolver);

		source = new SimpleModuleDefinitionSource("parentTestContext.xml", new String[] { plugin1, plugin2 });
		p2 = source.getModuleDefinition().getModule(plugin2);
		p3 = new SimpleModuleDefinition(p2, plugin3);
	}

	public void testGetClassLoader() {

		ClassLoader classLoader2 = moduleLoader.newClassLoader(p2, null);
		assertTrue(classLoader2 instanceof ModuleClassLoader);
		assertTrue(classLoader2.getParent().getClass().equals(this.getClass().getClassLoader().getClass()));

		GenericApplicationContext parentContext = new GenericApplicationContext();
		parentContext.setClassLoader(classLoader2);

		ClassLoader classLoader3 = moduleLoader.newClassLoader(p3, parentContext);
		assertTrue(classLoader3 instanceof ModuleClassLoader);
		assertSame(classLoader2, classLoader3.getParent());
	}
	
	public final void testGetClassLocations() {
		final Resource[] classLocations = moduleLoader.getClassLocations(p2);
		for (Resource resource : classLocations) {
			assertTrue(resource instanceof FileSystemResource);
			assertTrue(resource.exists());
		}
	}

	public void testGetSpringLocations() {
		final Resource[] springConfigResources = moduleLoader.getSpringConfigResources(p2, ClassUtils.getDefaultClassLoader());
		assertEquals(1, springConfigResources.length);
		assertEquals(ClassPathResource.class, springConfigResources[0].getClass());
		assertEquals("class path resource [impala-sample-dynamic-plugin2-context.xml]", springConfigResources[0].getDescription());
	}

}
