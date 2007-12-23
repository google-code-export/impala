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

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((moduleDefinition == null) ? 0 : moduleDefinition.hashCode());
		result = PRIME * result + ((moduleDefinitionSource == null) ? 0 : moduleDefinitionSource.hashCode());
		result = PRIME * result + ((moduleName == null) ? 0 : moduleName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ModuleOperationInput other = (ModuleOperationInput) obj;
		if (moduleDefinition == null) {
			if (other.moduleDefinition != null)
				return false;
		}
		else if (!moduleDefinition.equals(other.moduleDefinition))
			return false;
		if (moduleDefinitionSource == null) {
			if (other.moduleDefinitionSource != null)
				return false;
		}
		else if (!moduleDefinitionSource.equals(other.moduleDefinitionSource))
			return false;
		if (moduleName == null) {
			if (other.moduleName != null)
				return false;
		}
		else if (!moduleName.equals(other.moduleName))
			return false;
		return true;
	}
	
	

}
