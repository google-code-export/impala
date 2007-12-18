package org.impalaframework.module.web;

import junit.framework.TestCase;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.module.web.ImpalaRootServlet;
import org.impalaframework.module.web.WebRootPluginSpec;

public class ImpalaRootServletTest extends TestCase {

	public final void testNewPluginSpec() {
		ImpalaRootServlet servlet = new ImpalaRootServlet();
		SimpleRootModuleDefinition simpleRootModuleDefinition = new SimpleRootModuleDefinition("context.xml");
		ModuleDefinition newPluginSpec = servlet.newPluginSpec("plugin1", simpleRootModuleDefinition);
		assertEquals(WebRootPluginSpec.class.getName(), newPluginSpec.getClass().getName());
	}

}
