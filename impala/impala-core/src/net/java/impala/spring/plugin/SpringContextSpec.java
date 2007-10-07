package net.java.impala.spring.plugin;

public interface SpringContextSpec {

	public String[] getParentContextLocations();

	public String[] getPluginNames();
	
	public Plugin[] getPlugins();

	public Plugin getPlugin(String pluginName);

	public boolean hasPlugin(String pluginName);


}