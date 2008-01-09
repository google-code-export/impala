package org.impalaframework.testrun;

import junit.framework.TestCase;

import org.impalaframework.module.builder.SimpleModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;

public class Test1 extends TestCase implements ModuleDefinitionSource {
	private static final String plugin1 = "impala-sample-dynamic-plugin1";

	ModuleDefinitionSource source = new SimpleModuleDefinitionSource("parentTestContext.xml", new String[] { plugin1 });

	public void testOne() throws Exception {
		System.out.println();
	}
	
	public RootModuleDefinition getModuleDefinition() {
		return source.getModuleDefinition();
	}
}
