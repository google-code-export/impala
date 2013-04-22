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
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.impalaframework.module.holder.SharedModuleDefinitionSources.newTest1;

import java.util.Collections;
import java.util.Set;

import junit.framework.TestCase;

import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.holder.DefaultModuleStateHolder;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ApplicationManager;
import org.impalaframework.module.spi.ModuleRuntimeManager;
import org.impalaframework.module.spi.ModuleStateChange;
import org.impalaframework.module.spi.ModuleStateChangeNotifier;
import org.impalaframework.module.spi.TestApplicationManager;
import org.impalaframework.module.spi.Transition;
import org.impalaframework.module.spi.TransitionSet;
import org.impalaframework.module.transition.DefaultTransitionManager;
import org.impalaframework.module.transition.LoadTransitionProcessor;
import org.impalaframework.module.transition.TransitionProcessorRegistry;
import org.impalaframework.module.transition.UnloadTransitionProcessor;
import org.springframework.context.ConfigurableApplicationContext;

public class ProcessTransitionsTest extends TestCase {

    private ApplicationContextLoader loader;
    private ConfigurableApplicationContext parentContext;
    private ConfigurableApplicationContext childContext;
    private DefaultModuleStateHolder moduleStateHolder;
    private LoadTransitionProcessor loadTransitionProcessor;
    private UnloadTransitionProcessor unloadTransitionProcessor;
    private ModuleStateChangeNotifier moduleStateChangeNotifier;
    private ModuleRuntimeManager moduleRuntimeManager;
    private DefaultTransitionManager transitionManager;
    private Application application;
    
    private void replayMocks() {
        replay(loader);
        replay(parentContext);
        replay(childContext);
        replay(moduleStateChangeNotifier);
        replay(moduleRuntimeManager);
    }
    
    private void verifyMocks() {
        verify(loader);
        verify(parentContext);
        verify(childContext);
        verify(moduleStateChangeNotifier);
        verify(moduleRuntimeManager);
    }
    
    public void setUp() {

        loader = createMock(ApplicationContextLoader.class);
        parentContext = createMock(ConfigurableApplicationContext.class);
        childContext = createMock(ConfigurableApplicationContext.class);
        moduleStateChangeNotifier = createMock(ModuleStateChangeNotifier.class);
        moduleRuntimeManager = createMock(ModuleRuntimeManager.class);

        moduleStateHolder = new DefaultModuleStateHolder();
        transitionManager = new DefaultTransitionManager();
        
        ApplicationManager applicationManager = TestApplicationManager.newApplicationManager(null, moduleStateHolder, null);
        application = applicationManager.getCurrentApplication();
        
        TransitionProcessorRegistry transitionProcessors = new TransitionProcessorRegistry();
        loadTransitionProcessor = new LoadTransitionProcessor();
        unloadTransitionProcessor = new UnloadTransitionProcessor();
        SpringModuleRuntime moduleRuntime = new SpringModuleRuntime();
        moduleRuntime.setApplicationContextLoader(loader);
        
        loadTransitionProcessor.setModuleRuntimeManager(moduleRuntimeManager);
        
        transitionProcessors.addItem(Transition.UNLOADED_TO_LOADED, loadTransitionProcessor);
        transitionProcessors.addItem(Transition.LOADED_TO_UNLOADED, unloadTransitionProcessor);
        transitionManager.setTransitionProcessorRegistry(transitionProcessors);
    }
    
    public void testLoadRoot() {
        
        RootModuleDefinition rootModuleDefinition = newTest1().getModuleDefinition();
        rootModuleDefinition.freeze();
        
        ModuleStateChange moduleStateChange = new ModuleStateChange(Transition.UNLOADED_TO_LOADED, rootModuleDefinition);
        
        //expectations (round 1 - loading of parent)
        expect(moduleRuntimeManager.initModule(application, rootModuleDefinition)).andReturn(true);
        
        replayMocks();
        
        Set<ModuleStateChange> singleton = Collections.singleton(moduleStateChange);
        TransitionSet transitionSet = new TransitionSet(singleton, rootModuleDefinition);
        transitionManager.processTransitions(moduleStateHolder, application, transitionSet);
        verifyMocks();
    }
    
    public void testGetRootModuleContext() {
        assertNull(moduleStateHolder.getRootModule());
    }   
}
