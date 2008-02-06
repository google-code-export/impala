package org.impalaframework.facade;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.springframework.context.ApplicationContext;

public interface OperationsFacade {

	void init(ModuleDefinitionSource source);

	boolean reload(String moduleName);

	String reloadLike(String likeModuleName);

	void reloadRootModule();

	void unloadRootModule();

	boolean remove(String moduleName);

	void addModule(final ModuleDefinition moduleDefinition);

	boolean hasModule(String moduleName);

	String findLike(String moduleName);

	ApplicationContext getRootContext();

	<T extends Object> T getBean(String beanName, Class<T> t);

	<T extends Object> T getModuleBean(String moduleName, String beanName, Class<T> type);
	
	RootModuleDefinition getRootModuleDefinition();

}