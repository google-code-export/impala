package org.impalaframework.plugin.web;

import junit.framework.TestCase;

import org.impalaframework.plugin.spec.SimpleParentSpec;

public class ImpalaPluginServletTest extends TestCase {

	public final void testNewPluginSpec() {
		ImpalaPluginServlet servlet = new ImpalaPluginServlet();
		SimpleParentSpec simpleParentSpec = new SimpleParentSpec("context.xml");
		//FIXME PluginSpec newPluginSpec = servlet.newPluginSpec("plugin1", simpleParentSpec);
		//FIXME complete assertEquals(WebRootPluginSpec.class.getName(), newPluginSpec.getClass().getName());
	}

}
