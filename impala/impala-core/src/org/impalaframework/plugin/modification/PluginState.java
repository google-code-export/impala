package org.impalaframework.plugin.modification;

enum PluginState {

	LOADED("LOADED"), UNLOADED("UNLOADED");

	private String name;

	private PluginState(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
