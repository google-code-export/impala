package net.java.impala.spring.plugin;

import java.util.Collection;

public interface ChildSpecContainer {

	Collection<String> getPluginNames();

	PluginSpec getPlugin(String pluginName);

	boolean hasPlugin(String pluginName);

	Collection<PluginSpec> getPlugins();

	void add(PluginSpec pluginSpec);
	
	PluginSpec remove(String pluginName);
	
	public int hashCode();
	
	public boolean equals(Object obj);

}