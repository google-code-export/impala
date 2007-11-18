package org.impalaframework.plugin.loader;

import org.impalaframework.plugin.spec.PluginSpec;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

public interface PluginLoader {
	ClassLoader newClassLoader(PluginSpec pluginSpec, ApplicationContext parent);
	Resource[] getClassLocations(PluginSpec pluginSpec);
	Resource[] getSpringConfigResources(PluginSpec pluginSpec, ClassLoader classLoader);
	ConfigurableApplicationContext newApplicationContext(ApplicationContext parent, PluginSpec pluginSpec, ClassLoader classLoader);
	BeanDefinitionReader newBeanDefinitionReader(ConfigurableApplicationContext context, PluginSpec pluginSpec);
	void afterRefresh(ConfigurableApplicationContext context, PluginSpec plugin);
}
