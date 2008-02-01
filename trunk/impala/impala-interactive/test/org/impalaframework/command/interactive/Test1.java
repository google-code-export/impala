package org.impalaframework.command.interactive;

import junit.framework.TestCase;

import org.impalaframework.facade.DynamicContextHolder;
import org.impalaframework.module.builder.SimpleModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.resolver.LocationConstants;

public class Test1 extends TestCase implements ModuleDefinitionSource {
	
	public static final String plugin1 = "impala-sample-dynamic-plugin1";

	ModuleDefinitionSource source = new SimpleModuleDefinitionSource("parentTestContext.xml", new String[] { plugin1 });

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty(LocationConstants.ROOT_PROJECTS_PROPERTY, "impala-core");
		DynamicContextHolder.init(this);
	}
	
	public void testMyMethod() throws Exception {
		System.out.println("Running test method with " + DynamicContextHolder.get());
	}
	
	public RootModuleDefinition getModuleDefinition() {
		return source.getModuleDefinition();
	}
}
