package org.impalaframework.module.builder;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.TypeReader;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.type.TypeReaderRegistryFactory;
import org.impalaframework.resolver.StandaloneModuleLocationResolver;

public class IncrementalModuleDefinitionSourceTest extends TestCase {

	private RootModuleDefinition rootModuleDefinition;
	private StandaloneModuleLocationResolver resolver;
	private Map<String,TypeReader> typeReaders;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.typeReaders = TypeReaderRegistryFactory.getTypeReaders();
	}

	private void setExistingDefinition(String... moduleNames) {
		resolver = new StandaloneModuleLocationResolver();
		InternalModuleDefinitionSource moduleDefinitionSource = new InternalModuleDefinitionSource(typeReaders, resolver, moduleNames, true);
		rootModuleDefinition = moduleDefinitionSource.getModuleDefinition();
		System.out.println(rootModuleDefinition);
	}

	public void testGetSingleModulesToLoad() {
		setExistingDefinition("impala-core", "sample-module2");
		IncrementalModuleDefinitionSource moduleDefinitionSource = new IncrementalModuleDefinitionSource(resolver, typeReaders, rootModuleDefinition, "sample-module4");
		moduleDefinitionSource.getModuleDefinition();
		List<String> modulesToLoad = moduleDefinitionSource.getModulesToLoad();
		assertEquals(1, modulesToLoad.size());
		assertTrue(modulesToLoad.contains("sample-module4"));
	}
	
	public void testGetNoModulesToLoad() {
		setExistingDefinition("sample-module4");
		IncrementalModuleDefinitionSource moduleDefinitionSource = new IncrementalModuleDefinitionSource(resolver, typeReaders, rootModuleDefinition, "sample-module4");
		assertEquals(rootModuleDefinition, moduleDefinitionSource.getModuleDefinition());
		assertTrue(moduleDefinitionSource.getModulesToLoad().isEmpty());
	}
	
	public void testGetMultipleModulesToLoad() {
		setExistingDefinition("impala-core");
		IncrementalModuleDefinitionSource moduleDefinitionSource = new IncrementalModuleDefinitionSource(resolver, typeReaders, rootModuleDefinition, "sample-module4");
		moduleDefinitionSource.getModuleDefinition();
		List<String> modulesToLoad = moduleDefinitionSource.getModulesToLoad();
		assertEquals(2, modulesToLoad.size());
		assertTrue(modulesToLoad.contains("sample-module4"));
		assertTrue(modulesToLoad.contains("sample-module2"));
	}
	
	public void testGetModuleDefinitionFourFromCore() {
		setExistingDefinition("impala-core");
		IncrementalModuleDefinitionSource moduleDefinitionSource = new IncrementalModuleDefinitionSource(resolver, typeReaders, rootModuleDefinition, "sample-module4");
		checkDefinition(moduleDefinitionSource);
	}

	public void testGetModuleDefinitionFourFromTwo() {
		setExistingDefinition("impala-core", "sample-module2");
		IncrementalModuleDefinitionSource moduleDefinitionSource = new IncrementalModuleDefinitionSource(resolver, typeReaders, rootModuleDefinition, "sample-module4");
		checkDefinition(moduleDefinitionSource);
	}

	public void testGetModuleDefinitionFourFromFour() {
		setExistingDefinition("impala-core", "sample-module2", "sample-module4");
		IncrementalModuleDefinitionSource moduleDefinitionSource = new IncrementalModuleDefinitionSource(resolver, typeReaders, rootModuleDefinition, "sample-module4");
		checkDefinition(moduleDefinitionSource);
	}

	public void testGetModuleDefinitionDuffFromFour() {
		setExistingDefinition("impala-core", "sample-module2");
		IncrementalModuleDefinitionSource moduleDefinitionSource = new IncrementalModuleDefinitionSource(resolver, typeReaders, rootModuleDefinition, "duff-module");
		try {
			moduleDefinitionSource.getModuleDefinition();
		} catch (ConfigurationException e) {
			assertEquals("Application is using internally defined module structure, but no module.properties file is present on the classpath for module 'duff-module'", e.getMessage());
		}
	}

	private void checkDefinition(
			IncrementalModuleDefinitionSource moduleDefinitionSource) {
		RootModuleDefinition newDefinition = moduleDefinitionSource.getModuleDefinition();
		System.out.println("New definition: " + newDefinition);
		
		ModuleDefinition module2 = newDefinition.findChildDefinition("sample-module2", true);
		assertNotNull(module2);
		ModuleDefinition module4 = newDefinition.findChildDefinition("sample-module4", true);
		assertNotNull(module4);
		
		assertTrue(newDefinition.getChildDefinitions().contains(module2));
		assertEquals(1, newDefinition.getChildDefinitions().size());
		assertTrue(module2.getChildDefinitions().contains(module4));
		assertEquals(1, module2.getChildDefinitions().size());
		assertTrue(module4.getChildDefinitions().isEmpty());
	}
}
