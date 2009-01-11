/*
 * Copyright 2007-2008 the original author or authors.
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

package org.impalaframework.module.runtime;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.easymock.classextension.EasyMock.createMock;
import junit.framework.TestCase;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.RuntimeModule;
import org.impalaframework.module.holder.ModuleClassLoaderRegistry;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.impalaframework.module.monitor.ModuleChangeMonitor;
import org.impalaframework.module.spi.ModuleLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

public class BaseModuleRuntimeTest extends TestCase {

	private TestModuleRuntime moduleRuntime;
	private ModuleDefinition definition1;
	private ModuleChangeMonitor monitor;
	private ModuleLoaderRegistry registry;
	private ModuleLoader loader;
	private Resource[] resources;
	private ModuleClassLoaderRegistry classLoaderRegistry;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		classLoaderRegistry = new ModuleClassLoaderRegistry();
		moduleRuntime = new TestModuleRuntime();	
		moduleRuntime.setClassLoaderRegistry(classLoaderRegistry);
		
		definition1 = createMock(ModuleDefinition.class);
		monitor = createMock(ModuleChangeMonitor.class);
		registry = new ModuleLoaderRegistry();
		loader = createMock(ModuleLoader.class);
		registry.addItem("spring-application", loader);
		resources = new Resource[0];
	}
	
	public void testAfterModuleLoadedNull() throws Exception {
		moduleRuntime.afterModuleLoaded(definition1);
	}
	
	public void testNoModuleLoader() throws Exception {
		
		moduleRuntime.setModuleChangeMonitor(monitor);
		moduleRuntime.setModuleLoaderRegistry(registry);
		
		expect(definition1.getRuntimeFramework()).andReturn("spring");
		expect(definition1.getType()).andReturn("anotherType");
		//does nothing after this, as there is no module loader
		
		replay(definition1, monitor, loader);
		
		moduleRuntime.afterModuleLoaded(definition1);
		
		verify(definition1, monitor, loader);
	}
	
	public void testWithModuleLoader() throws Exception {
		
		moduleRuntime.setModuleChangeMonitor(monitor);
		moduleRuntime.setModuleLoaderRegistry(registry);

		expect(definition1.getRuntimeFramework()).andReturn("spring");
		expect(definition1.getType()).andReturn("application");
		expect(loader.getClassLocations(definition1)).andReturn(resources);
		expect(definition1.getName()).andReturn("myName");
		monitor.setResourcesToMonitor("myName", resources);
		
		replay(definition1, monitor, loader);
		
		moduleRuntime.afterModuleLoaded(definition1);
		
		verify(definition1, monitor, loader);
	}
	
	public void testLoad() throws Exception {
		
		expect(definition1.getName()).andReturn("myName");
		
		replay(definition1, monitor, loader);
		
		final RuntimeModule runtimeModule = moduleRuntime.loadRuntimeModule(definition1);
		assertTrue(runtimeModule instanceof SimpleRuntimeModule);
		assertEquals(definition1, runtimeModule.getModuleDefinition());
		assertEquals(ClassUtils.getDefaultClassLoader(), classLoaderRegistry.getClassLoader("myName"));
		
		verify(definition1, monitor, loader);
	}
	
	public void testClose() throws Exception {
		
		expect(definition1.getName()).andReturn("myName");

		replay(definition1, monitor, loader);

		final ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
		classLoaderRegistry.addClassLoader("myName", classLoader);
		moduleRuntime.closeModule(new SimpleRuntimeModule(classLoader, definition1));
		assertNull(classLoaderRegistry.getClassLoader("myName"));
		
		verify(definition1, monitor, loader);
	}
}

class TestModuleRuntime extends BaseModuleRuntime {

	@Override
	protected RuntimeModule doLoadModule(ModuleDefinition definition) {
		return new SimpleRuntimeModule(ClassUtils.getDefaultClassLoader(), definition);
	}

	public void doCloseModule(RuntimeModule runtimeModule) {
	}

	public String getRuntimeName() {
		return null;
	}
	
}
