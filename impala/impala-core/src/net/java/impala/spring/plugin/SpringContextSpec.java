package net.java.impala.spring.plugin;

public interface SpringContextSpec {

	public ParentSpec getParentSpec();
	
	public PluginSpec[] getPlugins();

	public String[] getPluginNames();

	public PluginSpec getPlugin(String pluginName);

	public boolean hasPlugin(String pluginName);


}