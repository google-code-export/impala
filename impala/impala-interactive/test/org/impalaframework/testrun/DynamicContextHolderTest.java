package org.impalaframework.testrun;

import java.io.File;

import junit.framework.TestCase;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.file.monitor.FileMonitor;
import org.impalaframework.plugin.builder.PluginSpecBuilder;
import org.impalaframework.plugin.builder.SimplePluginSpecBuilder;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.plugin.spec.PluginSpecProvider;
import org.impalaframework.plugin.spec.SimplePluginSpec;
import org.impalaframework.plugin.spec.transition.DefaultPluginStateManager;
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
		try {
			DynamicContextHolder.remove(ParentSpec.NAME);
		}
		catch (Exception e) {
		}
	}

	public void testNoInit() {
		try {
			DynamicContextHolder.get();
			fail();
		}
		catch (NoServiceException e) {
			assertEquals("No root application has been loaded", e.getMessage());
		}
	}

	public void testInit() {

		DefaultPluginStateManager holder = DynamicContextHolder.getPluginStateManager();

		final Test1 test1 = new Test1();
		DynamicContextHolder.init(test1);
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
		
		FileMonitor pluginBean = DynamicContextHolder.getPluginBean(test1, plugin1, "bean1", FileMonitor.class);
		assertEquals("classes.FileMonitorBean1", pluginBean.getClass().getName());
		
		try {
			DynamicContextHolder.getPluginBean(test1, "unknown-plugin", "bean1", FileMonitor.class);
			fail();
		}
		catch (NoServiceException e) {
			assertEquals("No application context could be found for plugin unknown-plugin", e.getMessage());
		}

		service(f1);
		noService(f2);
		noService(f3);

		final Test2 test2 = new Test2();
		DynamicContextHolder.init(test2);
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

		service(f1);
		service(f2);
		noService(f3);

		// context still same
		assertSame(context1, context2);
		assertTrue(holder.hasPlugin(plugin1));
		assertTrue(holder.hasPlugin(plugin2));

		// now load plugin 3 as well
		final Test3 test3 = new Test3();
		DynamicContextHolder.init(test3);
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

		FileMonitor f3PluginBean = DynamicContextHolder.getPluginBean(test3, plugin1, "bean3", FileMonitor.class);
		assertSame(f3, f3PluginBean);

		// context still same
		assertSame(context1, context3);

		service(f3);
		assertTrue(holder.hasPlugin(plugin1));
		assertTrue(holder.hasPlugin(plugin2));
		assertTrue(holder.hasPlugin(plugin3));

		// show that this will return false
		assertFalse(DynamicContextHolder.reload(test3, "unknown"));

		// now reload plugin1
		assertTrue(DynamicContextHolder.reload(test3, plugin1));
		assertTrue(holder.hasPlugin(plugin1));

		final ConfigurableApplicationContext p13reloaded = holder.getPlugins().get(plugin1);
		assertNotSame(p13reloaded, p13);
		FileMonitor f1reloaded = (FileMonitor) context3.getBean("bean1");

		assertEquals(f1.lastModified((File) null), f1reloaded.lastModified((File) null));
		service(f1reloaded);
		assertSame(f1reloaded, f1);

		// now reload plugin2, which will also reload plugin3
		assertTrue(DynamicContextHolder.reload(test3, plugin2));
		assertTrue(holder.hasPlugin(plugin2));

		final ConfigurableApplicationContext p23reloaded = holder.getPlugins().get(plugin2);
		assertNotSame(p23reloaded, p23);

		final ConfigurableApplicationContext p33reloaded = holder.getPlugins().get(plugin3);
		assertNotSame(p33reloaded, p33);

		FileMonitor f3reloaded = (FileMonitor) context3.getBean("bean3");

		assertEquals(f3.lastModified((File) null), f3reloaded.lastModified((File) null));
		service(f3reloaded);
		assertSame(f3reloaded, f3);

		// show that this will return null
		assertNull(DynamicContextHolder.reloadLike(test3, "unknown"));

		// now test reloadLike
		assertEquals(plugin2, DynamicContextHolder.reloadLike(test3, "plugin2"));
		f3reloaded = (FileMonitor) context3.getBean("bean3");
		service(f3reloaded);

		// now remove plugin2 (and by implication, child plugin3)
		assertFalse(DynamicContextHolder.remove("unknown"));
		assertTrue(DynamicContextHolder.remove(plugin2));
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

	public void testAdd() {
		final Test1 test1 = new Test1();
		DynamicContextHolder.init(test1);

		final ApplicationContext context1 = DynamicContextHolder.get();
		FileMonitor f1 = (FileMonitor) context1.getBean("bean1");
		FileMonitor f2 = (FileMonitor) context1.getBean("bean2");

		service(f1);
		noService(f2);
		DynamicContextHolder.addPlugin(new SimplePluginSpec(plugin2));
		service(f1);
		service(f2);
	}

	public void testReloadParent() {
		final Test1 test1 = new Test1();
		DynamicContextHolder.init(test1);

		final ApplicationContext context1a = DynamicContextHolder.get();
		FileMonitor f1 = DynamicContextHolder.getBean(test1, "bean1", FileMonitor.class);
		service(f1);
		DynamicContextHolder.reloadParent();
		final ApplicationContext context1b = DynamicContextHolder.get();
		f1 = DynamicContextHolder.getBean(test1, "bean1", FileMonitor.class);
		service(f1);

		assertFalse(context1a == context1b);
	}

	public void testUnloadParent() {
		final Test1 test1 = new Test1();
		DynamicContextHolder.init(test1);
		DynamicContextHolder.unloadParent();
		try {
			DynamicContextHolder.get();
		}
		catch (NoServiceException e) {
		}
	}

	private void service(FileMonitor f) {
		f.lastModified((File) null);
	}

	private void noService(FileMonitor f) {
		try {
			service(f);
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
}
