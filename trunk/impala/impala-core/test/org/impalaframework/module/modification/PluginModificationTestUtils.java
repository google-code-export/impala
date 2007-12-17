package org.impalaframework.module.modification;

import org.impalaframework.module.builder.SingleStringModuleDefinitionSource;
import org.impalaframework.module.spec.RootModuleDefinition;
import org.impalaframework.module.spec.SimpleRootModuleDefinition;

public class PluginModificationTestUtils {

	static RootModuleDefinition spec(String contextString, String pluginString) {
		String[] locations = contextString.split(",");
		SingleStringModuleDefinitionSource builder = new SingleStringModuleDefinitionSource(new SimpleRootModuleDefinition(locations),
				pluginString);
		RootModuleDefinition rootModuleDefinition = builder.getModuleDefintion();
		return rootModuleDefinition;
	}

}
