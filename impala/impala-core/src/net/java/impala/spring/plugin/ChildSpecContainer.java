package net.java.impala.spring.plugin;

import java.util.Collection;

public interface ChildSpecContainer {

	Collection<String> getPluginNames();

	PluginSpec getPlugin(String pluginName);

	boolean hasPlugin(String pluginName);

	Collection<PluginSpec> getPlugins();

}