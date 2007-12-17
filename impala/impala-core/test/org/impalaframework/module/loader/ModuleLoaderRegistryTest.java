package org.impalaframework.module.loader;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.module.loader.ApplicationModuleLoader;
import org.impalaframework.module.loader.DelegatingContextLoader;
import org.impalaframework.module.loader.RootModuleLoader;
import org.impalaframework.module.loader.ModuleLoader;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.impalaframework.module.spec.ModuleDefinition;
import org.impalaframework.module.spec.ModuleTypes;
import org.impalaframework.module.spec.SimpleRootModuleDefinition;
import org.impalaframework.resolver.ClassLocationResolver;
import org.impalaframework.resolver.PropertyClassLocationResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Phil Zoio
 */
public class ModuleLoaderRegistryTest extends TestCase {

	private ModuleLoaderRegistry registry;
	
	@Override
	protected void setUp() throws Exception {
		registry = new ModuleLoaderRegistry();
	}
	public void testNoPluginLoader() {
		try {
			registry.getPluginLoader("unknowntype");
		}
		catch (NoServiceException e) {
			assertEquals("No org.impalaframework.module.loader.ModuleLoader instance available for plugin type unknowntype", e.getMessage());
		}
	}
	
	public void testGetPluginLoader() {
		ClassLocationResolver resolver = new PropertyClassLocationResolver();
		registry.setPluginLoader(ModuleTypes.ROOT, new RootModuleLoader(resolver));
		registry.setPluginLoader(ModuleTypes.APPLICATION, new ApplicationModuleLoader(resolver));

		ModuleDefinition p = new SimpleRootModuleDefinition(new String[] { "parent-context.xml" });
		assertTrue(registry.getPluginLoader(p.getType()) instanceof RootModuleLoader);

		DelegatingContextLoader delegatingLoader = new DelegatingContextLoader() {
			public ConfigurableApplicationContext loadApplicationContext(ApplicationContext parent,
					ModuleDefinition plugin) {
				return null;
			}
		};
		registry.setDelegatingLoader("sometype", delegatingLoader);
		assertSame(delegatingLoader, registry.getDelegatingLoader("sometype"));
	}
	
	public void testSetPluginLoaders() {
		ClassLocationResolver resolver = new PropertyClassLocationResolver();
		Map<String,ModuleLoader> moduleLoaders = new HashMap<String, ModuleLoader>();
		moduleLoaders.put(ModuleTypes.ROOT, new RootModuleLoader(resolver));
		moduleLoaders.put(ModuleTypes.APPLICATION, new ApplicationModuleLoader(resolver));
		registry.setPluginLoaders(moduleLoaders);
		
		assertEquals(2, moduleLoaders.size());

		Map<String,DelegatingContextLoader> delegatingLoaders = new HashMap<String, DelegatingContextLoader>();
		DelegatingContextLoader delegatingLoader = new DelegatingContextLoader() {
			public ConfigurableApplicationContext loadApplicationContext(ApplicationContext parent,
					ModuleDefinition plugin) {
				return null;
			}
		};
		delegatingLoaders.put("key", delegatingLoader);
		registry.setDelegatingLoaders(delegatingLoaders);

		assertEquals(1, delegatingLoaders.size());		
	}
}
