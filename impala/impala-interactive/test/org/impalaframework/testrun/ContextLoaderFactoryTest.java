package org.impalaframework.testrun;

import org.impalaframework.plugin.loader.ApplicationContextLoader;
import org.impalaframework.plugin.loader.ApplicationPluginLoader;
import org.impalaframework.plugin.loader.BeansetApplicationPluginLoader;
import org.impalaframework.plugin.loader.ManualReloadingParentPluginLoader;
import org.impalaframework.plugin.loader.PluginLoaderRegistry;
import org.impalaframework.plugin.loader.RegistryBasedApplicationContextLoader;
import org.impalaframework.plugin.loader.SystemParentPluginLoader;
import org.impalaframework.plugin.spec.PluginTypes;
import org.impalaframework.resolver.PropertyClassLocationResolver;
import org.impalaframework.testrun.ContextLoaderFactory;

import junit.framework.TestCase;

public class ContextLoaderFactoryTest extends TestCase {

	public final void testManualReloading() {
		ContextLoaderFactory factory = new ContextLoaderFactory();
		RegistryBasedApplicationContextLoader r = newLoader(factory, false, true);
		assertEquals(ManualReloadingParentPluginLoader.class, r.getRegistry().getPluginLoader(PluginTypes.ROOT).getClass());
		assertNull(r.getPluginMonitor());
	}
	
	public final void testNoReloadableParent() {
		ContextLoaderFactory factory = new ContextLoaderFactory();
		RegistryBasedApplicationContextLoader r = newLoader(factory, false, false);
		assertNull(r.getPluginMonitor());
		assertEquals(SystemParentPluginLoader.class, r.getRegistry().getPluginLoader(PluginTypes.ROOT).getClass());
	}
	
	public final void testNewContextLoader() {
		ContextLoaderFactory factory = new ContextLoaderFactory();
		RegistryBasedApplicationContextLoader r = newLoader(factory, true, false);
		PluginLoaderRegistry registry = r.getRegistry();
		assertEquals(SystemParentPluginLoader.class, registry.getPluginLoader(PluginTypes.ROOT).getClass());
		assertEquals(ApplicationPluginLoader.class, registry.getPluginLoader(PluginTypes.APPLICATION).getClass());
		assertEquals(BeansetApplicationPluginLoader.class, registry.getPluginLoader(PluginTypes.APPLICATION_WITH_BEANSETS).getClass());
		assertNotNull(r.getPluginMonitor());
		r.setPluginMonitor(null);
	}
	
	public final void testGetPluginRegistry() {
		ContextLoaderFactory factory = new ContextLoaderFactory();
		RegistryBasedApplicationContextLoader r = newLoader(factory, true, false);
		PluginLoaderRegistry registry = factory.getPluginLoaderRegistry(new PropertyClassLocationResolver(), false);
		assertEquals(SystemParentPluginLoader.class, registry.getPluginLoader(PluginTypes.ROOT).getClass());
		assertEquals(ApplicationPluginLoader.class, registry.getPluginLoader(PluginTypes.APPLICATION).getClass());
		assertEquals(BeansetApplicationPluginLoader.class, registry.getPluginLoader(PluginTypes.APPLICATION_WITH_BEANSETS).getClass());
		assertNotNull(r.getPluginMonitor());
		r.setPluginMonitor(null);
	}
	
	private RegistryBasedApplicationContextLoader newLoader(ContextLoaderFactory factory, final boolean autoReload,
			final boolean reloadableParent) {
		ApplicationContextLoader loader = factory.newContextLoader(new PropertyClassLocationResolver(), autoReload,
				reloadableParent);

		assertTrue(loader instanceof RegistryBasedApplicationContextLoader);
		RegistryBasedApplicationContextLoader r = (RegistryBasedApplicationContextLoader) loader;
		return r;
	}

}
