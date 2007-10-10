package net.java.impala.testrun;

import java.util.Map;

import junit.framework.TestCase;
import net.java.impala.classloader.ImpalaTestContextResourceHelper;
import net.java.impala.classloader.TestContextResourceHelper;
import net.java.impala.location.PropertyClassLocationResolver;
import net.java.impala.monitor.FileMonitor;
import net.java.impala.spring.plugin.NoServiceException;
import net.java.impala.spring.plugin.PluginSpec;
import net.java.impala.spring.plugin.SimplePluginSpec;
import net.java.impala.spring.plugin.SimpleSpringContextSpec;
import net.java.impala.spring.plugin.SpringContextSpec;
import net.java.impala.spring.util.ApplicationContextLoader;
import net.java.impala.testrun.spring.TestApplicationContextLoader;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public class DynamicContextHolderTest extends TestCase {

	private static final String plugin1 = "impala-sample-dynamic-plugin1";

	private static final String plugin2 = "impala-sample-dynamic-plugin2";

	private static final String plugin3 = "impala-sample-dynamic-plugin3";

	public void testInit() {

		PropertyClassLocationResolver locationResolver = new PropertyClassLocationResolver();
		TestContextResourceHelper resourceHelper = new ImpalaTestContextResourceHelper(locationResolver);
		TestApplicationContextLoader loader = new TestApplicationContextLoader(resourceHelper);
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

		f1.lastModified(null);
		noService(f2);
		noService(f3);

		final Test2 test2 = new Test2();
		DynamicContextHolder.init(test2);
		assertSame(test2.getPluginSpec(), holder.getPluginSpec());
		
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

		f1.lastModified(null);
		f2.lastModified(null);
		noService(f3);

		// context still same
		assertSame(context1, context2);
		assertTrue(holder.hasPlugin(plugin1));
		assertTrue(holder.hasPlugin(plugin2));

		// now load plugin 3 as well
		final Test3 test3 = new Test3();
		DynamicContextHolder.init(test3);
		assertSame(test3.getPluginSpec(), holder.getPluginSpec());
		
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

		f3.lastModified(null);
		assertTrue(holder.hasPlugin(plugin1));
		assertTrue(holder.hasPlugin(plugin2));
		assertTrue(holder.hasPlugin(plugin3));

	}

	private void noService(FileMonitor f2) {
		try {
			f2.lastModified(null);
			fail();
		}
		catch (NoServiceException e) {
		}
	}

	class Test1 implements SpringContextSpecAware {
		SpringContextSpec spec = new SimpleSpringContextSpec("parentTestContext.xml", new String[] { plugin1 });

		public SpringContextSpec getPluginSpec() {
			return spec;
		}

	}

	class Test2 implements SpringContextSpecAware {
		SpringContextSpec spec = new SimpleSpringContextSpec("parentTestContext.xml", new String[] { plugin1, plugin2 });

		public SpringContextSpec getPluginSpec() {
			return spec;
		}

	}

	class Test3 implements SpringContextSpecAware {
		SpringContextSpec spec = new SimpleSpringContextSpec("parentTestContext.xml", new String[] { plugin1, plugin2 });

		public Test3() {

			PluginSpec p2 = spec.getParentSpec().getPlugin(plugin2);
			new SimplePluginSpec(p2, plugin3);
		}

		public SpringContextSpec getPluginSpec() {
			return spec;
		}
	}

	class TestPluginContextHolder extends PluginContextHolder {

		public TestPluginContextHolder(ApplicationContextLoader contextLoader) {
			super(contextLoader);
		}

		@Override
		public Map<String, ConfigurableApplicationContext> getPlugins() {
			return super.getPlugins();
		}

		@Override
		public SpringContextSpec getPluginSpec() {
			return super.getPluginSpec();
		}
	};
}
