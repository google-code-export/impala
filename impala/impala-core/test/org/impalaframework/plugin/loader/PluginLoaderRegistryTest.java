package org.impalaframework.plugin.loader;

import org.impalaframework.plugin.loader.ApplicationPluginLoader;
import org.impalaframework.plugin.loader.ParentPluginLoader;
import org.impalaframework.plugin.loader.PluginLoaderRegistry;
import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.plugin.spec.PluginTypes;
import org.impalaframework.plugin.spec.SimpleParentSpec;
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
