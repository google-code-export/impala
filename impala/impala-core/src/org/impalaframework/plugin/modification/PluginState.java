package org.impalaframework.plugin.modification;

enum PluginState {

	//FIXME add STALE state
	LOADED("LOADED"), UNLOADED("UNLOADED");

	private String name;

	private PluginState(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
