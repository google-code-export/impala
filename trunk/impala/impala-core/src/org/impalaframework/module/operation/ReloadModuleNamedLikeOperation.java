package org.impalaframework.module.operation;

import java.util.HashMap;
import java.util.Map;

import org.impalaframework.module.bootstrap.ModuleManagementFactory;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.holder.ModuleStateHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class ReloadModuleNamedLikeOperation extends BaseModuleOperation {

	final Logger logger = LoggerFactory.getLogger(ReloadNamedModuleOperation.class);

	protected ReloadModuleNamedLikeOperation(final ModuleManagementFactory factory) {
		super(factory);
	}

	public ModuleOperationResult execute(ModuleOperationInput moduleOperationInput) {

		Assert.notNull(moduleOperationInput, "moduleOperationInput cannot be null");
		String moduleToReload = moduleOperationInput.getModuleName();
		Assert.notNull(moduleToReload,
				"moduleName is required as it specifies the name used to match the module to reload in "
						+ this.getClass().getName());
		
		ModuleStateHolder moduleStateHolder = getFactory().getModuleStateHolder();
		RootModuleDefinition newDefinition = moduleStateHolder.cloneRootModuleDefinition();

		ModuleDefinition found = newDefinition.findChildDefinition(moduleToReload, false);

		if (found != null) {

			String foundModuleName = found.getName();
			
			ModuleOperation operation = getFactory().getModuleOperationRegistry().getOperation(
					ModuleOperationConstants.ReloadNamedModuleOperation);
			operation.execute(new ModuleOperationInput(null, null, foundModuleName));

			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("moduleName", foundModuleName);
			return new ModuleOperationResult(true, resultMap);
		}
		else {
			return ModuleOperationResult.FALSE;
		}
	}
}
