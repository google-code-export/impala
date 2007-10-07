package net.java.impala.spring.util;

import net.java.impala.spring.plugin.ApplicationContextSet;
import net.java.impala.spring.plugin.Plugin;
import net.java.impala.spring.plugin.PluginSpec;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

public interface ApplicationContextLoader {

	ClassLoader newParentClassLoader();

	ApplicationContextSet loadParentContext(PluginSpec pluginSpec, ClassLoader classLoader);

	ConfigurableApplicationContext addApplicationPlugin(ApplicationContext parent, Plugin plugin);

	ConfigurableApplicationContext loadContextFromResource(ApplicationContext parent, Resource resource,
			ClassLoader classLoader);

}