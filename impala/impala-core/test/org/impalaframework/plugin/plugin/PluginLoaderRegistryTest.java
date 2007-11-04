package org.impalaframework.plugin.plugin;

import org.impalaframework.plugin.plugin.ApplicationPluginLoader;
import org.impalaframework.plugin.plugin.ParentPluginLoader;
import org.impalaframework.plugin.plugin.PluginLoaderRegistry;
import org.impalaframework.plugin.plugin.PluginSpec;
import org.impalaframework.plugin.plugin.PluginTypes;
import org.impalaframework.plugin.plugin.SimpleParentSpec;
import org.impalaframework.resolver.ClassLocationResolver;
import org.impalaframework.resolver.PropertyClassLocationResolver;

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
