package org.impalaframework.module.loader;

import org.impalaframework.module.definition.ModuleDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

public interface ModuleLoader {
	ClassLoader newClassLoader(ModuleDefinition moduleDefinition, ApplicationContext parent);
	Resource[] getClassLocations(ModuleDefinition moduleDefinition);
	Resource[] getSpringConfigResources(ModuleDefinition moduleDefinition, ClassLoader classLoader);
	ConfigurableApplicationContext newApplicationContext(ApplicationContext parent, ModuleDefinition moduleDefinition, ClassLoader classLoader);
	BeanDefinitionReader newBeanDefinitionReader(ConfigurableApplicationContext context, ModuleDefinition moduleDefinition);
	void afterRefresh(ConfigurableApplicationContext context, ModuleDefinition plugin);
}
