package net.java.impala.spring.plugin;

import org.springframework.context.support.GenericApplicationContext;

import net.java.impala.classloader.CustomClassLoader;
import net.java.impala.location.PropertyClassLocationResolver;
import junit.framework.TestCase;

public class ApplicationPluginLoaderTest extends TestCase {

	private static final String plugin1 = "impala-sample-dynamic-plugin1";

	private static final String plugin2 = "impala-sample-dynamic-plugin2";
	
	private static final String plugin3 = "impala-sample-dynamic-plugin3";

	private ApplicationPluginLoader pluginLoader;
	
	public void setUp() {
		PropertyClassLocationResolver locationResolver = new PropertyClassLocationResolver();
		pluginLoader = new ApplicationPluginLoader(locationResolver);
	}
	
	public final void testGetClassLoader() {

		ApplicationContextSet contextSet = new ApplicationContextSet();
		
		SpringContextSpec spec = new SimpleSpringContextSpec("parentTestContext.xml", new String[] { plugin1, plugin2 });
		PluginSpec p2 = spec.getParentSpec().getPlugin(plugin2);
		PluginSpec p3 = new SimplePluginSpec(p2, plugin3);
		
		ClassLoader classLoader2 = pluginLoader.newClassLoader(contextSet, p2);
		assertTrue(classLoader2 instanceof CustomClassLoader);
		assertTrue(classLoader2.getParent().getClass().equals(this.getClass().getClassLoader().getClass()));
		
		GenericApplicationContext parentContext = new GenericApplicationContext();
		parentContext.setClassLoader(classLoader2);
		contextSet.getPluginContext().put(plugin2, parentContext);

		ClassLoader classLoader3 = pluginLoader.newClassLoader(contextSet, p3);
		assertTrue(classLoader3 instanceof CustomClassLoader);
		assertSame(classLoader2, classLoader3.getParent());
		
		
	}

}
