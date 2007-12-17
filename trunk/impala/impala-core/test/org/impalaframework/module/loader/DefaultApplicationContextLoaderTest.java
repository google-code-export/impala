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

package org.impalaframework.module.loader;

import java.io.File;

import junit.framework.TestCase;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.file.monitor.FileMonitor;
import org.impalaframework.module.builder.SimpleModuleDefinitionSource;
import org.impalaframework.module.loader.ApplicationPluginLoader;
import org.impalaframework.module.loader.DefaultApplicationContextLoader;
import org.impalaframework.module.loader.ParentPluginLoader;
import org.impalaframework.module.loader.PluginLoaderRegistry;
import org.impalaframework.module.modification.PluginModificationCalculator;
import org.impalaframework.module.modification.PluginTransition;
import org.impalaframework.module.modification.StrictPluginModificationCalculator;
import org.impalaframework.module.monitor.PluginModificationListener;
import org.impalaframework.module.monitor.PluginMonitor;
import org.impalaframework.module.operation.AddPluginOperation;
import org.impalaframework.module.operation.RemovePluginOperation;
import org.impalaframework.module.spec.ModuleDefinition;
import org.impalaframework.module.spec.ModuleDefinitionSource;
import org.impalaframework.module.spec.ModuleTypes;
import org.impalaframework.module.spec.SimpleModuleDefinition;
import org.impalaframework.module.transition.DefaultPluginStateManager;
import org.impalaframework.module.transition.LoadTransitionProcessor;
import org.impalaframework.module.transition.TransitionProcessorRegistry;
import org.impalaframework.module.transition.UnloadTransitionProcessor;
import org.impalaframework.resolver.PropertyClassLocationResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

/**
 * @author Phil Zoio
 */
public class DefaultApplicationContextLoaderTest extends TestCase {

	private DefaultApplicationContextLoader loader;

	private static final String plugin1 = "impala-sample-dynamic-plugin1";

	private static final String plugin2 = "impala-sample-dynamic-plugin2";

	private static final String plugin3 = "impala-sample-dynamic-plugin3";

	private DefaultPluginStateManager pluginStateManager;

	private PluginModificationCalculator calculator;

	public void setUp() {
		System.setProperty("impala.parent.project", "impala-core");
		
		PropertyClassLocationResolver resolver = new PropertyClassLocationResolver();

		PluginLoaderRegistry registry = new PluginLoaderRegistry();
		registry.setPluginLoader(ModuleTypes.ROOT, new ParentPluginLoader(resolver){
			@Override
			public ClassLoader newClassLoader(ModuleDefinition moduleDefinition, ApplicationContext parent) {
				return this.getClass().getClassLoader();
			}}) ;
		registry.setPluginLoader(ModuleTypes.APPLICATION, new ApplicationPluginLoader(resolver));

		loader = new DefaultApplicationContextLoader(registry);
		pluginStateManager = new DefaultPluginStateManager();
		
		TransitionProcessorRegistry transitionProcessors = new TransitionProcessorRegistry();
		LoadTransitionProcessor loadTransitionProcessor = new LoadTransitionProcessor(loader);
		UnloadTransitionProcessor unloadTransitionProcessor = new UnloadTransitionProcessor();
		transitionProcessors.addTransitionProcessor(PluginTransition.UNLOADED_TO_LOADED, loadTransitionProcessor);
		transitionProcessors.addTransitionProcessor(PluginTransition.LOADED_TO_UNLOADED, unloadTransitionProcessor);
		pluginStateManager.setTransitionProcessorRegistry(transitionProcessors);
		
		calculator = new StrictPluginModificationCalculator();
	}
	
	public void tearDown() {
		System.clearProperty("impala.parent.project");
	}

	public void testResourceBasedValue() {
		ModuleDefinitionSource spec = new SimpleModuleDefinitionSource("parentTestContext.xml", new String[] { plugin1, plugin2 });
		ModuleDefinition p2 = spec.getModuleDefintion().getPlugin(plugin2);
		new SimpleModuleDefinition(p2, plugin3);
		AddPluginOperation.addPlugin(pluginStateManager, calculator, spec.getModuleDefintion());

		ConfigurableApplicationContext parent = pluginStateManager.getParentContext();

		// the implementing FileMonitorBean3 will find the monitor.properties
		// file
		FileMonitor bean3 = (FileMonitor) parent.getBean("bean3");
		assertEquals(333L, bean3.lastModified((File) null));

		// this time, we will not be able to find the resource from
		// FileMonitorBean3
		assertEquals(100L, bean3.lastModified(new File("./")));
	}

	public void testLoadUnloadPlugins() {

		ModuleDefinitionSource spec = new SimpleModuleDefinitionSource("parentTestContext.xml", new String[] { plugin1, plugin2 });

		AddPluginOperation.addPlugin(pluginStateManager, calculator, spec.getModuleDefintion());
		ModuleDefinition root = spec.getModuleDefintion();

		ConfigurableApplicationContext parent = pluginStateManager.getParentContext();
		assertNotNull(parent);
		assertEquals(3, pluginStateManager.getPlugins().size());

		FileMonitor bean1 = (FileMonitor) parent.getBean("bean1");
		assertEquals(999L, bean1.lastModified((File) null));

		FileMonitor bean2 = (FileMonitor) parent.getBean("bean2");
		assertEquals(100L, bean2.lastModified((File) null));

		// shutdown plugin and check behaviour has gone
		RemovePluginOperation.removePlugin(pluginStateManager, calculator, plugin2);

		try {
			bean2.lastModified((File) null);
			fail();
		}
		catch (NoServiceException e) {
		}

		// bean 2 still works
		assertEquals(999L, bean1.lastModified((File) null));

		RemovePluginOperation.removePlugin(pluginStateManager, calculator, plugin1);

		try {
			bean1.lastModified((File) null);
			fail();
		}
		catch (NoServiceException e) {
		}

		// now reload the plugin, and see that behaviour returns
		AddPluginOperation.addPlugin(pluginStateManager, calculator, new SimpleModuleDefinition(plugin2));
		bean2 = (FileMonitor) parent.getBean("bean2");
		assertEquals(100L, bean2.lastModified((File) null));

		AddPluginOperation.addPlugin(pluginStateManager, calculator, new SimpleModuleDefinition(plugin1));
		bean1 = (FileMonitor) parent.getBean("bean1");
		assertEquals(999L, bean1.lastModified((File) null));

		FileMonitor bean3 = (FileMonitor) parent.getBean("bean3");
		try {
			bean3.lastModified((File) null);
			fail();
		}
		catch (NoServiceException e) {
		}

		ModuleDefinition p2 = root.getPlugin(plugin2);
		AddPluginOperation.addPlugin(pluginStateManager, calculator, new SimpleModuleDefinition(p2, plugin3));
		assertEquals(333L, bean3.lastModified((File) null));

		final ConfigurableApplicationContext applicationPlugin3 = pluginStateManager.getPlugin(plugin3);
		applicationPlugin3.close();

		try {
			bean3.lastModified((File) null);
			fail();
		}
		catch (NoServiceException e) {
		}
	}

	public void testLoadAll() {

		ModuleDefinitionSource spec = new SimpleModuleDefinitionSource("parentTestContext.xml", new String[] { plugin1, plugin2 });
		final ModuleDefinition p2 = spec.getModuleDefintion().getPlugin(plugin2);
		new SimpleModuleDefinition(p2, plugin3);

		AddPluginOperation.addPlugin(pluginStateManager, calculator, spec.getModuleDefintion());

		ConfigurableApplicationContext parent = pluginStateManager.getParentContext();
		assertNotNull(parent);

		FileMonitor bean3 = (FileMonitor) parent.getBean("bean3");
		bean3.lastModified((File) null);

		// check that all three plugins have loaded
		assertEquals(4, pluginStateManager.getPlugins().size());
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
