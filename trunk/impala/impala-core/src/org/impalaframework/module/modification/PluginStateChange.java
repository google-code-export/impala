package org.impalaframework.module.modification;

import org.impalaframework.module.spec.ModuleDefinition;
import org.springframework.util.Assert;

public final class PluginStateChange {
	
	private final PluginTransition transition;

	private final ModuleDefinition moduleDefinition;

	public PluginStateChange(PluginTransition transition, ModuleDefinition moduleDefinition) {
		super();
		Assert.notNull(transition);
		Assert.notNull(moduleDefinition);
		this.transition = transition;
		this.moduleDefinition = moduleDefinition;
	}

	public ModuleDefinition getPluginSpec() {
		return moduleDefinition;
	}

	public PluginTransition getTransition() {
		return transition;
	}

}
