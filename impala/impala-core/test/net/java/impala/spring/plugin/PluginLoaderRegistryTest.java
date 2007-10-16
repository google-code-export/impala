package net.java.impala.spring.plugin;

import net.java.impala.location.ClassLocationResolver;
import net.java.impala.location.PropertyClassLocationResolver;
import junit.framework.TestCase;

/**
 * @author Phil Zoio
 */
public class PluginLoaderRegistryTest extends TestCase {

	public final void testGetPluginLoader() {
		PluginLoaderRegistry registry = new PluginLoaderRegistry();
		ClassLocationResolver resolver = new PropertyClassLocationResolver();
		registry.setPluginLoader(PluginTypes.ROOT, new ParentPluginLoader(resolver));
		registry.setPluginLoader(PluginTypes.APPLICATION, new ApplicationPluginLoader(resolver));
		
		PluginSpec p = new SimpleParentSpec(new String[]{"parent-context.xml"});
		assertTrue(registry.getPluginLoader(p.getType()) instanceof ParentPluginLoader);
	}

}
