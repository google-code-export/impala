package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementFactory;
import org.impalaframework.module.holder.ModuleStateHolder;
import org.impalaframework.module.modification.ModificationExtractorRegistry;
import org.springframework.util.Assert;

public abstract class BaseModuleOperation implements ModuleOperation {
	
	private final ModuleManagementFactory factory;

	protected BaseModuleOperation(final ModuleManagementFactory factory) {
		super();
		Assert.notNull(factory);
		this.factory = factory;
	}

	protected ModuleManagementFactory getFactory() {
		return factory;
	}

	protected ModificationExtractorRegistry getModificationExtractorRegistry() {
		return getFactory().getModificationExtractorRegistry();
	}

	protected ModuleStateHolder getModuleStateHolder() {
		return getFactory().getModuleStateHolder();
	}
}
