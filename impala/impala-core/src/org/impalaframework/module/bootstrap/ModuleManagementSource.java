package org.impalaframework.module.bootstrap;

import org.impalaframework.module.loader.ApplicationContextLoader;
import org.impalaframework.module.loader.PluginLoaderRegistry;
import org.impalaframework.module.modification.PluginModificationCalculatorRegistry;
import org.impalaframework.module.transition.PluginStateManager;
import org.impalaframework.module.transition.TransitionProcessorRegistry;
import org.impalaframework.resolver.ClassLocationResolver;
import org.springframework.beans.factory.BeanFactory;

public interface ModuleManagementSource extends BeanFactory {
	
	ClassLocationResolver getClassLocationResolver();
	PluginLoaderRegistry getPluginLoaderRegistry();
	ApplicationContextLoader getApplicationContextLoader();
	PluginModificationCalculatorRegistry getPluginModificationCalculatorRegistry();
	TransitionProcessorRegistry getTransitionProcessorRegistry();
	PluginStateManager getPluginStateManager();
	void close();
	
}
