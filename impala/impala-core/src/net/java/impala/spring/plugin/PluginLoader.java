package net.java.impala.spring.plugin;

import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.Resource;

public interface PluginLoader {
	ClassLoader newClassLoader(ApplicationContextSet contextSet, PluginSpec pluginSpec);
	Resource[] getClassLocations(ApplicationContextSet contextSet, PluginSpec pluginSpec);
	Resource[] getSpringConfigResources(ApplicationContextSet contextSet, PluginSpec pluginSpec, ClassLoader classLoader);
	GenericApplicationContext newApplicationContext(ApplicationContext parent, ClassLoader classLoader);
	BeanDefinitionReader newBeanDefinitionReader(GenericApplicationContext context);
}
