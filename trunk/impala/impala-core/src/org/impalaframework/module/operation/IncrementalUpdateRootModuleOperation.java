package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.modification.ModificationExtractorType;

public class IncrementalUpdateRootModuleOperation extends ReloadRootModuleOperation {

	public IncrementalUpdateRootModuleOperation(ModuleManagementSource factory, ModuleDefinitionSource source) {
		super(factory, source);
	}

	protected ModificationExtractorType getPluginModificationType() {
		//FIXME test
		return ModificationExtractorType.STICKY;
	}

}
