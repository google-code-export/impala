package org.impalaframework.plugin.operation;

import org.impalaframework.plugin.bootstrap.ModuleManagementSource;
import org.impalaframework.plugin.modification.ModificationCalculationType;
import org.impalaframework.plugin.spec.PluginSpecProvider;

public class IncrementalReloadParentOperation extends ReloadParentOperation {

	public IncrementalReloadParentOperation(ModuleManagementSource factory, PluginSpecProvider pluginSpecBuilder) {
		super(factory, pluginSpecBuilder);
	}

	protected ModificationCalculationType getPluginModificationType() {
		//FIXME test
		return ModificationCalculationType.STICKY;
	}

}
