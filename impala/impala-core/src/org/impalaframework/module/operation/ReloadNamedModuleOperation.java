package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementFactory;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.ModuleStateHolder;
import org.impalaframework.module.modification.ModificationExtractor;
import org.impalaframework.module.modification.ModificationExtractorRegistry;
import org.impalaframework.module.modification.ModificationExtractorType;
import org.impalaframework.module.modification.TransitionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class ReloadNamedModuleOperation implements ModuleOperation {

	final Logger logger = LoggerFactory.getLogger(ReloadNamedModuleOperation.class);

	private final ModuleManagementFactory factory;

	protected ReloadNamedModuleOperation(final ModuleManagementFactory factory) {
		super();
		Assert.notNull(factory);

		this.factory = factory;
	}

	public ModuleOperationResult execute(ModuleOperationInput moduleOperationInput) {

		Assert.notNull(moduleOperationInput, "moduleOperationInput cannot be null");
		String moduleToReload = moduleOperationInput.getModuleName();
		Assert.notNull(moduleToReload, "moduleName is required as it specifies the name of the module to reload in " + this.getClass().getName());
		
		ModuleStateHolder moduleStateHolder = factory.getModuleStateHolder();
		RootModuleDefinition oldRootDefinition = moduleStateHolder.getRootModuleDefinition();
		RootModuleDefinition newRootDefinition = moduleStateHolder.cloneRootModuleDefinition();

		ModificationExtractorRegistry modificationExtractor = factory.getModificationExtractorRegistry();
		ModificationExtractor calculator = modificationExtractor
				.getModificationExtractor(ModificationExtractorType.STRICT);
		TransitionSet transitions = calculator.reload(oldRootDefinition, newRootDefinition, moduleToReload);
		moduleStateHolder.processTransitions(transitions);

		boolean result = !transitions.getModuleTransitions().isEmpty();
		return result ? ModuleOperationResult.TRUE : ModuleOperationResult.FALSE;
	}

	protected ModuleManagementFactory getFactory() {
		return factory;
	}
}
