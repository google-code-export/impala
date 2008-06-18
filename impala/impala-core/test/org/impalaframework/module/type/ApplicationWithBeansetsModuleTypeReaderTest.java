package org.impalaframework.module.type;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

import junit.framework.TestCase;

import org.impalaframework.module.builder.ModuleElementNames;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.definition.SimpleBeansetModuleDefinition;

public class ApplicationWithBeansetsModuleTypeReaderTest extends TestCase {
	private ApplicationWithBeansetsModuleTypeReader reader;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		reader = new ApplicationWithBeansetsModuleTypeReader();
	}

	public void testReadModuleDefinition() {
		ModuleDefinition definition = reader.readModuleDefinition(null, "mymodule", new Properties());
		SimpleBeansetModuleDefinition moduleDefinition = (SimpleBeansetModuleDefinition) definition;
		assertEquals("mymodule", moduleDefinition.getName());
		assertEquals(ModuleTypes.APPLICATION_WITH_BEANSETS, moduleDefinition.getType());
	}
	
	public void testReadModuleDefinitionLocations() {
		Properties properties = new Properties();
		properties.put(ModuleElementNames.CONTEXT_LOCATIONS_ELEMENT, "loc1, loc2,loc3");
		ModuleDefinition definition = reader.readModuleDefinition(null, "mymodule", properties);
		SimpleBeansetModuleDefinition moduleDefinition = (SimpleBeansetModuleDefinition) definition;
		assertEquals("mymodule", moduleDefinition.getName());
		assertEquals(ModuleTypes.APPLICATION_WITH_BEANSETS, moduleDefinition.getType());
		assertEquals(Arrays.asList(new String[]{ "loc1", "loc2", "loc3"}), moduleDefinition.getContextLocations());
	}
	
	public void testWithOverrides() {
		Properties properties = new Properties();
		properties.setProperty(ModuleElementNames.OVERRIDES_ELEMENT, "beanset: all;");
		ModuleDefinition definition = reader.readModuleDefinition(null, "mymodule", properties);
		SimpleBeansetModuleDefinition moduleDefinition = (SimpleBeansetModuleDefinition) definition;
		assertEquals("mymodule", moduleDefinition.getName());
		assertEquals(ModuleTypes.APPLICATION_WITH_BEANSETS, moduleDefinition.getType());
		assertEquals(Collections.singletonMap("beanset", Collections.singleton("all")), moduleDefinition.getOverrides());
	}
}
