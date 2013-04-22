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

package org.impalaframework.spring.module;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.easymock.classextension.EasyMock.createMock;
import junit.framework.TestCase;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.module.RuntimeModule;
import org.impalaframework.module.holder.ModuleClassLoaderRegistry;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ModuleStateHolder;
import org.impalaframework.module.spi.TestApplicationManager;
import org.springframework.context.ConfigurableApplicationContext;

public class SpringModuleRuntimeTest extends TestCase {

    private SpringModuleRuntime moduleRuntime;
    private ModuleStateHolder moduleStateHolder;
    private ConfigurableApplicationContext applicationContext;
    private ModuleDefinition definition1;
    private ModuleDefinition definition2;
    private ModuleDefinition definition3;
    private Application application;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        moduleRuntime = new SpringModuleRuntime();
        moduleStateHolder = createMock(ModuleStateHolder.class);
        
        applicationContext = createMock(ConfigurableApplicationContext.class);
        
        definition1 = createMock(ModuleDefinition.class);
        definition2 = createMock(ModuleDefinition.class);
        definition3 = createMock(ModuleDefinition.class);

        application = TestApplicationManager.newApplicationManager(new ModuleClassLoaderRegistry(), moduleStateHolder, null).getCurrentApplication();
    }
    
    public void testGetParentApplicationContextWithNonSpringModule() throws Exception {

        SpringRuntimeModule module1 = createMock(SpringRuntimeModule.class);
        RuntimeModule module2 = createMock(RuntimeModule.class);
        SpringRuntimeModule module3 = createMock(SpringRuntimeModule.class);
        
        expect(definition3.getParentDefinition()).andReturn(definition2);
        expect(definition2.getName()).andReturn("definition2");
        expect(moduleStateHolder.getModule("definition2")).andReturn(module2);
        
        //this is not a SpringRuntimeModule, so go to the next one
        expect(definition2.getParentDefinition()).andReturn(definition1);
        expect(definition1.getName()).andReturn("definition1");
        expect(moduleStateHolder.getModule("definition1")).andReturn(module1);
        expect(module1.getApplicationContext()).andReturn(applicationContext);
        
        replay(definition1, definition2, definition3, module1, module2, module3, moduleStateHolder);
        
        assertSame(applicationContext, moduleRuntime.getParentApplicationContext(application, definition3));
        
        verify(definition1, definition2, definition3, module1, module2, module3, moduleStateHolder);
    }
    
    public void testGetParentApplicationContext() throws Exception {
        
        SpringRuntimeModule module1 = createMock(SpringRuntimeModule.class);
        SpringRuntimeModule module3 = createMock(SpringRuntimeModule.class);
        
        expect(definition3.getParentDefinition()).andReturn(definition1);
        expect(definition1.getName()).andReturn("definition1");
        expect(moduleStateHolder.getModule("definition1")).andReturn(module1);
        expect(module1.getApplicationContext()).andReturn(applicationContext);
        
        replay(definition1, definition2, definition3, module1, module3, moduleStateHolder);
        
        assertSame(applicationContext, moduleRuntime.getParentApplicationContext(application, definition3));
        
        verify(definition1, definition2, definition3, module1, module3, moduleStateHolder);
    }
    
    public void testClose() throws Exception {
        
        ApplicationContextLoader loader = createMock(ApplicationContextLoader.class);
        moduleRuntime.setApplicationContextLoader(loader);
        
        expect(definition1.getName()).andReturn("definition1");
        loader.closeContext(application.getId(), definition1, applicationContext);
        
        replay(definition1, applicationContext, loader);
        
        moduleRuntime.closeModule(application, new DefaultSpringRuntimeModule(definition1, applicationContext));
        
        verify(definition1, applicationContext, loader);
    }
    
    public void testGetParentApplicationContextNull() throws Exception {
        
        expect(definition3.getParentDefinition()).andReturn(null);
        
        replay(definition1, definition2, definition3, moduleStateHolder);
        
        assertNull(moduleRuntime.getParentApplicationContext(application, definition3));
        
        verify(definition1, definition2, definition3, moduleStateHolder);
    }
    
}
