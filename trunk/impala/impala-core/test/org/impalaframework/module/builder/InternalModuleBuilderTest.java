package org.impalaframework.module.builder;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import junit.framework.TestCase;

import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleBeansetModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.resolver.StandaloneModuleLocationResolver;

public class InternalModuleBuilderTest extends TestCase {

	private Map<String, Set<String>> children;
	private Map<String, Properties> moduleProperties;
	private String rootModuleName;

	protected void setUp() throws Exception {
		super.setUp();
		StandaloneModuleLocationResolver resolver = new StandaloneModuleLocationResolver();
		String[] moduleNames = new String[] { "sample-module4" };
		InternalModuleDefinitionSource moduleDefinitionSource = new InternalModuleDefinitionSource(
				resolver, moduleNames, true);
		moduleDefinitionSource.inspectModules();
		children = moduleDefinitionSource.getChildren();
		moduleProperties = moduleDefinitionSource.getModuleProperties();
		rootModuleName = moduleDefinitionSource.getRootModuleName();
	}

	public void testGetModuleDefinition() {
		InternalModuleBuilder builder = new InternalModuleBuilder(rootModuleName, moduleProperties, children);
		RootModuleDefinition definition = builder.getModuleDefinition();
		System.out.println(definition);
		
		RootModuleDefinition root = new SimpleRootModuleDefinition("impala-core", "parentTestContext.xml");
		SimpleModuleDefinition sample2 = new SimpleModuleDefinition(root, "sample-module2");
		new SimpleBeansetModuleDefinition(sample2, "sample-module4");
		
		assertEquals(root, definition);
	}

}
