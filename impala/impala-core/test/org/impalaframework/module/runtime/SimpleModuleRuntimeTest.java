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

import static org.easymock.EasyMock.*;
import org.impalaframework.classloader.ClassLoaderFactory;
import org.impalaframework.module.RuntimeModule;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.springframework.util.ClassUtils;

import junit.framework.TestCase;

public class SimpleModuleRuntimeTest extends TestCase {
	
	private SimpleModuleRuntime runtime;
	private ClassLoaderFactory classLoaderFactory;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		runtime = new SimpleModuleRuntime();
		classLoaderFactory = createMock(ClassLoaderFactory.class);
		runtime.setClassLoaderFactory(classLoaderFactory);
	}

	public void testDoLoadModule() {
		final SimpleModuleDefinition definition = new SimpleModuleDefinition("mymodule");
		expect(classLoaderFactory.newClassLoader(null, definition)).andReturn(ClassUtils.getDefaultClassLoader());
		
		replay(classLoaderFactory);
		
		final RuntimeModule module = runtime.doLoadModule(definition);
		assertTrue(module instanceof SimpleRuntimeModule);
		
		verify(classLoaderFactory);
	}

}
