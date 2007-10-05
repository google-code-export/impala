package net.java.impala.spring.plugin;

public interface PluginSpec {

	public String[] getParentContextLocations();

	public String[] getPluginNames();

	public boolean hasPlugin(String pluginName);

}