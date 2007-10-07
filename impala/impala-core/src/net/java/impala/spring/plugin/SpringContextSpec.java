package net.java.impala.spring.plugin;

public interface SpringContextSpec {

	public ParentSpec getParentSpec();
	
	public String[] getParentContextLocations();

	public String[] getPluginNames();
	
	public PluginSpec[] getPlugins();

	public PluginSpec getPlugin(String pluginName);

	public boolean hasPlugin(String pluginName);


}