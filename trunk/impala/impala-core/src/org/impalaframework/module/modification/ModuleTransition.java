package org.impalaframework.module.modification;

import org.springframework.util.Assert;

public enum ModuleTransition {

	LOADED_TO_UNLOADED(ModuleState.LOADED, ModuleState.UNLOADED), 
	UNLOADED_TO_LOADED(ModuleState.UNLOADED, ModuleState.LOADED), 
	STALE_TO_LOADED(ModuleState.STALE, ModuleState.LOADED), //FIXME wire in processor for this
	CONTEXT_LOCATIONS_ADDED(ModuleState.LOADED, ModuleState.LOADED);
	
	private Enum beforeState;

	private Enum afterState;

	private ModuleTransition(Enum<ModuleState> beforeState, Enum<ModuleState> afterState) {
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
