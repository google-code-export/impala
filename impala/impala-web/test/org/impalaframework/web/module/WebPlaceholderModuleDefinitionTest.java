package org.impalaframework.web.module;

import junit.framework.TestCase;

import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.web.module.WebModuleTypes;
import org.impalaframework.web.module.WebPlaceholderModuleDefinition;

public class WebPlaceholderModuleDefinitionTest extends TestCase {

	public void testGetters() throws Exception {
		RootModuleDefinition parent = new SimpleRootModuleDefinition("parent-context.xml");
		WebPlaceholderModuleDefinition definition1 = new WebPlaceholderModuleDefinition(parent, "placeholder");
		assertEquals("placeholder", definition1.getName());
		assertEquals(WebModuleTypes.WEB_PLACEHOLDER, definition1.getType());
		assertSame(parent, definition1.getParentDefinition());
		assertTrue(definition1.getContextLocations().isEmpty());
	}	
	
	public void testEquals() throws Exception {
		RootModuleDefinition parent = new SimpleRootModuleDefinition("parent-context.xml");
		WebPlaceholderModuleDefinition definition1 = new WebPlaceholderModuleDefinition(parent, "placeholder");
		WebPlaceholderModuleDefinition definition2 = new WebPlaceholderModuleDefinition(parent, "placeholder");
		
		assertEquals(definition1, definition2);
	}
	
	public void testAdd() throws Exception {
		RootModuleDefinition parent = new SimpleRootModuleDefinition("parent-context.xml");
		WebPlaceholderModuleDefinition definition1 = new WebPlaceholderModuleDefinition(parent, "placeholder");
		WebPlaceholderModuleDefinition definition3 = new WebPlaceholderModuleDefinition(parent, "toAdd");
		
		try {
			definition1.add(definition3);
			fail();
		}
		catch (UnsupportedOperationException e) {
			assertEquals("Cannot add module 'toAdd' to web placeholder module definitionSource 'placeholder', as this cannot contain other modules", e.getMessage());
		}
		
		assertNull(definition1.findChildDefinition("someother", true));
		assertTrue(definition1.getChildDefinitions().isEmpty());
		assertTrue(definition1.getModuleNames().isEmpty());
		assertNull(definition1.remove("someplugin"));
	}
	
}
