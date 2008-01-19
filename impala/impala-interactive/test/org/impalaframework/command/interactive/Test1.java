package org.impalaframework.command.interactive;

import junit.framework.TestCase;

import org.impalaframework.module.builder.SimpleModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.testrun.DynamicContextHolder;

public class Test1 extends TestCase implements ModuleDefinitionSource {
	
	public static final String plugin1 = "impala-sample-dynamic-plugin1";

	ModuleDefinitionSource source = new SimpleModuleDefinitionSource("parentTestContext.xml", new String[] { plugin1 });

	public void testMyMethod() throws Exception {
		System.out.println("Running test method with " + DynamicContextHolder.get());
	}
	
	public RootModuleDefinition getModuleDefinition() {
		return source.getModuleDefinition();
	}
}
