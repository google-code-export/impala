package org.impalaframework.module.operation;

import org.impalaframework.module.holder.ModuleStateHolder;
import org.impalaframework.module.modification.ModificationExtractorRegistry;

public abstract class BaseModuleOperation implements ModuleOperation {

	private ModificationExtractorRegistry modificationExtractorRegistry;

	private ModuleStateHolder moduleStateHolder;

	protected BaseModuleOperation() {
		super();
	}

	protected ModificationExtractorRegistry getModificationExtractorRegistry() {
		return modificationExtractorRegistry;
	}

	protected ModuleStateHolder getModuleStateHolder() {
		return moduleStateHolder;
	}

	public void setModificationExtractorRegistry(ModificationExtractorRegistry modificationExtractorRegistry) {
		this.modificationExtractorRegistry = modificationExtractorRegistry;
	}

	public void setModuleStateHolder(ModuleStateHolder moduleStateHolder) {
		this.moduleStateHolder = moduleStateHolder;
	}

}
