package org.impalaframework.module.transition;

import static org.impalaframework.module.transition.SharedSpecProviders.newTest1;
import static org.impalaframework.module.transition.SharedSpecProviders.newTest2;

import java.io.File;

import junit.framework.TestCase;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.file.monitor.FileMonitor;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.loader.ApplicationModuleLoader;
import org.impalaframework.module.loader.DefaultApplicationContextLoader;
import org.impalaframework.module.loader.RootModuleLoader;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.impalaframework.module.modification.ModuleModificationCalculator;
import org.impalaframework.module.modification.ModuleTransition;
import org.impalaframework.module.modification.ModuleTransitionSet;
import org.impalaframework.module.modification.StrictModuleModificationCalculator;
import org.impalaframework.module.transition.DefaultPluginStateManager;
import org.impalaframework.module.transition.LoadTransitionProcessor;
import org.impalaframework.module.transition.TransitionProcessorRegistry;
import org.impalaframework.module.transition.UnloadTransitionProcessor;
import org.impalaframework.resolver.ClassLocationResolver;
import org.impalaframework.resolver.PropertyClassLocationResolver;
import org.springframework.context.ConfigurableApplicationContext;

public class PluginStateManagerTest extends TestCase {

	public void setUp() {
		System.setProperty("impala.parent.project", "impala");
	}

	public void tearDown() {
		System.clearProperty("impala.parent.project");
	}

	public void testProcessTransitions() {
		
		DefaultPluginStateManager tm = new DefaultPluginStateManager();
		ModuleLoaderRegistry registry = new ModuleLoaderRegistry();
		ClassLocationResolver resolver = new PropertyClassLocationResolver();
		registry.setPluginLoader(ModuleTypes.ROOT, new RootModuleLoader(resolver));
		registry.setPluginLoader(ModuleTypes.APPLICATION, new ApplicationModuleLoader(resolver));
		DefaultApplicationContextLoader contextLoader = new DefaultApplicationContextLoader(registry);
		
		TransitionProcessorRegistry transitionProcessors = new TransitionProcessorRegistry();
		LoadTransitionProcessor loadTransitionProcessor = new LoadTransitionProcessor(contextLoader);
		UnloadTransitionProcessor unloadTransitionProcessor = new UnloadTransitionProcessor();
		transitionProcessors.addTransitionProcessor(ModuleTransition.UNLOADED_TO_LOADED, loadTransitionProcessor);
		transitionProcessors.addTransitionProcessor(ModuleTransition.LOADED_TO_UNLOADED, unloadTransitionProcessor);
		tm.setTransitionProcessorRegistry(transitionProcessors);		
		
		RootModuleDefinition test1Spec = newTest1().getModuleDefintion();
		ModuleModificationCalculator calculator = new StrictModuleModificationCalculator();
		ModuleTransitionSet transitions = calculator.getTransitions(null, test1Spec);
		tm.processTransitions(transitions);

		ConfigurableApplicationContext context = tm.getParentContext();
		service((FileMonitor) context.getBean("bean1"));
		noService((FileMonitor) context.getBean("bean3"));

		RootModuleDefinition test2Spec = newTest2().getModuleDefintion();
		transitions = calculator.getTransitions(test1Spec, test2Spec);
		tm.processTransitions(transitions);

		context = tm.getParentContext();
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
