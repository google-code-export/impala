package org.impalaframework.plugin.spec.transition;

import java.io.File;

import junit.framework.TestCase;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.file.monitor.FileMonitor;
import org.impalaframework.plugin.builder.PluginSpecBuilder;
import org.impalaframework.plugin.builder.SimplePluginSpecBuilder;
import org.impalaframework.plugin.loader.ApplicationPluginLoader;
import org.impalaframework.plugin.loader.ParentPluginLoader;
import org.impalaframework.plugin.loader.PluginLoaderRegistry;
import org.impalaframework.plugin.loader.RegistryBasedApplicationContextLoader;
import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.plugin.spec.PluginSpecProvider;
import org.impalaframework.plugin.spec.PluginTypes;
import org.impalaframework.plugin.spec.SimplePluginSpec;
import org.impalaframework.plugin.spec.modification.PluginModificationCalculator;
import org.impalaframework.plugin.spec.modification.PluginTransitionSet;
import org.impalaframework.resolver.ClassLocationResolver;
import org.impalaframework.resolver.PropertyClassLocationResolver;
import org.springframework.context.ConfigurableApplicationContext;

public class PluginTransitionManagerTest extends TestCase {

	private static final String plugin1 = "impala-sample-dynamic-plugin1";

	private static final String plugin2 = "impala-sample-dynamic-plugin2";

	private static final String plugin3 = "impala-sample-dynamic-plugin3";

	public void setUp() {
		System.setProperty("impala.parent.project", "impala");
	}

	public void tearDown() {
		System.clearProperty("impala.parent.project");
	}

	public void testProcessTransitions() {
		
		PluginTransitionManager tm = new PluginTransitionManager();
		PluginLoaderRegistry registry = new PluginLoaderRegistry();
		ClassLocationResolver resolver = new PropertyClassLocationResolver();
		registry.setPluginLoader(PluginTypes.ROOT, new ParentPluginLoader(resolver));
		registry.setPluginLoader(PluginTypes.APPLICATION, new ApplicationPluginLoader(resolver));
		tm.setContextLoader(new RegistryBasedApplicationContextLoader(registry));

		ParentSpec test1Spec = new Test1().getPluginSpec();
		PluginModificationCalculator calculator = new PluginModificationCalculator();
		PluginTransitionSet transitions = calculator.getTransitions(null, test1Spec);
		tm.processTransitions(transitions);

		ConfigurableApplicationContext context = tm.getParentContext();
		service((FileMonitor) context.getBean("bean1"));
		noService((FileMonitor) context.getBean("bean3"));

		ParentSpec test2Spec = new Test2().getPluginSpec();
		transitions = calculator.getTransitions(test1Spec, test2Spec);
		tm.processTransitions(transitions);

		context = tm.getParentContext();
		service((FileMonitor) context.getBean("bean1"));
		//now we got bean3
		service((FileMonitor) context.getBean("bean3"));

	}

	private void noService(FileMonitor f) {
		try {
			f.lastModified((File) null);
			fail();
		}
		catch (NoServiceException e) {
		}
	}

	private void service(FileMonitor f) {
		f.lastModified((File) null);
	}

	class Test1 implements PluginSpecProvider {
		PluginSpecBuilder spec = new SimplePluginSpecBuilder("parentTestContext.xml", new String[] { plugin1, plugin2 });

		public Test1() {
		}

		public ParentSpec getPluginSpec() {
			return spec.getParentSpec();
		}
	}

	class Test2 implements PluginSpecProvider {
		PluginSpecBuilder spec = new SimplePluginSpecBuilder("parentTestContext.xml", new String[] { plugin1, plugin2 });

		public Test2() {

			PluginSpec p2 = spec.getParentSpec().getPlugin(plugin2);
			new SimplePluginSpec(p2, plugin3);
		}

		public ParentSpec getPluginSpec() {
			return spec.getParentSpec();
		}
	}

}
