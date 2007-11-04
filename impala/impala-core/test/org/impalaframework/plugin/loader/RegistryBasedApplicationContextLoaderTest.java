/*
 * Copyright 2007 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.plugin.loader;

import java.io.File;

import junit.framework.TestCase;

import org.impalaframework.file.monitor.FileMonitor;
import org.impalaframework.plugin.builder.PluginSpecBuilder;
import org.impalaframework.plugin.builder.SimplePluginSpecBuilder;
import org.impalaframework.plugin.loader.ApplicationPluginLoader;
import org.impalaframework.plugin.loader.ParentPluginLoader;
import org.impalaframework.plugin.loader.PluginLoaderRegistry;
import org.impalaframework.plugin.monitor.PluginModificationListener;
import org.impalaframework.plugin.monitor.PluginMonitor;
import org.impalaframework.plugin.plugin.NoServiceException;
import org.impalaframework.plugin.plugin.PluginTypes;
import org.impalaframework.plugin.spec.ApplicationContextSet;
import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.plugin.spec.SimplePluginSpec;
import org.impalaframework.plugin.util.RegistryBasedApplicationContextLoader;
import org.impalaframework.resolver.PropertyClassLocationResolver;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

/**
 * @author Phil Zoio
 */
public class RegistryBasedApplicationContextLoaderTest extends TestCase {

	private RegistryBasedApplicationContextLoader loader;

	private static final String plugin1 = "impala-sample-dynamic-plugin1";

	private static final String plugin2 = "impala-sample-dynamic-plugin2";

	private static final String plugin3 = "impala-sample-dynamic-plugin3";

	public void setUp() {
		PropertyClassLocationResolver resolver = new PropertyClassLocationResolver();

		PluginLoaderRegistry registry = new PluginLoaderRegistry();
		registry.setPluginLoader(PluginTypes.ROOT, new ParentPluginLoader(resolver));
		registry.setPluginLoader(PluginTypes.APPLICATION, new ApplicationPluginLoader(resolver));

		loader = new RegistryBasedApplicationContextLoader(registry);
	}

	public void testResourceBasedValue() {
		PluginSpecBuilder spec = new SimplePluginSpecBuilder("parentTestContext.xml", new String[] { plugin1, plugin2 });
		PluginSpec p2 = spec.getParentSpec().getPlugin(plugin2);
		new SimplePluginSpec(p2, plugin3);

		final ApplicationContextSet appSet = new ApplicationContextSet();
		loader.addApplicationPlugin(appSet, spec.getParentSpec(), null);

		ConfigurableApplicationContext parent = appSet.getContext();

		// the implementing FileMonitorBean3 will find the monitor.properties
		// file
		FileMonitor bean3 = (FileMonitor) parent.getBean("bean3");
		assertEquals(333L, bean3.lastModified((File) null));

		// this time, we will not be able to find the resource from
		// FileMonitorBean3
		assertEquals(100L, bean3.lastModified(new File("./")));
	}

	public void testLoadUnloadPlugins() {

		PluginSpecBuilder spec = new SimplePluginSpecBuilder("parentTestContext.xml", new String[] { plugin1, plugin2 });

		final ApplicationContextSet appSet = new ApplicationContextSet();
		loader.addApplicationPlugin(appSet, spec.getParentSpec(), null);
		PluginSpec root = spec.getParentSpec();

		ConfigurableApplicationContext parent = appSet.getContext();
		assertNotNull(parent);
		assertEquals(3, appSet.getPluginContext().size());

		FileMonitor bean1 = (FileMonitor) parent.getBean("bean1");
		assertEquals(999L, bean1.lastModified((File) null));

		FileMonitor bean2 = (FileMonitor) parent.getBean("bean2");
		assertEquals(100L, bean2.lastModified((File) null));

		// shutdown plugin and check behaviour has gone
		ConfigurableApplicationContext child2 = appSet.getPluginContext().get(plugin2);
		child2.close();

		try {
			bean2.lastModified((File) null);
			fail();
		}
		catch (NoServiceException e) {
		}

		// bean 2 still works
		assertEquals(999L, bean1.lastModified((File) null));

		ConfigurableApplicationContext child1 = appSet.getPluginContext().get(plugin1);
		child1.close();

		try {
			bean1.lastModified((File) null);
			fail();
		}
		catch (NoServiceException e) {
		}

		// now reload the plugin, and see that behaviour returns
		loader.addApplicationPlugin(appSet, new SimplePluginSpec(plugin2), parent);
		bean2 = (FileMonitor) parent.getBean("bean2");
		assertEquals(100L, bean2.lastModified((File) null));

		loader.addApplicationPlugin(appSet, new SimplePluginSpec(plugin1), parent);
		bean1 = (FileMonitor) parent.getBean("bean1");
		assertEquals(999L, bean1.lastModified((File) null));

		FileMonitor bean3 = (FileMonitor) parent.getBean("bean3");
		try {
			bean3.lastModified((File) null);
			fail();
		}
		catch (NoServiceException e) {
		}

		PluginSpec p2 = root.getPlugin(plugin2);

		final ConfigurableApplicationContext applicationPlugin2 = appSet.getPluginContext().get(plugin2);

		loader.addApplicationPlugin(appSet, new SimplePluginSpec(p2, plugin3), applicationPlugin2);
		assertEquals(333L, bean3.lastModified((File) null));

		final ConfigurableApplicationContext applicationPlugin3 = appSet.getPluginContext().get(plugin3);
		applicationPlugin3.close();

		try {
			bean3.lastModified((File) null);
			fail();
		}
		catch (NoServiceException e) {
		}
	}

	public void testLoadAll() {

		PluginSpecBuilder spec = new SimplePluginSpecBuilder("parentTestContext.xml", new String[] { plugin1, plugin2 });
		final PluginSpec p2 = spec.getParentSpec().getPlugin(plugin2);
		new SimplePluginSpec(p2, plugin3);

		final ApplicationContextSet loaded = new ApplicationContextSet();
		loader.addApplicationPlugin(loaded, spec.getParentSpec(), null);

		ConfigurableApplicationContext parent = loaded.getContext();
		assertNotNull(parent);

		FileMonitor bean3 = (FileMonitor) parent.getBean("bean3");
		bean3.lastModified((File) null);

		// check that all three plugins have loaded
		assertEquals(4, loaded.getPluginContext().size());
	}

	public void testSetMonitor() {
		RegistryBasedApplicationContextLoader loader = new RegistryBasedApplicationContextLoader(
				new PluginLoaderRegistry());
		PluginMonitor monitor = loader.getPluginMonitor();
		assertNull(monitor);

		RecordingPluginMonitor recording1 = new RecordingPluginMonitor();
		loader.setPluginMonitor(recording1);
		assertEquals(1, recording1.getStarted());
		
		RecordingPluginMonitor recording2 = new RecordingPluginMonitor();
		loader.setPluginMonitor(recording2);
		assertEquals(1, recording1.getStarted());
		assertEquals(1, recording1.getStopped());
		assertEquals(1, recording2.getStarted());
		assertEquals(0, recording2.getStopped());
		
		assertSame(recording2, loader.getPluginMonitor());
		
		loader.setPluginMonitor(null);
		assertEquals(1, recording2.getStopped());
		assertNull(loader.getPluginMonitor());
	}

	class RecordingPluginMonitor implements PluginMonitor {

		private int started = 0;

		private int stopped = 0;

		public void addModificationListener(PluginModificationListener listener) {
		}

		public void setResourcesToMonitor(String pluginName, Resource[] resources) {
		}

		public void start() {
			started++;
		}

		public void stop() {
			stopped++;
		}

		public int getStarted() {
			return started;
		}

		public int getStopped() {
			return stopped;
		}

	}

}
