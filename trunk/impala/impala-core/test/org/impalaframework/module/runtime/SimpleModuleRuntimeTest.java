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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.File;

import junit.framework.TestCase;

import org.impalaframework.classloader.ClassLoaderFactory;
import org.impalaframework.classloader.ModuleClassLoader;
import org.impalaframework.module.RuntimeModule;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.spi.ModuleClassLoaderSource;
import org.springframework.util.ClassUtils;

public class SimpleModuleRuntimeTest extends TestCase {
	
	private SimpleModuleRuntime runtime;
	private ClassLoaderFactory classLoaderFactory;
	private ModuleClassLoaderSource moduleClassLoaderSource;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		runtime = new SimpleModuleRuntime();
		classLoaderFactory = createMock(ClassLoaderFactory.class);
		moduleClassLoaderSource = createMock(ModuleClassLoaderSource.class);
		runtime.setClassLoaderFactory(classLoaderFactory);
		runtime.setModuleClassLoaderSource(moduleClassLoaderSource);
	}

	public void testDoLoadModule() {
		final SimpleModuleDefinition definition = new SimpleModuleDefinition("mymodule");
		expect(classLoaderFactory.newClassLoader(null, definition)).andReturn(ClassUtils.getDefaultClassLoader());
		
		replay(classLoaderFactory, moduleClassLoaderSource);
		
		final RuntimeModule module = runtime.doLoadModule(definition);
		assertTrue(module instanceof SimpleRuntimeModule);
		
		verify(classLoaderFactory, moduleClassLoaderSource);
	}

	public void testDoLoadModuleWithParent() {
		final SimpleModuleDefinition parent = new SimpleModuleDefinition("parent");
		final SimpleModuleDefinition definition = new SimpleModuleDefinition(parent, "mymodule");
		final ModuleClassLoader parentClassLoader = new ModuleClassLoader(new File[] {new File("./")});
		expect(moduleClassLoaderSource.getClassLoader("parent")).andReturn(parentClassLoader);
		expect(classLoaderFactory.newClassLoader(parentClassLoader, definition)).andReturn(ClassUtils.getDefaultClassLoader());
		
		replay(classLoaderFactory, moduleClassLoaderSource);
		
		final RuntimeModule module = runtime.doLoadModule(definition);
		assertTrue(module instanceof SimpleRuntimeModule);
		
		verify(classLoaderFactory, moduleClassLoaderSource);
	}

}
