package org.impalaframework.module.web;

import junit.framework.TestCase;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.module.web.ServletPluginSpec;
import org.impalaframework.module.web.WebPlaceholderPluginSpec;
import org.impalaframework.module.web.WebRootPluginSpec;
import org.impalaframework.module.web.WebXmlPluginSpecBuilder;
import org.springframework.core.io.ClassPathResource;

public class WebXmlPluginSpecBuilderTest extends TestCase {

	public final void testCreatePluginSpec() {
		WebXmlPluginSpecBuilder builder = new WebXmlPluginSpecBuilder();
		builder.setResource(new ClassPathResource("xmlspec/webspec.xml"));
		RootModuleDefinition actual = builder.getModuleDefinition();
		assertEquals(4, actual.getPlugins().size());
		
		RootModuleDefinition expected = new SimpleRootModuleDefinition(new String[] { "parentTestContext.xml" });
		assertEquals(expected, actual);
		
		ModuleDefinition spec1 = new SimpleModuleDefinition(expected, "plugin1");
		ModuleDefinition spec2 = new WebRootPluginSpec(expected, "servlet1", new String[]{"location1", "location2"});
		ModuleDefinition spec3 = new ServletPluginSpec(expected, "servlet2", new String[]{"location3", "location4"});
		ModuleDefinition spec4 = new WebPlaceholderPluginSpec(expected, "servlet3");
		
		assertEquals(spec1, actual.findPlugin("plugin1", true));
		assertEquals(spec2, actual.findPlugin("servlet1", true));
		assertEquals(spec3, actual.findPlugin("servlet2", true));
		assertEquals(spec4, actual.findPlugin("servlet3", true));
	}

}
