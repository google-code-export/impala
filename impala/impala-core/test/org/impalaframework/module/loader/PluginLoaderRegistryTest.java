package org.impalaframework.module.loader;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.module.loader.ApplicationPluginLoader;
import org.impalaframework.module.loader.DelegatingContextLoader;
import org.impalaframework.module.loader.ParentPluginLoader;
import org.impalaframework.module.loader.PluginLoader;
import org.impalaframework.module.loader.PluginLoaderRegistry;
import org.impalaframework.module.spec.PluginSpec;
import org.impalaframework.module.spec.PluginTypes;
import org.impalaframework.module.spec.SimpleParentSpec;
import org.impalaframework.resolver.ClassLocationResolver;
import org.impalaframework.resolver.PropertyClassLocationResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Phil Zoio
 */
public class PluginLoaderRegistryTest extends TestCase {

	private PluginLoaderRegistry registry;
	
	@Override
	protected void setUp() throws Exception {
		registry = new PluginLoaderRegistry();
	}
	public void testNoPluginLoader() {
		try {
			registry.getPluginLoader("unknowntype");
		}
		catch (NoServiceException e) {
			assertEquals("No org.impalaframework.module.loader.PluginLoader instance available for plugin type unknowntype", e.getMessage());
		}
	}
	
	public void testGetPluginLoader() {
		ClassLocationResolver resolver = new PropertyClassLocationResolver();
		registry.setPluginLoader(PluginTypes.ROOT, new ParentPluginLoader(resolver));
		registry.setPluginLoader(PluginTypes.APPLICATION, new ApplicationPluginLoader(resolver));

		PluginSpec p = new SimpleParentSpec(new String[] { "parent-context.xml" });
		assertTrue(registry.getPluginLoader(p.getType()) instanceof ParentPluginLoader);

		DelegatingContextLoader delegatingLoader = new DelegatingContextLoader() {
			public ConfigurableApplicationContext loadApplicationContext(ApplicationContext parent,
					PluginSpec plugin) {
				return null;
			}
		};
		registry.setDelegatingLoader("sometype", delegatingLoader);
		assertSame(delegatingLoader, registry.getDelegatingLoader("sometype"));
	}
	
	public void testSetPluginLoaders() {
		ClassLocationResolver resolver = new PropertyClassLocationResolver();
		Map<String,PluginLoader> pluginLoaders = new HashMap<String, PluginLoader>();
		pluginLoaders.put(PluginTypes.ROOT, new ParentPluginLoader(resolver));
		pluginLoaders.put(PluginTypes.APPLICATION, new ApplicationPluginLoader(resolver));
		registry.setPluginLoaders(pluginLoaders);
		
		assertEquals(2, pluginLoaders.size());

		Map<String,DelegatingContextLoader> delegatingLoaders = new HashMap<String, DelegatingContextLoader>();
		DelegatingContextLoader delegatingLoader = new DelegatingContextLoader() {
			public ConfigurableApplicationContext loadApplicationContext(ApplicationContext parent,
					PluginSpec plugin) {
				return null;
			}
		};
		delegatingLoaders.put("key", delegatingLoader);
		registry.setDelegatingLoaders(delegatingLoaders);

		assertEquals(1, delegatingLoaders.size());		
	}
}
