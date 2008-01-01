package org.impalaframework.module.modification;

import org.impalaframework.module.definition.ModuleState;
import org.springframework.util.Assert;

public enum Transition {

	LOADED_TO_UNLOADED(ModuleState.LOADED, ModuleState.UNLOADED), 
	UNLOADED_TO_LOADED(ModuleState.UNLOADED, ModuleState.LOADED),
	//FIXME what to do about this?
	STALE_TO_UNLOADED(ModuleState.STALE, ModuleState.UNLOADED), 
	CONTEXT_LOCATIONS_ADDED(ModuleState.LOADED, ModuleState.LOADED);
	
	private Enum beforeState;

	private Enum afterState;

	private Transition(Enum<ModuleState> beforeState, Enum<ModuleState> afterState) {
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
