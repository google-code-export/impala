package net.java.impala.spring.plugin;

import org.springframework.core.io.Resource;

public interface PluginLoader {
	ClassLoader getClassLoader(ApplicationContextSet contextSet, PluginSpec pluginSpec);
	Resource[] getClassLocations(ApplicationContextSet contextSet, PluginSpec pluginSpec);
	Resource[] getSpringConfigResources(ApplicationContextSet contextSet, PluginSpec pluginSpec);
}
