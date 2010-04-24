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

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.easymock.classextension.EasyMock.createMock;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.classloader.TestClassLoaderFactory;
import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.RuntimeModule;
import org.impalaframework.module.holder.ModuleClassLoaderRegistry;
import org.impalaframework.module.monitor.DefaultModuleRuntimeMonitor;
import org.impalaframework.module.monitor.ModuleChangeMonitor;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.TestApplicationManager;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

public class BaseModuleRuntimeTest extends TestCase {

    private TestModuleRuntime moduleRuntime;
    private ModuleDefinition definition1;
    private ModuleChangeMonitor monitor;
    private ModuleLocationResolver moduleLocationResolver;
    private List<Resource> resources;
    private ModuleClassLoaderRegistry classLoaderRegistry;
    private Application application;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        classLoaderRegistry = new ModuleClassLoaderRegistry();
        moduleRuntime = new TestModuleRuntime();    
        moduleRuntime.setClassLoaderFactory(new TestClassLoaderFactory());
        
        definition1 = createMock(ModuleDefinition.class);
        monitor = createMock(ModuleChangeMonitor.class);
        moduleLocationResolver = createMock(ModuleLocationResolver.class);
        resources = new ArrayList<Resource>();
        
        application = TestApplicationManager.newApplicationManager(classLoaderRegistry, null, null).getCurrentApplication();
    }
    
    public void testAfterModuleLoadedNull() throws Exception {
        moduleRuntime.afterModuleLoaded(definition1);
    }
    
    public void testWithModuleLoader() throws Exception {

        DefaultModuleRuntimeMonitor runtimeMonitor = new DefaultModuleRuntimeMonitor();
        runtimeMonitor.setModuleChangeMonitor(monitor);
        runtimeMonitor.setModuleLocationResolver(moduleLocationResolver);
        moduleRuntime.setModuleRuntimeMonitor(runtimeMonitor);

        expect(definition1.getName()).andReturn("myName");
        expect(moduleLocationResolver.getApplicationModuleClassLocations("myName")).andReturn(resources);
        this.monitor.setResourcesToMonitor(eq("myName"), aryEq(resources.toArray(new Resource[0])));
        
        replay(definition1, monitor, moduleLocationResolver);
        
        moduleRuntime.afterModuleLoaded(definition1);
        
        verify(definition1, monitor, moduleLocationResolver);
    }
    
    public void testLoad() throws Exception {
        
        expect(definition1.getParentDefinition()).andReturn(null);
        expect(definition1.getName()).andReturn("myName");
        
        replay(definition1, monitor, moduleLocationResolver);
        
        final RuntimeModule runtimeModule = moduleRuntime.loadRuntimeModule(application, definition1);
        assertTrue(runtimeModule instanceof SimpleRuntimeModule);
        assertEquals(definition1, runtimeModule.getModuleDefinition());
        assertEquals(ClassUtils.getDefaultClassLoader(), classLoaderRegistry.getClassLoader("myName"));
        
        verify(definition1, monitor, moduleLocationResolver);
    }
    
    public void testFail() throws Exception {
        
        moduleRuntime = new TestModuleRuntime() {

            @Override
            protected RuntimeModule doLoadModule(Application application, ModuleDefinition definition) {
                throw new RuntimeException();
            }
            
        };
        
        expect(definition1.getName()).andReturn("myName");
        
        replay(definition1, monitor, moduleLocationResolver);
        
        try {
            moduleRuntime.loadRuntimeModule(application, definition1);
            fail();
        }
        catch (RuntimeException e) {
        }
        assertNull(classLoaderRegistry.getClassLoader("myName"));
        
        verify(definition1, monitor, moduleLocationResolver);
    }
    
    public void testClose() throws Exception {
        
        expect(definition1.getName()).andReturn("myName");

        replay(definition1, monitor, moduleLocationResolver);

        final ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
        classLoaderRegistry.addClassLoader("myName", classLoader);
        moduleRuntime.closeModule(application, new SimpleRuntimeModule(classLoader, definition1));
        assertNull(classLoaderRegistry.getClassLoader("myName"));
        
        verify(definition1, monitor, moduleLocationResolver);
    }
}

class TestModuleRuntime extends BaseModuleRuntime {

    @Override
    protected RuntimeModule doLoadModule(Application application, ClassLoader classLoader, ModuleDefinition definition) {
        return new SimpleRuntimeModule(ClassUtils.getDefaultClassLoader(), definition);
    }

    public void doCloseModule(String applicationId, RuntimeModule runtimeModule) {
    }

    public String getRuntimeName() {
        return null;
    }
    
}
