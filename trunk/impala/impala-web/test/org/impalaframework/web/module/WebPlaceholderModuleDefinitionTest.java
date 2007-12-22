package org.impalaframework.web.module;

import junit.framework.TestCase;

import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.web.module.WebModuleTypes;
import org.impalaframework.web.module.WebPlaceholderModuleDefinition;

public class WebPlaceholderModuleDefinitionTest extends TestCase {

	public void testGetters() throws Exception {
		RootModuleDefinition parent = new SimpleRootModuleDefinition("parent-context.xml");
		WebPlaceholderModuleDefinition plugin1 = new WebPlaceholderModuleDefinition(parent, "placeholder");
		assertEquals("placeholder", plugin1.getName());
		assertEquals(WebModuleTypes.WEB_PLACEHOLDER, plugin1.getType());
		assertSame(parent, plugin1.getParentDefinition());
		assertTrue(plugin1.getContextLocations().isEmpty());
	}	
	
	public void testEquals() throws Exception {
		RootModuleDefinition parent = new SimpleRootModuleDefinition("parent-context.xml");
		WebPlaceholderModuleDefinition plugin1 = new WebPlaceholderModuleDefinition(parent, "placeholder");
		WebPlaceholderModuleDefinition plugin2 = new WebPlaceholderModuleDefinition(parent, "placeholder");
		
		assertEquals(plugin1, plugin2);
	}
	
	public void testAdd() throws Exception {
		RootModuleDefinition parent = new SimpleRootModuleDefinition("parent-context.xml");
		WebPlaceholderModuleDefinition plugin1 = new WebPlaceholderModuleDefinition(parent, "placeholder");
		WebPlaceholderModuleDefinition plugin3 = new WebPlaceholderModuleDefinition(parent, "toAdd");
		
		try {
			plugin1.add(plugin3);
			fail();
		}
		catch (UnsupportedOperationException e) {
			assertEquals("Cannot add plugin 'toAdd' to web placeholder plugin spec 'placeholder', as this cannot contain other plugins", e.getMessage());
		}
		
		assertNull(plugin1.findChildDefinition("someother", true));
		assertTrue(plugin1.getChildDefinitions().isEmpty());
		assertTrue(plugin1.getModuleNames().isEmpty());
		assertNull(plugin1.remove("someplugin"));
	}
	
}
