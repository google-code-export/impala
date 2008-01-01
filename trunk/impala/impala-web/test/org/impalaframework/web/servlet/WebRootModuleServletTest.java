package org.impalaframework.web.servlet;

import junit.framework.TestCase;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.web.module.WebRootModuleDefinition;
import org.impalaframework.web.servlet.RootWebModuleServlet;

public class WebRootModuleServletTest extends TestCase {

	public final void testNewModuleDefinition() {
		RootWebModuleServlet servlet = new RootWebModuleServlet();
		SimpleRootModuleDefinition simpleRootModuleDefinition = new SimpleRootModuleDefinition("context.xml");
		ModuleDefinition newModuleDefinition = servlet.newModuleDefinition("plugin1", simpleRootModuleDefinition);
		assertEquals(WebRootModuleDefinition.class.getName(), newModuleDefinition.getClass().getName());
	}

}
