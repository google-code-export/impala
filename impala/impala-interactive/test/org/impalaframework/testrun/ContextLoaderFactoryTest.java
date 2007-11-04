package org.impalaframework.testrun;

import org.impalaframework.location.PropertyClassLocationResolver;
import org.impalaframework.spring.plugin.ApplicationPluginLoader;
import org.impalaframework.spring.plugin.BeansetApplicationPluginLoader;
import org.impalaframework.spring.plugin.ManualReloadingParentPluginLoader;
import org.impalaframework.spring.plugin.PluginTypes;
import org.impalaframework.spring.plugin.SystemParentPluginLoader;
import org.impalaframework.spring.util.ApplicationContextLoader;
import org.impalaframework.spring.util.RegistryBasedApplicationContextLoader;
import org.impalaframework.testrun.ContextLoaderFactory;

import junit.framework.TestCase;

public class ContextLoaderFactoryTest extends TestCase {

	public final void testNewContextLoader() {
		ContextLoaderFactory factory = new ContextLoaderFactory();
		RegistryBasedApplicationContextLoader r = newLoader(factory, false, false);
		assertNull(r.getPluginMonitor());
		assertEquals(SystemParentPluginLoader.class, r.getRegistry().getPluginLoader(PluginTypes.ROOT).getClass());

		r = newLoader(factory, true, false);
		assertEquals(SystemParentPluginLoader.class, r.getRegistry().getPluginLoader(PluginTypes.ROOT).getClass());
		assertEquals(ApplicationPluginLoader.class, r.getRegistry().getPluginLoader(PluginTypes.APPLICATION).getClass());
		assertEquals(BeansetApplicationPluginLoader.class, r.getRegistry().getPluginLoader(PluginTypes.APPLICATION_WITH_BEANSETS).getClass());
		assertNotNull(r.getPluginMonitor());
		r.setPluginMonitor(null);

		r = newLoader(factory, false, true);
		assertEquals(ManualReloadingParentPluginLoader.class, r.getRegistry().getPluginLoader(PluginTypes.ROOT).getClass());
		assertNull(r.getPluginMonitor());

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
