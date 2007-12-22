package org.impalaframework.facade;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.springframework.context.ApplicationContext;

public interface OperationsFacade {

	void init(ModuleDefinitionSource source);

	boolean reload(String moduleName);

	boolean reload(ModuleDefinitionSource source, String moduleName);

	String reloadLike(ModuleDefinitionSource source, String likeModuleName);

	String reloadLike(String likeModuleName);

	void reloadAll();

	void unloadParent();

	boolean remove(String moduleName);

	void addPlugin(final ModuleDefinition moduleDefinition);

	boolean hasModule(String moduleName);

	String findLike(ModuleDefinitionSource source, String moduleName);

	ApplicationContext getRootContext();

	<T extends Object> T getBean(String beanName, Class<T> t);

	<T extends Object> T getPluginBean(String moduleName, String beanName, Class<T> type);
	
	RootModuleDefinition getRootModuleDefinition();

}