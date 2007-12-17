package org.impalaframework.module.operation;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.modification.ModificationCalculationType;
import org.impalaframework.module.spec.PluginSpecProvider;

public class IncrementalReloadParentOperation extends ReloadParentOperation {

	public IncrementalReloadParentOperation(ModuleManagementSource factory, PluginSpecProvider pluginSpecBuilder) {
		super(factory, pluginSpecBuilder);
	}

	protected ModificationCalculationType getPluginModificationType() {
		//FIXME test
		return ModificationCalculationType.STICKY;
	}

}
