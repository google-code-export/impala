package org.impalaframework.module.web;

import junit.framework.TestCase;

import org.impalaframework.module.spec.ParentSpec;
import org.impalaframework.module.spec.SimpleParentSpec;
import org.impalaframework.module.web.WebPlaceholderPluginSpec;
import org.impalaframework.module.web.WebPluginTypes;

public class WebPlaceholderPluginSpecTest extends TestCase {

	public void testGetters() throws Exception {
		ParentSpec parent = new SimpleParentSpec("parent-context.xml");
		WebPlaceholderPluginSpec plugin1 = new WebPlaceholderPluginSpec(parent, "placeholder");
		assertEquals("placeholder", plugin1.getName());
		assertEquals(WebPluginTypes.WEB_PLACEHOLDER, plugin1.getType());
		assertSame(parent, plugin1.getParent());
		assertTrue(plugin1.getContextLocations().isEmpty());
	}	
	
	public void testEquals() throws Exception {
		ParentSpec parent = new SimpleParentSpec("parent-context.xml");
		WebPlaceholderPluginSpec plugin1 = new WebPlaceholderPluginSpec(parent, "placeholder");
		WebPlaceholderPluginSpec plugin2 = new WebPlaceholderPluginSpec(parent, "placeholder");
		
		assertEquals(plugin1, plugin2);
	}
	
	public void testAdd() throws Exception {
		ParentSpec parent = new SimpleParentSpec("parent-context.xml");
		WebPlaceholderPluginSpec plugin1 = new WebPlaceholderPluginSpec(parent, "placeholder");
		WebPlaceholderPluginSpec plugin3 = new WebPlaceholderPluginSpec(parent, "toAdd");
		
		try {
			plugin1.add(plugin3);
			fail();
		}
		catch (UnsupportedOperationException e) {
			assertEquals("Cannot add plugin 'toAdd' to web placeholder plugin spec 'placeholder', as this cannot contain other plugins", e.getMessage());
		}
		
		assertNull(plugin1.findPlugin("someother", true));
		assertTrue(plugin1.getPlugins().isEmpty());
		assertTrue(plugin1.getPluginNames().isEmpty());
		assertNull(plugin1.remove("someplugin"));
	}
	
}
