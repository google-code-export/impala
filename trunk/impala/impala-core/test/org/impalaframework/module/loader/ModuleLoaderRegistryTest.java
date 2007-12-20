package org.impalaframework.module.loader;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.module.loader.ApplicationModuleLoader;
import org.impalaframework.module.loader.DelegatingContextLoader;
import org.impalaframework.module.loader.RootModuleLoader;
import org.impalaframework.module.loader.ModuleLoader;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.resolver.PropertyModuleLocationResolver;
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
			registry.getModuleLoader("unknowntype");
		}
		catch (NoServiceException e) {
			assertEquals("No org.impalaframework.module.loader.ModuleLoader instance available for plugin type unknowntype", e.getMessage());
		}
	}
	
	public void testGetPluginLoader() {
		ModuleLocationResolver resolver = new PropertyModuleLocationResolver();
		registry.setModuleLoader(ModuleTypes.ROOT, new RootModuleLoader(resolver));
		registry.setModuleLoader(ModuleTypes.APPLICATION, new ApplicationModuleLoader(resolver));

		ModuleDefinition p = new SimpleRootModuleDefinition(new String[] { "parent-context.xml" });
		assertTrue(registry.getModuleLoader(p.getType()) instanceof RootModuleLoader);

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
		ModuleLocationResolver resolver = new PropertyModuleLocationResolver();
		Map<String,ModuleLoader> moduleLoaders = new HashMap<String, ModuleLoader>();
		moduleLoaders.put(ModuleTypes.ROOT, new RootModuleLoader(resolver));
		moduleLoaders.put(ModuleTypes.APPLICATION, new ApplicationModuleLoader(resolver));
		registry.setModuleLoaders(moduleLoaders);
		
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
