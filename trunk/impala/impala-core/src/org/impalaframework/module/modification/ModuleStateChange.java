package org.impalaframework.module.modification;

import org.impalaframework.module.definition.ModuleDefinition;
import org.springframework.util.Assert;

public final class ModuleStateChange {
	
	private final Transition transition;

	private final ModuleDefinition moduleDefinition;

	public ModuleStateChange(Transition transition, ModuleDefinition moduleDefinition) {
		super();
		Assert.notNull(transition);
		Assert.notNull(moduleDefinition);
		this.transition = transition;
		this.moduleDefinition = moduleDefinition;
	}

	public ModuleDefinition getPluginSpec() {
		return moduleDefinition;
	}

	public Transition getTransition() {
		return transition;
	}

}
