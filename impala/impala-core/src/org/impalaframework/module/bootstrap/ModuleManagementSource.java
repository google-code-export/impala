package org.impalaframework.module.bootstrap;

import org.impalaframework.module.loader.ApplicationContextLoader;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.impalaframework.module.modification.ModuleModificationCalculatorRegistry;
import org.impalaframework.module.transition.PluginStateManager;
import org.impalaframework.module.transition.TransitionProcessorRegistry;
import org.impalaframework.resolver.ClassLocationResolver;
import org.springframework.beans.factory.BeanFactory;

public interface ModuleManagementSource extends BeanFactory {
	
	ClassLocationResolver getClassLocationResolver();
	ModuleLoaderRegistry getPluginLoaderRegistry();
	ApplicationContextLoader getApplicationContextLoader();
	ModuleModificationCalculatorRegistry getPluginModificationCalculatorRegistry();
	TransitionProcessorRegistry getTransitionProcessorRegistry();
	PluginStateManager getPluginStateManager();
	void close();
	
}
