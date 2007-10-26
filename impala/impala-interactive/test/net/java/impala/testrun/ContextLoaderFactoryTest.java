package net.java.impala.testrun;

import junit.framework.TestCase;
import net.java.impala.location.PropertyClassLocationResolver;
import net.java.impala.spring.plugin.ApplicationPluginLoader;
import net.java.impala.spring.plugin.BeansetApplicationPluginLoader;
import net.java.impala.spring.plugin.ManualReloadingParentPluginLoader;
import net.java.impala.spring.plugin.PluginTypes;
import net.java.impala.spring.plugin.SystemParentPluginLoader;
import net.java.impala.spring.util.ApplicationContextLoader;
import net.java.impala.spring.util.RegistryBasedApplicationContextLoader;

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
