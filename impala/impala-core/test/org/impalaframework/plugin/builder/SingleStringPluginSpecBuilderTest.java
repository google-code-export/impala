package org.impalaframework.plugin.builder;

import junit.framework.TestCase;

import org.impalaframework.plugin.spec.ParentSpec;
import org.impalaframework.plugin.spec.SimpleBeansetPluginSpec;
import org.impalaframework.plugin.spec.SimpleParentSpec;

public class SingleStringPluginSpecBuilderTest extends TestCase {

	public void testEmptyString() {
		SimpleParentSpec parentSpec = new SimpleParentSpec(new String[] { "parent-context" });
		String pluginString = "";
		SingleStringPluginSpecBuilder builder = new SingleStringPluginSpecBuilder(parentSpec, pluginString);
		ParentSpec result = builder.getPluginSpec();
		assertSame(result, parentSpec);
	}
	
	public void testPluginWithoutBeanSpec() {
		SimpleParentSpec parentSpec = new SimpleParentSpec(new String[] { "parent-context" });
		String pluginString = " wineorder-hibernate , wineorder-dao ";
		SingleStringPluginSpecBuilder builder = new SingleStringPluginSpecBuilder(parentSpec, pluginString);
		ParentSpec result = builder.getPluginSpec();
		assertSame(result, parentSpec);
		assertEquals(2, parentSpec.getPluginNames().size());
		System.out.println(parentSpec.getPluginNames());
		assertNotNull(result.getPlugin("wineorder-hibernate"));
		assertNotNull(result.getPlugin("wineorder-dao"));
	}
	
	public void testPluginWithBeanOverrides() {
		SimpleParentSpec parentSpec = new SimpleParentSpec(new String[] { "parent-context" });
		String pluginString = " wineorder-hibernate ,wineorder-merchant ( null: set1, set2; mock: set3, duff ), wineorder-dao ()";
		SingleStringPluginSpecBuilder builder = new SingleStringPluginSpecBuilder(parentSpec, pluginString);
		ParentSpec result = builder.getPluginSpec();
		assertSame(result, parentSpec);
		assertEquals(3, parentSpec.getPluginNames().size());
		System.out.println(parentSpec.getPluginNames());
		assertNotNull(result.getPlugin("wineorder-hibernate"));
		assertNotNull(result.getPlugin("wineorder-dao"));
		assertNotNull(result.getPlugin("wineorder-merchant"));
		assertTrue(result.getPlugin("wineorder-dao") instanceof SimpleBeansetPluginSpec);
		assertTrue(result.getPlugin("wineorder-merchant") instanceof SimpleBeansetPluginSpec);
	}
	
	public void testInvalidBrackets() {
		SimpleParentSpec parentSpec = new SimpleParentSpec(new String[] { "parent-context" });
		String pluginString = "plugin (( null: set1, set2; mock: set3, duff )";
		SingleStringPluginSpecBuilder builder = new SingleStringPluginSpecBuilder(parentSpec, pluginString);
		try {
			builder.doPluginSplit();
			fail(IllegalArgumentException.class.getName());
		}
		catch (IllegalArgumentException e) {
			assertEquals("Invalid plugin string plugin (( null: set1, set2; mock: set3, duff ). Invalid character '(' at column 9", e.getMessage());
		}
		
		pluginString = "plugin ( null: set1, set2; mock: set3, duff ))";
		builder = new SingleStringPluginSpecBuilder(parentSpec, pluginString);
		try {
			builder.doPluginSplit();
			fail(IllegalArgumentException.class.getName());
		}
		catch (IllegalArgumentException e) {
			assertEquals("Invalid plugin string plugin ( null: set1, set2; mock: set3, duff )). Invalid character ')' at column 46", e.getMessage());
		}
	}

}
