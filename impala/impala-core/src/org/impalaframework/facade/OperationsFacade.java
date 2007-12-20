package org.impalaframework.facade;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.springframework.context.ApplicationContext;

public interface OperationsFacade {

	void init(ModuleDefinitionSource source);

	boolean reload(String plugin);

	boolean reload(ModuleDefinitionSource source, String plugin);

	String reloadLike(ModuleDefinitionSource source, String plugin);

	String reloadLike(String plugin);

	void reloadAll();

	void unloadParent();

	boolean remove(String plugin);

	void addPlugin(final ModuleDefinition moduleDefinition);

	boolean hasModule(String plugin);

	String findLike(ModuleDefinitionSource source, String plugin);

	ApplicationContext getRootContext();

	<T extends Object> T getBean(String beanName, Class<T> t);

	<T extends Object> T getPluginBean(String pluginName, String beanName, Class<T> t);
	
	RootModuleDefinition getRootModuleDefinition();

}