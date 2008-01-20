package org.impalaframework.module.holder;

import static org.impalaframework.module.holder.SharedModuleDefinitionSources.newTest1;
import static org.impalaframework.module.holder.SharedModuleDefinitionSources.newTest2;

import java.io.File;

import junit.framework.TestCase;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.file.monitor.FileMonitor;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.DefaultModuleStateHolder;
import org.impalaframework.module.loader.ApplicationModuleLoader;
import org.impalaframework.module.loader.DefaultApplicationContextLoader;
import org.impalaframework.module.loader.RootModuleLoader;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.impalaframework.module.modification.ModificationExtractor;
import org.impalaframework.module.modification.Transition;
import org.impalaframework.module.modification.TransitionSet;
import org.impalaframework.module.modification.StrictModificationExtractor;
import org.impalaframework.module.transition.LoadTransitionProcessor;
import org.impalaframework.module.transition.TransitionProcessorRegistry;
import org.impalaframework.module.transition.UnloadTransitionProcessor;
import org.impalaframework.resolver.LocationConstants;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.resolver.PropertyModuleLocationResolver;
import org.springframework.context.ConfigurableApplicationContext;

public class ModuleStateHolderTest extends TestCase {

	public void setUp() {
		System.setProperty(LocationConstants.ROOT_PROJECTS_PROPERTY, "impala");
	}

	public void tearDown() {
		System.clearProperty(LocationConstants.ROOT_PROJECTS_PROPERTY);
	}

	public void testProcessTransitions() {
		
		DefaultModuleStateHolder tm = new DefaultModuleStateHolder();
		ModuleLoaderRegistry registry = new ModuleLoaderRegistry();
		ModuleLocationResolver resolver = new PropertyModuleLocationResolver();
		registry.setModuleLoader(ModuleTypes.ROOT, new RootModuleLoader(resolver));
		registry.setModuleLoader(ModuleTypes.APPLICATION, new ApplicationModuleLoader(resolver));
		DefaultApplicationContextLoader contextLoader = new DefaultApplicationContextLoader(registry);
		
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
