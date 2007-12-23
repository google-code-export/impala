package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementFactory;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.modification.ModificationExtractorType;

public class IncrementalUpdateRootModuleOperation extends ReloadRootModuleOperation {

	public IncrementalUpdateRootModuleOperation(ModuleManagementFactory factory, ModuleDefinitionSource source) {
		super(factory, source);
	}

	protected ModificationExtractorType getPluginModificationType() {
		//FIXME test
		return ModificationExtractorType.STICKY;
	}

}
