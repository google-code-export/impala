package org.impalaframework.module.definition;

public enum ModuleState {

	LOADED("LOADED"), UNLOADED("UNLOADED"), STALE("STALE");

	private String name;

	private ModuleState(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
