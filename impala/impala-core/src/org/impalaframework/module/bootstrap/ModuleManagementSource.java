package org.impalaframework.module.bootstrap;

import org.impalaframework.module.holder.ModuleStateHolder;
import org.impalaframework.module.loader.ApplicationContextLoader;
import org.impalaframework.module.loader.ModuleLoaderRegistry;
import org.impalaframework.module.modification.ModificationExtractorRegistry;
import org.impalaframework.module.transition.TransitionProcessorRegistry;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.springframework.beans.factory.BeanFactory;

public interface ModuleManagementSource extends BeanFactory {
	
	ModuleLocationResolver getClassLocationResolver();
	ModuleLoaderRegistry getPluginLoaderRegistry();
	ApplicationContextLoader getApplicationContextLoader();
	ModificationExtractorRegistry getModificationExtractorRegistry();
	TransitionProcessorRegistry getTransitionProcessorRegistry();
	ModuleStateHolder getModuleStateHolder();
	void close();
	
}
