package org.impalaframework.module.builder;

import java.util.List;

import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.resolver.StandaloneModuleLocationResolver;

import junit.framework.TestCase;

public class IncrementalModuleDefinitionSourceTest extends TestCase {

	private RootModuleDefinition moduleDefinition;
	private StandaloneModuleLocationResolver resolver;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	private void setExistingDefinition(String... moduleNames) {
		resolver = new StandaloneModuleLocationResolver();
		InternalModuleDefinitionSource moduleDefinitionSource = new InternalModuleDefinitionSource(resolver, moduleNames, true);
		moduleDefinition = moduleDefinitionSource.getModuleDefinition();
		System.out.println(moduleDefinition);
	}

	public void testGetSingleModulesToLoad() {
		setExistingDefinition("impala-core", "sample-module2");
		IncrementalModuleDefinitionSource moduleDefinitionSource = new IncrementalModuleDefinitionSource(resolver, moduleDefinition, "sample-module4");
		moduleDefinitionSource.getModuleDefinition();
		List<String> modulesToLoad = moduleDefinitionSource.getModulesToLoad();
		assertEquals(1, modulesToLoad.size());
		assertTrue(modulesToLoad.contains("sample-module4"));
	}
	
	public void testGetNoModulesToLoad() {
		setExistingDefinition("sample-module4");
		IncrementalModuleDefinitionSource moduleDefinitionSource = new IncrementalModuleDefinitionSource(resolver, moduleDefinition, "sample-module4");
		assertEquals(moduleDefinition, moduleDefinitionSource.getModuleDefinition());
		assertTrue(moduleDefinitionSource.getModulesToLoad().isEmpty());
	}
	
	public void testGetMultipleModulesToLoad() {
		setExistingDefinition("impala-core");
		IncrementalModuleDefinitionSource moduleDefinitionSource = new IncrementalModuleDefinitionSource(resolver, moduleDefinition, "sample-module4");
		moduleDefinitionSource.getModuleDefinition();
		List<String> modulesToLoad = moduleDefinitionSource.getModulesToLoad();
		assertEquals(2, modulesToLoad.size());
		assertTrue(modulesToLoad.contains("sample-module4"));
		assertTrue(modulesToLoad.contains("sample-module2"));
	}

}
