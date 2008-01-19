package org.impalaframework.module.modification;

import org.impalaframework.module.definition.ModuleState;
import org.springframework.util.Assert;

public enum Transition {

	LOADED_TO_UNLOADED(ModuleState.LOADED, ModuleState.UNLOADED), 
	UNLOADED_TO_LOADED(ModuleState.UNLOADED, ModuleState.LOADED),
	CONTEXT_LOCATIONS_ADDED(ModuleState.LOADED, ModuleState.LOADED);
	
	private Enum<ModuleState> beforeState;

	private Enum<ModuleState> afterState;

	private Transition(Enum<ModuleState> beforeState, Enum<ModuleState> afterState) {
		Assert.notNull(beforeState);
		Assert.notNull(afterState);
		this.beforeState = beforeState;
		this.afterState = afterState;
	}

	public Enum<ModuleState> getAfterState() {
		return afterState;
	}

	public Enum<ModuleState> getBeforeState() {
		return beforeState;
	}

}
