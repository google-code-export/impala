package org.impalaframework.module.web;

import junit.framework.TestCase;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.module.web.RootWebModuleServlet;
import org.impalaframework.module.web.WebRootModuleDefinition;

public class WebRootModuleServletTest extends TestCase {

	public final void testNewPluginSpec() {
		RootWebModuleServlet servlet = new RootWebModuleServlet();
		SimpleRootModuleDefinition simpleRootModuleDefinition = new SimpleRootModuleDefinition("context.xml");
		ModuleDefinition newPluginSpec = servlet.newPluginSpec("plugin1", simpleRootModuleDefinition);
		assertEquals(WebRootModuleDefinition.class.getName(), newPluginSpec.getClass().getName());
	}

}
