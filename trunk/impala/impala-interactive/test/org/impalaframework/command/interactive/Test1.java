package org.impalaframework.command.interactive;

import org.impalaframework.module.builder.SimpleModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;

public class Test1 implements ModuleDefinitionSource {
	
	public static final String plugin1 = "impala-sample-dynamic-plugin1";

	ModuleDefinitionSource source = new SimpleModuleDefinitionSource("parentTestContext.xml", new String[] { plugin1 });

	public RootModuleDefinition getModuleDefinition() {
		return source.getModuleDefinition();
	}
}
