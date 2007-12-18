package org.impalaframework.module.modification;

import org.impalaframework.module.builder.SingleStringModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;

public class ModuleModificationTestUtils {

	static RootModuleDefinition spec(String contextString, String pluginString) {
		String[] locations = contextString.split(",");
		SingleStringModuleDefinitionSource builder = new SingleStringModuleDefinitionSource(new SimpleRootModuleDefinition(locations),
				pluginString);
		RootModuleDefinition rootModuleDefinition = builder.getModuleDefintion();
		return rootModuleDefinition;
	}

}
