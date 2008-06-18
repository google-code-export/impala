package org.impalaframework.module.type;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

import junit.framework.TestCase;

import org.impalaframework.module.builder.ModuleElementNames;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;

public class RootModuleTypeReaderTest extends TestCase {

	private RootModuleTypeReader reader;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		reader = new RootModuleTypeReader();
	}

	public void testReadModuleDefinitionDefaults() {
		Properties properties = new Properties();
		ModuleDefinition moduleDefinition = reader.readModuleDefinition(null, "rootModule", properties);
		SimpleRootModuleDefinition definition = (SimpleRootModuleDefinition) moduleDefinition;
		assertEquals(Collections.singletonList("root-context.xml"), definition.getContextLocations());
		assertEquals(Collections.singletonList("rootModule"), definition.getRootProjectNames());
	}

	public void testReadModuleDefinition() {
		Properties properties = new Properties();
		properties.setProperty(ModuleElementNames.CONTEXT_LOCATIONS_ELEMENT, "loc1,loc2");
		properties.setProperty(ModuleElementNames.ROOT_PROJECT_NAMES_ELEMENT, "proj1,proj2");
		ModuleDefinition moduleDefinition = reader.readModuleDefinition(null, "rootModule", properties);
		SimpleRootModuleDefinition definition = (SimpleRootModuleDefinition) moduleDefinition;
		assertEquals(Arrays.asList(new String[]{"loc1", "loc2"}), definition.getContextLocations());
		assertEquals(Arrays.asList(new String[]{"proj1", "proj2"}), definition.getRootProjectNames());
	}

}
