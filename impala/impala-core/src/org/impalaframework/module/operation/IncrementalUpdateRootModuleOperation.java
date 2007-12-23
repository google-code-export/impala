package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementFactory;
import org.impalaframework.module.modification.ModificationExtractorType;

public class IncrementalUpdateRootModuleOperation extends ReloadRootModuleOperation {

	protected IncrementalUpdateRootModuleOperation(ModuleManagementFactory factory) {
		super(factory);
	}

	protected ModificationExtractorType getPluginModificationType() {
		//FIXME test
		return ModificationExtractorType.STICKY;
	}

}
