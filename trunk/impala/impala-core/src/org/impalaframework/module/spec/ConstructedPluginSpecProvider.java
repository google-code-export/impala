package org.impalaframework.module.spec;


public class ConstructedPluginSpecProvider implements PluginSpecProvider {

	private ParentSpec pluginSpec;
	
	public ConstructedPluginSpecProvider(ParentSpec pluginSpec) {
		super();
		this.pluginSpec = pluginSpec;
	}

	public ParentSpec getPluginSpec() {
		return pluginSpec;
	}

}
