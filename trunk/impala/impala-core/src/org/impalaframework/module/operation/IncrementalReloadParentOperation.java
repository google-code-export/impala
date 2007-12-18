package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.modification.ModificationCalculationType;

public class IncrementalReloadParentOperation extends ReloadParentOperation {

	public IncrementalReloadParentOperation(ModuleManagementSource factory, ModuleDefinitionSource pluginSpecBuilder) {
		super(factory, pluginSpecBuilder);
	}

	protected ModificationCalculationType getPluginModificationType() {
		//FIXME test
		return ModificationCalculationType.STICKY;
	}

}
