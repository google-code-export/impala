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

public class WebXmlRootDefinitionBuilderTest extends TestCase {

	public final void testCreateModuleDefinition() {
		WebXmlRootDefinitionBuilder builder = new WebXmlRootDefinitionBuilder();
		builder.setResource(new ClassPathResource("xmlspec/webspec.xml"));
		RootModuleDefinition actual = builder.getModuleDefinition();
		assertEquals(5, actual.getChildDefinitions().size());
		
		RootModuleDefinition expected = new SimpleRootModuleDefinition(new String[] { "parentTestContext.xml" });
		assertEquals(expected, actual);
		
		ModuleDefinition definition1 = new SimpleModuleDefinition(expected, "plugin1");
		ModuleDefinition definition2 = new SimpleModuleDefinition(expected, "plugin2", new String[]{"location1","location2"});
		ModuleDefinition definition3 = new WebRootModuleDefinition(expected, "servlet1", new String[]{"location1", "location2"});
		ModuleDefinition definition4 = new ServletModuleDefinition(expected, "servlet2", new String[]{"location3", "location4" });
		ModuleDefinition definition5 = new WebPlaceholderModuleDefinition(expected, "servlet3");

		assertEquals(definition1, actual.findChildDefinition("plugin1", true));
		assertEquals(definition2, actual.findChildDefinition("plugin2", true));
		assertEquals(definition3, actual.findChildDefinition("servlet1", true));
		assertEquals(definition4, actual.findChildDefinition("servlet2", true));
		assertEquals(definition5, actual.findChildDefinition("servlet3", true));
	}

}
