package org.impalaframework.module.modification;

enum PluginState {

	LOADED("LOADED"), UNLOADED("UNLOADED"), STALE("STALE");

	private String name;

	private PluginState(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
