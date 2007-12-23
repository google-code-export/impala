package org.impalaframework.module.operation;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleDefinitionSource;

public class ModuleOperationInput {

	// the external module definition source to be operated on. Should not be
	// available via the
	// current ModuleStateHolder instance
	private final ModuleDefinitionSource moduleDefinitionSource;

	// the external module definition to be operated on. Should not be available
	// via the
	// current ModuleStateHolder instance
	private final ModuleDefinition moduleDefinition;

	// the module to be operated on
	private final String moduleName;

	public ModuleOperationInput(ModuleDefinitionSource moduleDefinitionSource, ModuleDefinition moduleDefinition,
			String moduleName) {
		super();
		this.moduleDefinitionSource = moduleDefinitionSource;
		this.moduleDefinition = moduleDefinition;
		this.moduleName = moduleName;
	}

	public ModuleDefinition getModuleDefinition() {
		return moduleDefinition;
	}

	public ModuleDefinitionSource getModuleDefinitionSource() {
		return moduleDefinitionSource;
	}

	public String getModuleName() {
		return moduleName;
	}

}
