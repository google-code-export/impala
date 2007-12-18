package org.impalaframework.module.builder;

import junit.framework.TestCase;

import org.impalaframework.module.builder.SingleStringModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleBeansetModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;

public class SingleStringModuleDefinitionSourceTest extends TestCase {

	public void testEmptyString() {
		SimpleRootModuleDefinition parentSpec = new SimpleRootModuleDefinition(new String[] { "parent-context" });
		String pluginString = "";
		SingleStringModuleDefinitionSource builder = new SingleStringModuleDefinitionSource(parentSpec, pluginString);
		RootModuleDefinition result = builder.getModuleDefintion();
		assertSame(result, parentSpec);
	}
	
	public void testPluginWithoutBeanSpec() {
		SimpleRootModuleDefinition parentSpec = new SimpleRootModuleDefinition(new String[] { "parent-context" });
		String pluginString = " wineorder-hibernate , wineorder-dao ";
		SingleStringModuleDefinitionSource builder = new SingleStringModuleDefinitionSource(parentSpec, pluginString);
		RootModuleDefinition result = builder.getModuleDefintion();
		assertSame(result, parentSpec);
		assertEquals(2, parentSpec.getPluginNames().size());
		System.out.println(parentSpec.getPluginNames());
		assertNotNull(result.getPlugin("wineorder-hibernate"));
		assertNotNull(result.getPlugin("wineorder-dao"));
	}
	
	public void testPluginWithBeanOverrides() {
		SimpleRootModuleDefinition parentSpec = new SimpleRootModuleDefinition(new String[] { "parent-context" });
		String pluginString = " wineorder-hibernate ,wineorder-merchant ( null: set1, set2; mock: set3, duff ), wineorder-dao ()";
		SingleStringModuleDefinitionSource builder = new SingleStringModuleDefinitionSource(parentSpec, pluginString);
		RootModuleDefinition result = builder.getModuleDefintion();
		assertSame(result, parentSpec);
		assertEquals(3, parentSpec.getPluginNames().size());
		System.out.println(parentSpec.getPluginNames());
		assertNotNull(result.getPlugin("wineorder-hibernate"));
		assertNotNull(result.getPlugin("wineorder-dao"));
		assertNotNull(result.getPlugin("wineorder-merchant"));
		assertTrue(result.getPlugin("wineorder-dao") instanceof SimpleBeansetModuleDefinition);
		assertTrue(result.getPlugin("wineorder-merchant") instanceof SimpleBeansetModuleDefinition);
	}
	
	public void testInvalidBrackets() {
		SimpleRootModuleDefinition parentSpec = new SimpleRootModuleDefinition(new String[] { "parent-context" });
		String pluginString = "plugin (( null: set1, set2; mock: set3, duff )";
		SingleStringModuleDefinitionSource builder = new SingleStringModuleDefinitionSource(parentSpec, pluginString);
		try {
			builder.doPluginSplit();
			fail(IllegalArgumentException.class.getName());
		}
		catch (IllegalArgumentException e) {
			assertEquals("Invalid plugin string plugin (( null: set1, set2; mock: set3, duff ). Invalid character '(' at column 9", e.getMessage());
		}
		
		pluginString = "plugin ( null: set1, set2; mock: set3, duff ))";
		builder = new SingleStringModuleDefinitionSource(parentSpec, pluginString);
		try {
			builder.doPluginSplit();
			fail(IllegalArgumentException.class.getName());
		}
		catch (IllegalArgumentException e) {
			assertEquals("Invalid plugin string plugin ( null: set1, set2; mock: set3, duff )). Invalid character ')' at column 46", e.getMessage());
		}
	}

}
