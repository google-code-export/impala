package org.impalaframework.module.operation;

import org.impalaframework.module.modification.ModificationExtractorType;

public class IncrementalUpdateRootModuleOperation extends ReloadRootModuleOperation {

	protected IncrementalUpdateRootModuleOperation() {
		super();
	}

	protected ModificationExtractorType getModificationExtractorType() {
		return ModificationExtractorType.STICKY;
	}

}
