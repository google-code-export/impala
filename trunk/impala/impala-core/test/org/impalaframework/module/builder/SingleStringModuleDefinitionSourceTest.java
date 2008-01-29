package org.impalaframework.module.builder;

import junit.framework.TestCase;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.builder.SingleStringModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleBeansetModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;

public class SingleStringModuleDefinitionSourceTest extends TestCase {

	public void testEmptyString() {
		SimpleRootModuleDefinition rootDefinition = new SimpleRootModuleDefinition(new String[] { "parent-context" });
		String moduleString = "";
		SingleStringModuleDefinitionSource builder = new SingleStringModuleDefinitionSource(rootDefinition, moduleString);
		RootModuleDefinition result = builder.getModuleDefinition();
		assertSame(result, rootDefinition);
	}
	
	public void testPluginWithoutBeanSpec() {
		SimpleRootModuleDefinition rootDefinition = new SimpleRootModuleDefinition(new String[] { "parent-context" });
		String moduleString = " wineorder-hibernate , wineorder-dao ";
		SingleStringModuleDefinitionSource builder = new SingleStringModuleDefinitionSource(rootDefinition, moduleString);
		RootModuleDefinition result = builder.getModuleDefinition();
		assertSame(result, rootDefinition);
		assertEquals(2, rootDefinition.getModuleNames().size());
		System.out.println(rootDefinition.getModuleNames());
		assertNotNull(result.getModule("wineorder-hibernate"));
		assertNotNull(result.getModule("wineorder-dao"));
	}
	
	public void testPluginWithBeanOverrides() {
		SimpleRootModuleDefinition rootDefinition = new SimpleRootModuleDefinition(new String[] { "parent-context" });
		String pluginString = " wineorder-hibernate ,wineorder-merchant ( null: set1, set2; mock: set3, duff ), wineorder-dao ()";
		SingleStringModuleDefinitionSource builder = new SingleStringModuleDefinitionSource(rootDefinition, pluginString);
		RootModuleDefinition result = builder.getModuleDefinition();
		assertSame(result, rootDefinition);
		assertEquals(3, rootDefinition.getModuleNames().size());
		System.out.println(rootDefinition.getModuleNames());
		assertNotNull(result.getModule("wineorder-hibernate"));
		assertNotNull(result.getModule("wineorder-dao"));
		assertNotNull(result.getModule("wineorder-merchant"));
		assertTrue(result.getModule("wineorder-dao") instanceof SimpleBeansetModuleDefinition);
		assertTrue(result.getModule("wineorder-merchant") instanceof SimpleBeansetModuleDefinition);
	}	
	
	public void testInvalidBrackets() {
		SimpleRootModuleDefinition rootDefinition = new SimpleRootModuleDefinition(new String[] { "parent-context" });
		String moduleString = "plugin (( null: set1, set2; mock: set3, duff )";
		SingleStringSourceDelegate builder = new SingleStringSourceDelegate(rootDefinition, moduleString);
		try {
			builder.doDefinitionSplit();
			fail(IllegalArgumentException.class.getName());
		}
		catch (ConfigurationException e) {
			assertEquals("Invalid definition string plugin (( null: set1, set2; mock: set3, duff ). Invalid character '(' at column 9", e.getMessage());
		}
		
		moduleString = "plugin ( null: set1, set2; mock: set3, duff ))";
		builder = new SingleStringSourceDelegate(rootDefinition, moduleString);
		try {
			builder.doDefinitionSplit();
			fail(IllegalArgumentException.class.getName());
		}
		catch (ConfigurationException e) {
			assertEquals("Invalid definition string plugin ( null: set1, set2; mock: set3, duff )). Invalid character ')' at column 46", e.getMessage());
		}
	}

}
