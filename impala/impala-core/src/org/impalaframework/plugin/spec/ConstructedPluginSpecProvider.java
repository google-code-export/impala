package org.impalaframework.plugin.spec;

public class ConstructedPluginSpecProvider implements PluginSpecProvider {

	private ParentSpec pluginSpec;
	
	public ConstructedPluginSpecProvider(ParentSpec pluginSpec) {
		super();
		//FIXME assert, test
		this.pluginSpec = pluginSpec;
	}

	public ParentSpec getPluginSpec() {
		return pluginSpec;
	}

}
