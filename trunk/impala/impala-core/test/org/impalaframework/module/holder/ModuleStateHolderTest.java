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

package org.impalaframework.module.holder;

import static org.impalaframework.module.holder.SharedModuleDefinitionSources.newTest1;
import static org.impalaframework.module.holder.SharedModuleDefinitionSources.newTest2;

import java.io.File;

import junit.framework.TestCase;

import org.impalaframework.classloader.ModuleClassLoaderFactory;
import org.impalaframework.exception.NoServiceException;
import org.impalaframework.file.FileMonitor;
import org.impalaframework.module.ModificationExtractor;
import org.impalaframework.module.Transition;
import org.impalaframework.module.TransitionSet;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.loader.ApplicationModuleLoader;
import org.impalaframework.module.loader.DefaultApplicationContextLoader;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.impalaframework.module.loader.RootModuleLoader;
import org.impalaframework.module.modification.StrictModificationExtractor;
import org.impalaframework.module.transition.LoadTransitionProcessor;
import org.impalaframework.module.transition.TransitionProcessorRegistry;
import org.impalaframework.module.transition.UnloadTransitionProcessor;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.resolver.StandaloneModuleLocationResolver;
import org.impalaframework.service.registry.internal.ServiceRegistryImpl;
import org.springframework.context.ConfigurableApplicationContext;

public class ModuleStateHolderTest extends TestCase {

	public void testProcessTransitions() {
		
		DefaultModuleStateHolder tm = new DefaultModuleStateHolder();
		ModuleLoaderRegistry registry = new ModuleLoaderRegistry();
		ModuleLocationResolver resolver = new StandaloneModuleLocationResolver();
		RootModuleLoader rootModuleLoader = new RootModuleLoader();
		rootModuleLoader.setModuleLocationResolver(resolver);
		rootModuleLoader.setClassLoaderFactory(new ModuleClassLoaderFactory());
		
		registry.setModuleLoader(ModuleTypes.ROOT, rootModuleLoader);
		ApplicationModuleLoader applicationModuleLoader = new ApplicationModuleLoader();
		applicationModuleLoader.setModuleLocationResolver(resolver);
		applicationModuleLoader.setClassLoaderFactory(new ModuleClassLoaderFactory());
		registry.setModuleLoader(ModuleTypes.APPLICATION, applicationModuleLoader);
		DefaultApplicationContextLoader contextLoader = new DefaultApplicationContextLoader();
		contextLoader.setModuleLoaderRegistry(registry);contextLoader.setServiceRegistry(new ServiceRegistryImpl());
		
		TransitionProcessorRegistry transitionProcessors = new TransitionProcessorRegistry();
		LoadTransitionProcessor loadTransitionProcessor = new LoadTransitionProcessor(contextLoader);
		UnloadTransitionProcessor unloadTransitionProcessor = new UnloadTransitionProcessor();
		transitionProcessors.addTransitionProcessor(Transition.UNLOADED_TO_LOADED, loadTransitionProcessor);
		transitionProcessors.addTransitionProcessor(Transition.LOADED_TO_UNLOADED, unloadTransitionProcessor);
		tm.setTransitionProcessorRegistry(transitionProcessors);		
		
		RootModuleDefinition test1Definition = newTest1().getModuleDefinition();
		ModificationExtractor calculator = new StrictModificationExtractor();
		TransitionSet transitions = calculator.getTransitions(null, test1Definition);
		tm.processTransitions(transitions);

		ConfigurableApplicationContext context = tm.getRootModuleContext();
		service((FileMonitor) context.getBean("bean1"));
		noService((FileMonitor) context.getBean("bean3"));

		RootModuleDefinition test2Definition = newTest2().getModuleDefinition();
		transitions = calculator.getTransitions(test1Definition, test2Definition);
		tm.processTransitions(transitions);

		context = tm.getRootModuleContext();
		service((FileMonitor) context.getBean("bean1"));
		//now we got bean3
		service((FileMonitor) context.getBean("bean3"));

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
