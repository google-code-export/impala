package org.impalaframework.module.modification;

import org.impalaframework.module.spec.ModuleDefinition;
import org.springframework.util.Assert;

public final class ModuleStateChange {
	
	private final ModuleTransition transition;

	private final ModuleDefinition moduleDefinition;

	public ModuleStateChange(ModuleTransition transition, ModuleDefinition moduleDefinition) {
		super();
		Assert.notNull(transition);
		Assert.notNull(moduleDefinition);
		this.transition = transition;
		this.moduleDefinition = moduleDefinition;
	}

	public ModuleDefinition getPluginSpec() {
		return moduleDefinition;
	}

	public ModuleTransition getTransition() {
		return transition;
	}

}
