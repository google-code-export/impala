package org.impalaframework.testrun;

import java.io.File;
import java.util.Map;

import junit.framework.TestCase;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.file.monitor.FileMonitor;
import org.impalaframework.plugin.builder.PluginSpecBuilder;
import org.impalaframework.plugin.builder.SimplePluginSpecBuilder;
import org.impalaframework.plugin.loader.ApplicationContextLoader;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.plugin.spec.PluginSpecProvider;
import org.impalaframework.plugin.spec.SimplePluginSpec;
import org.impalaframework.plugin.spec.transition.PluginStateManager;
import org.impalaframework.spring.DefaultSpringContextHolder;
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

		PluginStateManager holder = DynamicStateHolder.getPluginStateManager();
		
		final Test1 test1 = new Test1();
		DynamicStateHolder.init(test1);
		assertSame(test1.getPluginSpec(), holder.getParentSpec());

		assertTrue(holder.hasPlugin(plugin1));
		assertTrue(holder.hasParentContext());
		final ApplicationContext context1 = holder.getParentContext();
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
		DynamicStateHolder.init(test2);
		assertTrue(test2.getPluginSpec() == holder.getParentSpec());

		assertTrue(holder.hasPlugin(plugin1));
		assertTrue(holder.hasPlugin(plugin2));
		assertTrue(holder.hasParentContext());
		final ApplicationContext context2 = holder.getParentContext();
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
		DynamicStateHolder.init(test3);
		assertTrue(test3.getPluginSpec() == holder.getParentSpec());

		final ApplicationContext context3 = holder.getParentContext();
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
		assertFalse(DynamicStateHolder.reload(test3, "unknown"));

		// now reload plugin1
		assertTrue(DynamicStateHolder.reload(test3, plugin1));
		assertTrue(holder.hasPlugin(plugin1));

		final ConfigurableApplicationContext p13reloaded = holder.getPlugins().get(plugin1);
		assertNotSame(p13reloaded, p13);
		FileMonitor f1reloaded = (FileMonitor) context3.getBean("bean1");

		assertEquals(f1.lastModified((File) null), f1reloaded.lastModified((File) null));
		f1reloaded.lastModified((File) null);
		assertSame(f1reloaded, f1);

		// now reload plugin2, which will also reload plugin3
		assertTrue(DynamicStateHolder.reload(test3, plugin2));
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
		assertNull(DynamicStateHolder.reloadLike(test3, "unknown"));

		// now test reloadLike
		assertEquals(plugin2, DynamicStateHolder.reloadLike(test3, "plugin2"));
		f3reloaded = (FileMonitor) context3.getBean("bean3");
		f3reloaded.lastModified((File) null);

		// now remove plugin2 (and by implication, child plugin3)
		assertFalse(DynamicStateHolder.remove("unknown"));
		assertTrue(DynamicStateHolder.remove(plugin2));
		assertFalse(holder.hasPlugin(plugin2));
		// check that the child is gone too
		assertFalse(holder.hasPlugin(plugin3));

		final PluginSpec test3ParentSpec = holder.getParentSpec();
		assertTrue(test3ParentSpec.hasPlugin(plugin1));
		assertFalse(test3ParentSpec.hasPlugin(plugin2));

		f3reloaded = (FileMonitor) context3.getBean("bean3");
		FileMonitor f2reloaded = (FileMonitor) context3.getBean("bean2");
		noService(f3reloaded);
		noService(f2reloaded);
		
	}

	private void noService(FileMonitor f) {
		try {
			f.lastModified((File) null);
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
