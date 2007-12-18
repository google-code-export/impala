package org.impalaframework.module.bootstrap;

import org.impalaframework.module.loader.ApplicationContextLoader;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.impalaframework.module.manager.ModuleStateManager;
import org.impalaframework.module.modification.ModuleModificationExtractorRegistry;
import org.impalaframework.module.transition.TransitionProcessorRegistry;
import org.impalaframework.resolver.ClassLocationResolver;
import org.springframework.beans.factory.BeanFactory;

public interface ModuleManagementSource extends BeanFactory {
	
	ClassLocationResolver getClassLocationResolver();
	ModuleLoaderRegistry getPluginLoaderRegistry();
	ApplicationContextLoader getApplicationContextLoader();
	ModuleModificationExtractorRegistry getPluginModificationCalculatorRegistry();
	TransitionProcessorRegistry getTransitionProcessorRegistry();
	ModuleStateManager getPluginStateManager();
	void close();
	
}
