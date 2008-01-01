package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementFactory;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.ModuleStateHolder;
import org.impalaframework.module.modification.ModificationExtractor;
import org.impalaframework.module.modification.ModificationExtractorRegistry;
import org.impalaframework.module.modification.ModificationExtractorType;
import org.impalaframework.module.modification.ModuleState;
import org.impalaframework.module.modification.TransitionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class ReloadNamedModuleOperation  extends BaseModuleOperation {

	final Logger logger = LoggerFactory.getLogger(ReloadNamedModuleOperation.class);

	protected ReloadNamedModuleOperation(final ModuleManagementFactory factory) {
		super(factory);
	}

	public ModuleOperationResult execute(ModuleOperationInput moduleOperationInput) {

		Assert.notNull(moduleOperationInput, "moduleOperationInput cannot be null");
		String moduleToReload = moduleOperationInput.getModuleName();
		Assert.notNull(moduleToReload, "moduleName is required as it specifies the name of the module to reload in "
				+ this.getClass().getName());

		ModuleStateHolder moduleStateHolder = getFactory().getModuleStateHolder();
		RootModuleDefinition oldRootDefinition = moduleStateHolder.getRootModuleDefinition();
		RootModuleDefinition newRootDefinition = moduleStateHolder.cloneRootModuleDefinition();

		ModificationExtractorRegistry modificationExtractor = getFactory().getModificationExtractorRegistry();
		ModificationExtractor calculator = modificationExtractor
				.getModificationExtractor(ModificationExtractorType.STRICT);

		ModuleDefinition childDefinition = newRootDefinition.findChildDefinition(moduleToReload, true);

		if (childDefinition != null) {
			childDefinition.setState(ModuleState.STALE);

			TransitionSet transitions = calculator.getTransitions(oldRootDefinition, newRootDefinition);
			moduleStateHolder.processTransitions(transitions);

			boolean result = !transitions.getModuleTransitions().isEmpty();
			return result ? ModuleOperationResult.TRUE : ModuleOperationResult.FALSE;
		}
		
		return ModuleOperationResult.FALSE;
	}
}
