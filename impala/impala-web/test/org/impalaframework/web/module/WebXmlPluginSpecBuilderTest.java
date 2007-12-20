package org.impalaframework.web.module;

import junit.framework.TestCase;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.web.module.ServletModuleDefinition;
import org.impalaframework.web.module.WebPlaceholderModuleDefinition;
import org.impalaframework.web.module.WebRootModuleDefinition;
import org.impalaframework.web.module.WebXmlRootDefinitionBuilder;
import org.springframework.core.io.ClassPathResource;

public class WebXmlPluginSpecBuilderTest extends TestCase {

	public final void testCreatePluginSpec() {
		WebXmlRootDefinitionBuilder builder = new WebXmlRootDefinitionBuilder();
		builder.setResource(new ClassPathResource("xmlspec/webspec.xml"));
		RootModuleDefinition actual = builder.getModuleDefinition();
		assertEquals(4, actual.getPlugins().size());
		
		RootModuleDefinition expected = new SimpleRootModuleDefinition(new String[] { "parentTestContext.xml" });
		assertEquals(expected, actual);
		
		ModuleDefinition spec1 = new SimpleModuleDefinition(expected, "plugin1");
		ModuleDefinition spec2 = new WebRootModuleDefinition(expected, "servlet1", new String[]{"location1", "location2"});
		ModuleDefinition spec3 = new ServletModuleDefinition(expected, "servlet2", new String[]{"location3", "location4"});
		ModuleDefinition spec4 = new WebPlaceholderModuleDefinition(expected, "servlet3");
		
		assertEquals(spec1, actual.findPlugin("plugin1", true));
		assertEquals(spec2, actual.findPlugin("servlet1", true));
		assertEquals(spec3, actual.findPlugin("servlet2", true));
		assertEquals(spec4, actual.findPlugin("servlet3", true));
	}

}
