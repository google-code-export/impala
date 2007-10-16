package net.java.impala.spring.monitor;

public class PluginModificationInfo {
	private String pluginName;

	public PluginModificationInfo(String pluginName) {
		super();
		this.pluginName = pluginName;
	}

	public String getPluginName() {
		return pluginName;
	}

	public String toString() {
		return this.getClass().getName() + ": " + pluginName;
	}
	
}
