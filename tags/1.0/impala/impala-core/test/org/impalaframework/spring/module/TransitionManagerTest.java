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

import static org.impalaframework.module.holder.SharedModuleDefinitionSources.newTest1;
import static org.impalaframework.module.holder.SharedModuleDefinitionSources.newTest2;

import java.io.File;
import java.util.Collections;
import java.util.Map;

import junit.framework.TestCase;

import org.impalaframework.classloader.CustomClassLoaderFactory;
import org.impalaframework.exception.NoServiceException;
import org.impalaframework.file.FileMonitor;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.holder.DefaultModuleStateHolder;
import org.impalaframework.module.holder.ModuleClassLoaderRegistry;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.impalaframework.module.modification.StrictModificationExtractor;
import org.impalaframework.module.runtime.DefaultModuleRuntimeManager;
import org.impalaframework.module.spi.Application;
import org.impalaframework.module.spi.ApplicationManager;
import org.impalaframework.module.spi.ModificationExtractor;
import org.impalaframework.module.spi.ModuleRuntime;
import org.impalaframework.module.spi.TestApplicationManager;
import org.impalaframework.module.spi.Transition;
import org.impalaframework.module.spi.TransitionSet;
import org.impalaframework.module.transition.DefaultTransitionManager;
import org.impalaframework.module.transition.LoadTransitionProcessor;
import org.impalaframework.module.transition.TransitionProcessorRegistry;
import org.impalaframework.module.transition.UnloadTransitionProcessor;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.resolver.StandaloneModuleLocationResolver;
import org.impalaframework.service.registry.internal.DelegatingServiceRegistry;
import org.impalaframework.spring.module.loader.ApplicationModuleLoader;
import org.impalaframework.spring.module.loader.DefaultApplicationContextLoader;
import org.impalaframework.spring.module.loader.DelegatingContextLoaderRegistry;
import org.springframework.context.ConfigurableApplicationContext;

public class TransitionManagerTest extends TestCase {

    public void testProcessTransitions() {
        
        DefaultModuleStateHolder moduleStateHolder = new DefaultModuleStateHolder();
        DefaultTransitionManager transitionManager = new DefaultTransitionManager();
        ModuleLoaderRegistry registry = new ModuleLoaderRegistry();
        ModuleLocationResolver resolver = new StandaloneModuleLocationResolver();

        CustomClassLoaderFactory classLoaderFactory = new CustomClassLoaderFactory();
        classLoaderFactory.setModuleLocationResolver(resolver);
        
        ApplicationModuleLoader rootModuleLoader = new ApplicationModuleLoader();
        
        registry.addItem("spring-"+ModuleTypes.ROOT, rootModuleLoader);
        ApplicationModuleLoader applicationModuleLoader = new ApplicationModuleLoader();
        
        registry.addItem("spring-"+ModuleTypes.APPLICATION, applicationModuleLoader);
        DefaultApplicationContextLoader contextLoader = new DefaultApplicationContextLoader();
        contextLoader.setModuleLoaderRegistry(registry);
        contextLoader.setDelegatingContextLoaderRegistry(new DelegatingContextLoaderRegistry());
        
        TransitionProcessorRegistry transitionProcessors = new TransitionProcessorRegistry();
        LoadTransitionProcessor loadTransitionProcessor = new LoadTransitionProcessor();
        SpringModuleRuntime moduleRuntime = new SpringModuleRuntime();
        moduleRuntime.setApplicationContextLoader(contextLoader);
        moduleRuntime.setClassLoaderFactory(classLoaderFactory);
        
        ModuleRuntime springModuleRuntime = moduleRuntime;
        Map<String, ModuleRuntime> moduleRuntimes = Collections.singletonMap("spring", springModuleRuntime);
        DefaultModuleRuntimeManager manager = new DefaultModuleRuntimeManager();
        manager.setModuleRuntimes(moduleRuntimes);
        
        loadTransitionProcessor.setModuleRuntimeManager(manager);
        UnloadTransitionProcessor unloadTransitionProcessor = new UnloadTransitionProcessor();
        transitionProcessors.addItem(Transition.UNLOADED_TO_LOADED, loadTransitionProcessor);
        transitionProcessors.addItem(Transition.LOADED_TO_UNLOADED, unloadTransitionProcessor);
        transitionManager.setTransitionProcessorRegistry(transitionProcessors);     

        ApplicationManager applicationManager = TestApplicationManager.newApplicationManager(new ModuleClassLoaderRegistry(), moduleStateHolder, new DelegatingServiceRegistry());
        Application application = applicationManager.getCurrentApplication();
        
        RootModuleDefinition test1Definition = newTest1().getModuleDefinition();
        ModificationExtractor calculator = new StrictModificationExtractor();
        TransitionSet transitions = calculator.getTransitions(application, null, test1Definition);
        
        transitionManager.processTransitions(moduleStateHolder, application, transitions);

        ConfigurableApplicationContext context = SpringModuleUtils.getRootSpringContext(moduleStateHolder);
        service((FileMonitor) context.getBean("bean1"));
        noService((FileMonitor) context.getBean("bean3"));

        RootModuleDefinition test2Definition = newTest2().getModuleDefinition();
        transitions = calculator.getTransitions(application, test1Definition, test2Definition);
        transitionManager.processTransitions(moduleStateHolder, application, transitions);

        context = SpringModuleUtils.getRootSpringContext(moduleStateHolder);
        service((FileMonitor) context.getBean("bean1"));
        //now we got bean3
        service((FileMonitor) context.getBean("bean3"));
        
        moduleStateHolder.setExternalRootModuleName("sample-module1");
        SpringRuntimeModule module1 = (SpringRuntimeModule) moduleStateHolder.getModule("sample-module1");
        ConfigurableApplicationContext newRootModule = SpringModuleUtils.getRootSpringContext(moduleStateHolder);
        assertSame(module1.getApplicationContext(), newRootModule);

    }

    private void noService(FileMonitor f) {
        try {
            f.lastModified((File) null);
            fail();
        }
        catch (NoServiceException e) {
        }
    }

    private void service(FileMonitor f) {
        f.lastModified((File) null);
    }
    
}
