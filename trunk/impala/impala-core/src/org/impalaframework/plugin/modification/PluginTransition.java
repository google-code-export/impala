package org.impalaframework.plugin.modification;

import org.springframework.util.Assert;

public enum PluginTransition {

	LOADED_TO_UNLOADED(PluginState.LOADED, PluginState.UNLOADED), 
	UNLOADED_TO_LOADED(PluginState.UNLOADED, PluginState.LOADED), 
	CONTEXT_LOCATIONS_ADDED(PluginState.LOADED, PluginState.LOADED);
	
	private Enum beforeState;

	private Enum afterState;

	private PluginTransition(Enum<PluginState> beforeState, Enum<PluginState> afterState) {
		Assert.notNull(beforeState);
		Assert.notNull(afterState);
		this.beforeState = beforeState;
		this.afterState = afterState;
	}

	public Enum getAfterState() {
		return afterState;
	}

	public Enum getBeforeState() {
		return beforeState;
	}

}
