package org.impalaframework.module.modification;

import org.impalaframework.module.builder.SingleStringPluginSpecBuilder;
import org.impalaframework.module.spec.RootModuleDefinition;
import org.impalaframework.module.spec.SimpleRootModuleDefinition;

public class PluginModificationTestUtils {

	static RootModuleDefinition spec(String contextString, String pluginString) {
		String[] locations = contextString.split(",");
		SingleStringPluginSpecBuilder builder = new SingleStringPluginSpecBuilder(new SimpleRootModuleDefinition(locations),
				pluginString);
		RootModuleDefinition rootModuleDefinition = builder.getPluginSpec();
		return rootModuleDefinition;
	}

}
