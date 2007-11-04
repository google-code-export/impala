package org.impalaframework.spring.plugin;

import org.impalaframework.location.ClassLocationResolver;
import org.impalaframework.location.PropertyClassLocationResolver;
import org.impalaframework.spring.plugin.ApplicationPluginLoader;
import org.impalaframework.spring.plugin.ParentPluginLoader;
import org.impalaframework.spring.plugin.PluginLoaderRegistry;
import org.impalaframework.spring.plugin.PluginSpec;
import org.impalaframework.spring.plugin.PluginTypes;
import org.impalaframework.spring.plugin.SimpleParentSpec;

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
