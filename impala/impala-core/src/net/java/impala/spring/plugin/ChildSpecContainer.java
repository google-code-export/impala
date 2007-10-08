package net.java.impala.spring.plugin;

public interface ChildSpecContainer {

	String[] getPluginNames();

	PluginSpec getPlugin(String pluginName);

	boolean hasPlugin(String pluginName);

	PluginSpec[] getPlugins();

}