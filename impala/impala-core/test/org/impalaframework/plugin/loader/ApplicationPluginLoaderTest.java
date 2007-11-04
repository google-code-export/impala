package org.impalaframework.plugin.loader;

import junit.framework.TestCase;

import org.impalaframework.classloader.ParentClassLoader;
import org.impalaframework.plugin.builder.PluginSpecBuilder;
import org.impalaframework.plugin.builder.SimplePluginSpecBuilder;
import org.impalaframework.plugin.loader.ApplicationPluginLoader;
import org.impalaframework.plugin.spec.ApplicationContextSet;
import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.plugin.spec.SimplePluginSpec;
import org.impalaframework.resolver.PropertyClassLocationResolver;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

/**
 * @author Phil Zoio
 */
public class ApplicationPluginLoaderTest extends TestCase {

	private static final String plugin1 = "impala-sample-dynamic-plugin1";

	private static final String plugin2 = "impala-sample-dynamic-plugin2";

	private static final String plugin3 = "impala-sample-dynamic-plugin3";

	private ApplicationPluginLoader pluginLoader;

	private PluginSpecBuilder spec;

	private PluginSpec p2;

	private PluginSpec p3;

	private ApplicationContextSet contextSet;

	public void setUp() {
		PropertyClassLocationResolver locationResolver = new PropertyClassLocationResolver();
		pluginLoader = new ApplicationPluginLoader(locationResolver);

		spec = new SimplePluginSpecBuilder("parentTestContext.xml", new String[] { plugin1, plugin2 });
		p2 = spec.getParentSpec().getPlugin(plugin2);
		p3 = new SimplePluginSpec(p2, plugin3);
		
		contextSet = new ApplicationContextSet();
	}

	public void testGetClassLoader() {

		ClassLoader classLoader2 = pluginLoader.newClassLoader(contextSet, p2, null);
		assertTrue(classLoader2 instanceof ParentClassLoader);
		assertTrue(classLoader2.getParent().getClass().equals(this.getClass().getClassLoader().getClass()));

		GenericApplicationContext parentContext = new GenericApplicationContext();
		parentContext.setClassLoader(classLoader2);
		contextSet.getPluginContext().put(plugin2, parentContext);

		ClassLoader classLoader3 = pluginLoader.newClassLoader(contextSet, p3, parentContext);
		assertTrue(classLoader3 instanceof ParentClassLoader);
		assertSame(classLoader2, classLoader3.getParent());

	}
	
	public final void testGetClassLocations() {
		final Resource[] classLocations = pluginLoader.getClassLocations(contextSet, p2);
		for (Resource resource : classLocations) {
			assertTrue(resource instanceof FileSystemResource);
			assertTrue(resource.exists());
		}
	}

	public void testGetSpringLocations() {
		final Resource[] springConfigResources = pluginLoader.getSpringConfigResources(contextSet, p2, ClassUtils.getDefaultClassLoader());
		assertEquals(1, springConfigResources.length);
		assertEquals(FileSystemResource.class, springConfigResources[0].getClass());
		assertTrue(springConfigResources[0].exists());
	}

}
