package net.java.impala.spring.plugin;

import junit.framework.TestCase;
import net.java.impala.classloader.ParentClassLoader;
import net.java.impala.location.PropertyClassLocationResolver;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * @author Phil Zoio
 */
public class ParentPluginLoaderTest extends TestCase {
	
	private static final String plugin1 = "impala-sample-dynamic-plugin1";

	private static final String plugin2 = "impala-sample-dynamic-plugin2";
	
	private ParentPluginLoader pluginLoader;

	private ApplicationContextSet contextSet;

	private SpringContextSpec spec;
	
	public void setUp() {
		PropertyClassLocationResolver locationResolver = new PropertyClassLocationResolver();
		pluginLoader = new ParentPluginLoader(locationResolver);
		contextSet = new ApplicationContextSet();
		spec = new SimpleSpringContextSpec("parentTestContext.xml", new String[] { plugin1, plugin2 });
	}
	
	public final void testGetClassLoader() {
		final ClassLoader classLoader = pluginLoader.newClassLoader(contextSet, spec.getParentSpec(), null);
		assertTrue(classLoader instanceof ParentClassLoader);
		assertTrue(classLoader.getParent().getClass().equals(this.getClass().getClassLoader().getClass()));
	}

	public void testGetSpringLocations() {
		final ClassLoader classLoader = pluginLoader.newClassLoader(contextSet, spec.getParentSpec(), null);
		final Resource[] springConfigResources = pluginLoader.getSpringConfigResources(contextSet, spec.getParentSpec(), classLoader);
		
		assertEquals(1, springConfigResources.length);
		assertEquals(ClassPathResource.class, springConfigResources[0].getClass());
		assertTrue(springConfigResources[0].exists());
	
	}

}
