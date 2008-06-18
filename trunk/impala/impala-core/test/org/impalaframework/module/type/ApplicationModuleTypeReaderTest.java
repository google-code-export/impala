package org.impalaframework.module.type;

import java.util.Arrays;
import java.util.Properties;

import junit.framework.TestCase;

import org.impalaframework.module.builder.ModuleElementNames;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.ModuleTypes;
import org.impalaframework.module.definition.SimpleModuleDefinition;

public class ApplicationModuleTypeReaderTest extends TestCase {

	private ApplicationModuleTypeReader reader;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		reader = new ApplicationModuleTypeReader();
	}

	public void testReadModuleDefinition() {
		ModuleDefinition definition = reader.readModuleDefinition(null, "mymodule", new Properties());
		SimpleModuleDefinition moduleDefinition = (SimpleModuleDefinition) definition;
		assertEquals("mymodule", moduleDefinition.getName());
		assertEquals(ModuleTypes.APPLICATION, moduleDefinition.getType());
	}
	

	public void testReadModuleDefinitionLocations() {
		Properties properties = new Properties();
		properties.put(ModuleElementNames.CONTEXT_LOCATIONS_ELEMENT, "loc1, loc2,loc3");
		ModuleDefinition definition = reader.readModuleDefinition(null, "mymodule", properties);
		SimpleModuleDefinition moduleDefinition = (SimpleModuleDefinition) definition;
		assertEquals("mymodule", moduleDefinition.getName());
		assertEquals(ModuleTypes.APPLICATION, moduleDefinition.getType());
		assertEquals(Arrays.asList(new String[]{ "loc1", "loc2", "loc3"}), moduleDefinition.getContextLocations());
	}

}
