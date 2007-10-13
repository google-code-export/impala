package net.java.impala.spring.plugin;

import junit.framework.TestCase;
import net.java.impala.classloader.ParentClassLoader;
import net.java.impala.location.PropertyClassLocationResolver;

public class ParentPluginLoaderTest extends TestCase {
	
	private static final String plugin1 = "impala-sample-dynamic-plugin1";

	private static final String plugin2 = "impala-sample-dynamic-plugin2";
	
	private ParentPluginLoader pluginLoader;
	
	public void setUp() {
		PropertyClassLocationResolver locationResolver = new PropertyClassLocationResolver();
		pluginLoader = new ParentPluginLoader(locationResolver);
	}
	
	public final void testGetClassLoader() {

		ApplicationContextSet contextSet = new ApplicationContextSet();
		
		SpringContextSpec spec = new SimpleSpringContextSpec("parentTestContext.xml", new String[] { plugin1, plugin2 });
		
		final ClassLoader classLoader = pluginLoader.newClassLoader(contextSet, spec.getParentSpec());
		assertTrue(classLoader instanceof ParentClassLoader);
		assertTrue(classLoader.getParent().getClass().equals(this.getClass().getClassLoader().getClass()));
	}

}
