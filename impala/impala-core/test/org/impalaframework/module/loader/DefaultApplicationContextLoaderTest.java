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
import org.impalaframework.module.bootstrap.BeanFactoryModuleManagementFactory;
import org.impalaframework.module.bootstrap.ModuleManagementFactory;
import org.impalaframework.module.builder.SimpleModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.holder.DefaultModuleStateHolder;
import org.impalaframework.module.monitor.ModuleChangeListener;
import org.impalaframework.module.monitor.ModuleChangeMonitor;
import org.impalaframework.module.operation.AddModuleOperation;
import org.impalaframework.module.operation.ModuleOperationInput;
import org.impalaframework.module.operation.RemoveModuleOperation;
import org.impalaframework.resolver.PropertyModuleLocationResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

/**
 * @author Phil Zoio
 */
public class DefaultApplicationContextLoaderTest extends TestCase {

	private static final String plugin1 = "impala-sample-dynamic-plugin1";

	private static final String plugin2 = "impala-sample-dynamic-plugin2";

	private static final String plugin3 = "impala-sample-dynamic-plugin3";

	private DefaultModuleStateHolder moduleStateHolder;

	private ModuleManagementFactory factory;

	public void setUp() {
		System.setProperty("impala.parent.project", "impala-core");
		
		factory = new BeanFactoryModuleManagementFactory(new ClassPathXmlApplicationContext(
				"META-INF/impala-bootstrap.xml"));
		PropertyModuleLocationResolver resolver = new PropertyModuleLocationResolver();

		ModuleLoaderRegistry registry = factory.getPluginLoaderRegistry();
		registry.setModuleLoader(ModuleTypes.ROOT, new RootModuleLoader(resolver){
			@Override
			public ClassLoader newClassLoader(ModuleDefinition moduleDefinition, ApplicationContext parent) {
				return this.getClass().getClassLoader();
			}}) ;
		registry.setModuleLoader(ModuleTypes.APPLICATION, new ApplicationModuleLoader(resolver));

		moduleStateHolder = (DefaultModuleStateHolder) factory.getModuleStateHolder();
	}
	
	public void tearDown() {
		System.clearProperty("impala.parent.project");
	}

	public void testResourceBasedValue() {
		ModuleDefinitionSource spec = new SimpleModuleDefinitionSource("parentTestContext.xml", new String[] { plugin1, plugin2 });
		ModuleDefinition p2 = spec.getModuleDefinition().getModule(plugin2);
		new SimpleModuleDefinition(p2, plugin3);
		addModule(spec);

		ConfigurableApplicationContext parent = moduleStateHolder.getParentContext();

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

		addModule(spec);
		ModuleDefinition root = spec.getModuleDefinition();

		ConfigurableApplicationContext parent = moduleStateHolder.getParentContext();
		assertNotNull(parent);
		assertEquals(3, moduleStateHolder.getModuleContexts().size());

		FileMonitor bean1 = (FileMonitor) parent.getBean("bean1");
		assertEquals(999L, bean1.lastModified((File) null));

		FileMonitor bean2 = (FileMonitor) parent.getBean("bean2");
		assertEquals(100L, bean2.lastModified((File) null));

		// shutdown plugin and check behaviour has gone
		removeModule(plugin2);

		try {
			bean2.lastModified((File) null);
			fail();
		}
		catch (NoServiceException e) {
		}

		// bean 2 still works
		assertEquals(999L, bean1.lastModified((File) null));

		removeModule(plugin1);

		try {
			bean1.lastModified((File) null);
			fail();
		}
		catch (NoServiceException e) {
		}

		// now reload the plugin, and see that behaviour returns
		
		addModule(new SimpleModuleDefinition(plugin2));
		bean2 = (FileMonitor) parent.getBean("bean2");
		assertEquals(100L, bean2.lastModified((File) null));

		addModule(new SimpleModuleDefinition(plugin1));
		bean1 = (FileMonitor) parent.getBean("bean1");
		assertEquals(999L, bean1.lastModified((File) null));

		FileMonitor bean3 = (FileMonitor) parent.getBean("bean3");
		try {
			bean3.lastModified((File) null);
			fail();
		}
		catch (NoServiceException e) {
		}

		ModuleDefinition p2 = root.getModule(plugin2);
		addModule(new SimpleModuleDefinition(p2, plugin3));
		assertEquals(333L, bean3.lastModified((File) null));

		final ConfigurableApplicationContext applicationPlugin3 = moduleStateHolder.getModule(plugin3);
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
		final ModuleDefinition p2 = spec.getModuleDefinition().getModule(plugin2);
		new SimpleModuleDefinition(p2, plugin3);

		addModule(spec);

		ConfigurableApplicationContext parent = moduleStateHolder.getParentContext();
		assertNotNull(parent);

		FileMonitor bean3 = (FileMonitor) parent.getBean("bean3");
		bean3.lastModified((File) null);

		// check that all three plugins have loaded
		assertEquals(4, moduleStateHolder.getModuleContexts().size());
	}

	private void addModule(ModuleDefinitionSource source) {
		RootModuleDefinition moduleDefinition = source.getModuleDefinition();
		addModule(moduleDefinition);
	}

	private void addModule(ModuleDefinition moduleDefinition) {
		new AddModuleOperation(factory).execute(new ModuleOperationInput(null, moduleDefinition, null));
	}

	private void removeModule(String moduleName) {
		new RemoveModuleOperation(factory).execute(new ModuleOperationInput(null, null, moduleName));
	}

	class RecordingPluginMonitor implements ModuleChangeMonitor {

		private int started = 0;

		private int stopped = 0;

		public void addModificationListener(ModuleChangeListener listener) {
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
