package org.impalaframework.spring.plugin;

import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

public interface PluginLoader {
	ClassLoader newClassLoader(ApplicationContextSet contextSet, PluginSpec pluginSpec, ApplicationContext parent);
	Resource[] getClassLocations(ApplicationContextSet contextSet, PluginSpec pluginSpec);
	Resource[] getSpringConfigResources(ApplicationContextSet contextSet, PluginSpec pluginSpec, ClassLoader classLoader);
	ConfigurableApplicationContext newApplicationContext(ApplicationContext parent, ClassLoader classLoader);
	BeanDefinitionReader newBeanDefinitionReader(ConfigurableApplicationContext context, PluginSpec pluginSpec);
	void afterRefresh(ConfigurableApplicationContext context, BeanDefinitionReader reader, PluginSpec plugin);
}
