/*
 * Copyright 2007-2010 the original author or authors.
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
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ClassLoaderRegistry;
import org.impalaframework.module.spi.TestApplicationManager;
import org.springframework.util.ClassUtils;

public class SimpleModuleRuntimeTest extends TestCase {
    
    private SimpleModuleRuntime runtime;
    private ClassLoaderFactory classLoaderFactory;
    private ClassLoaderRegistry classLoaderRegistry;
    private Application application;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        runtime = new SimpleModuleRuntime();
        classLoaderFactory = createMock(ClassLoaderFactory.class);
        classLoaderRegistry = createMock(ClassLoaderRegistry.class);
        runtime.setClassLoaderFactory(classLoaderFactory);
 
        application = TestApplicationManager.newApplicationManager(classLoaderRegistry, null, null).getCurrentApplication();
    }

    public void testDoLoadModule() {
        final SimpleModuleDefinition definition = new SimpleModuleDefinition("mymodule");
        expect(classLoaderFactory.newClassLoader(application, ClassUtils.getDefaultClassLoader(), definition)).andReturn(ClassUtils.getDefaultClassLoader());
        
        replay(classLoaderFactory, classLoaderRegistry);
        
        final RuntimeModule module = runtime.doLoadModule(application, definition);
        assertTrue(module instanceof SimpleRuntimeModule);
        
        verify(classLoaderFactory, classLoaderRegistry);
    }

    public void testDoLoadModuleWithParent() {
        final SimpleModuleDefinition parent = new SimpleModuleDefinition("parent");
        final SimpleModuleDefinition definition = new SimpleModuleDefinition(parent, "mymodule");
        final ModuleClassLoader parentClassLoader = new ModuleClassLoader(new File[] {new File("./")});
        expect(classLoaderRegistry.getClassLoader("parent")).andReturn(parentClassLoader);
        expect(classLoaderFactory.newClassLoader(application, parentClassLoader, definition)).andReturn(ClassUtils.getDefaultClassLoader());
        
        replay(classLoaderFactory, classLoaderRegistry);
        
        final RuntimeModule module = runtime.doLoadModule(application, definition);
        assertTrue(module instanceof SimpleRuntimeModule);
        
        verify(classLoaderFactory, classLoaderRegistry);
    }

}
