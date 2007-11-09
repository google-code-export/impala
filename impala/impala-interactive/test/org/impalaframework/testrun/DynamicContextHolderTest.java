package org.impalaframework.testrun;

import java.io.File;
import java.util.Map;

import junit.framework.TestCase;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.file.monitor.FileMonitor;
import org.impalaframework.plugin.builder.PluginSpecBuilder;
import org.impalaframework.plugin.builder.SimplePluginSpecBuilder;
import org.impalaframework.plugin.loader.ApplicationContextLoader;
import org.impalaframework.plugin.loader.ApplicationPluginLoader;
import org.impalaframework.plugin.loader.ParentPluginLoader;
import org.impalaframework.plugin.loader.PluginLoaderRegistry;
import org.impalaframework.plugin.loader.RegistryBasedApplicationContextLoader;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.plugin.spec.PluginSpecProvider;
import org.impalaframework.plugin.spec.PluginTypes;
import org.impalaframework.plugin.spec.SimplePluginSpec;
import org.impalaframework.resolver.ClassLocationResolver;
import org.impalaframework.resolver.PropertyClassLocationResolver;
import org.impalaframework.spring.DefaultSpringContextHolder;
import org.impalaframework.testrun.DynamicContextHolder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public class DynamicContextHolderTest extends TestCase {

	private static final String plugin1 = "impala-sample-dynamic-plugin1";

	private static final String plugin2 = "impala-sample-dynamic-plugin2";

	private static final String plugin3 = "impala-sample-dynamic-plugin3";

	public void setUp() {
		System.setProperty("impala.parent.project", "impala");
	}

	public void tearDown() {
		System.clearProperty("impala.parent.project");
	}

	public void testInit() {

		PluginLoaderRegistry registry = new PluginLoaderRegistry();
		ClassLocationResolver resolver = new PropertyClassLocationResolver();
		registry.setPluginLoader(PluginTypes.ROOT, new ParentPluginLoader(resolver));
		registry.setPluginLoader(PluginTypes.APPLICATION, new ApplicationPluginLoader(resolver));
		ApplicationContextLoader loader = new RegistryBasedApplicationContextLoader(registry);
		TestPluginContextHolder holder = new TestPluginContextHolder(loader);

		DynamicContextHolder.setPluginContextHolder(holder);
		final Test1 test1 = new Test1();
		DynamicContextHolder.init(test1);
		assertSame(test1.getPluginSpec(), holder.getPluginSpec());

		assertTrue(holder.hasPlugin(plugin1));
		assertTrue(holder.hasParentContext());
		final ApplicationContext context1 = holder.getContext();
		final ConfigurableApplicationContext p11 = holder.getPlugins().get(plugin1);
		assertNotNull(p11);
		assertNull(holder.getPlugins().get(plugin2));
		assertNull(holder.getPlugins().get(plugin3));

		FileMonitor f1 = (FileMonitor) context1.getBean("bean1");
		FileMonitor f2 = (FileMonitor) context1.getBean("bean2");
		FileMonitor f3 = (FileMonitor) context1.getBean("bean3");

		f1.lastModified((File) null);
		noService(f2);
		noService(f3);

		final Test2 test2 = new Test2();
		DynamicContextHolder.init(test2);
		assertFalse(test2.getPluginSpec() == holder.getPluginSpec());

		assertTrue(holder.hasPlugin(plugin1));
		assertTrue(holder.hasPlugin(plugin2));
		assertTrue(holder.hasParentContext());
		final ApplicationContext context2 = holder.getContext();
		final ConfigurableApplicationContext p12 = holder.getPlugins().get(plugin1);
		assertNotNull(p12);
		assertSame(p11, p12);
		final ConfigurableApplicationContext p22 = holder.getPlugins().get(plugin2);
		assertNotNull(p22);
		assertNull(holder.getPlugins().get(plugin3));

		f1 = (FileMonitor) context2.getBean("bean1");
		f2 = (FileMonitor) context2.getBean("bean2");
		f3 = (FileMonitor) context2.getBean("bean3");

		f1.lastModified((File) null);
		f2.lastModified((File) null);
		noService(f3);

		// context still same
		assertSame(context1, context2);
		assertTrue(holder.hasPlugin(plugin1));
		assertTrue(holder.hasPlugin(plugin2));

		// now load plugin 3 as well
		final Test3 test3 = new Test3();
		DynamicContextHolder.init(test3);
		assertFalse(test3.getPluginSpec() == holder.getPluginSpec());

		final ApplicationContext context3 = holder.getContext();
		final ConfigurableApplicationContext p13 = holder.getPlugins().get(plugin1);
		assertSame(p11, p13);
		final ConfigurableApplicationContext p23 = holder.getPlugins().get(plugin2);
		assertSame(p22, p23);
		final ConfigurableApplicationContext p33 = holder.getPlugins().get(plugin3);
		assertNotNull(p33);

		f1 = (FileMonitor) context3.getBean("bean1");
		f2 = (FileMonitor) context3.getBean("bean2");
		f3 = (FileMonitor) context3.getBean("bean3");

		// context still same
		assertSame(context1, context3);

		f3.lastModified((File) null);
		assertTrue(holder.hasPlugin(plugin1));
		assertTrue(holder.hasPlugin(plugin2));
		assertTrue(holder.hasPlugin(plugin3));

		// show that this will return false
		assertFalse(DynamicContextHolder.reload("unknown"));

		// now reload plugin1
		assertTrue(DynamicContextHolder.reload(plugin1));
		assertTrue(holder.hasPlugin(plugin1));

		final ConfigurableApplicationContext p13reloaded = holder.getPlugins().get(plugin1);
		assertNotSame(p13reloaded, p13);
		FileMonitor f1reloaded = (FileMonitor) context3.getBean("bean1");

		assertEquals(f1.lastModified((File) null), f1reloaded.lastModified((File) null));
		f1reloaded.lastModified((File) null);
		assertSame(f1reloaded, f1);

		// now reload plugin2, which will also reload plugin3
		assertTrue(DynamicContextHolder.reload(plugin2));
		assertTrue(holder.hasPlugin(plugin2));

		final ConfigurableApplicationContext p23reloaded = holder.getPlugins().get(plugin2);
		assertNotSame(p23reloaded, p23);

		final ConfigurableApplicationContext p33reloaded = holder.getPlugins().get(plugin3);
		assertNotSame(p33reloaded, p33);

		FileMonitor f3reloaded = (FileMonitor) context3.getBean("bean3");

		assertEquals(f3.lastModified((File) null), f3reloaded.lastModified((File) null));
		f3reloaded.lastModified((File) null);
		assertSame(f3reloaded, f3);

		// show that this will return null
		assertNull(DynamicContextHolder.reloadLike("unknown"));

		// now test reloadLike
		assertEquals(plugin2, DynamicContextHolder.reloadLike("plugin2"));
		f3reloaded = (FileMonitor) context3.getBean("bean3");
		f3reloaded.lastModified((File) null);

		// now remove plugin2 (and by implication, child plugin3)
		assertFalse(DynamicContextHolder.remove("unknown"));
		assertTrue(DynamicContextHolder.remove(plugin2));
		assertFalse(holder.hasPlugin(plugin2));
		// check that the child is gone too
		assertFalse(holder.hasPlugin(plugin3));

		final PluginSpec test3ParentSpec = holder.getPluginSpec();
		assertTrue(test3ParentSpec.hasPlugin(plugin1));
		assertFalse(test3ParentSpec.hasPlugin(plugin2));

		f3reloaded = (FileMonitor) context3.getBean("bean3");
		noService(f3reloaded);
	}

	private void noService(FileMonitor f2) {
		try {
			f2.lastModified((File) null);
			fail();
		}
		catch (NoServiceException e) {
		}
	}

	class Test1 implements PluginSpecProvider {
		PluginSpecBuilder spec = new SimplePluginSpecBuilder("parentTestContext.xml", new String[] { plugin1 });

		public ParentSpec getPluginSpec() {
			return spec.getParentSpec();
		}

	}

	class Test2 implements PluginSpecProvider {
		PluginSpecBuilder spec = new SimplePluginSpecBuilder("parentTestContext.xml", new String[] { plugin1, plugin2 });

		public ParentSpec getPluginSpec() {
			return spec.getParentSpec();
		}

	}

	class Test3 implements PluginSpecProvider {
		PluginSpecBuilder spec = new SimplePluginSpecBuilder("parentTestContext.xml", new String[] { plugin1, plugin2 });

		public Test3() {

			PluginSpec p2 = spec.getParentSpec().getPlugin(plugin2);
			new SimplePluginSpec(p2, plugin3);
		}

		public ParentSpec getPluginSpec() {
			return spec.getParentSpec();
		}
	}

	class TestPluginContextHolder extends DefaultSpringContextHolder {

		public TestPluginContextHolder(ApplicationContextLoader contextLoader) {
			super(contextLoader);
		}

		@Override
		public Map<String, ConfigurableApplicationContext> getPlugins() {
			return super.getPlugins();
		}

		@Override
		public ParentSpec getPluginSpec() {
			return super.getPluginSpec();
		}
	};
}
