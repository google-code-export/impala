package org.impalaframework.plugin.spec.modification;

import org.springframework.util.Assert;

public class PluginTransition {

	private Enum beforeState;

	private Enum afterState;

	public PluginTransition(Enum<PluginState> beforeState, Enum<PluginState> afterState) {
		super();
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
