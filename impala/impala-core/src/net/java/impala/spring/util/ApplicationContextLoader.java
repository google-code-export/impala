package net.java.impala.spring.util;

import net.java.impala.spring.plugin.ApplicationContextSet;
import net.java.impala.spring.plugin.PluginSpec;
import net.java.impala.spring.plugin.SpringContextSpec;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public interface ApplicationContextLoader {

	ClassLoader newParentClassLoader();

	ApplicationContextSet loadParentContext(SpringContextSpec pluginSpec, ClassLoader classLoader);

	ConfigurableApplicationContext addApplicationPlugin(ApplicationContext parent, PluginSpec plugin);

}