package org.impalaframework.module.operation;

import org.impalaframework.module.modification.ModificationExtractorType;

public class IncrementalUpdateRootModuleOperation extends ReloadRootModuleOperation {

	protected IncrementalUpdateRootModuleOperation() {
		super();
	}

	protected ModificationExtractorType getPluginModificationType() {
		return ModificationExtractorType.STICKY;
	}

}
