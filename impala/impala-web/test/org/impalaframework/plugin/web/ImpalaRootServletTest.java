package org.impalaframework.plugin.web;

import junit.framework.TestCase;

import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.plugin.spec.SimpleParentSpec;

public class ImpalaRootServletTest extends TestCase {

	public final void testNewPluginSpec() {
		ImpalaRootServlet servlet = new ImpalaRootServlet();
		SimpleParentSpec simpleParentSpec = new SimpleParentSpec("context.xml");
		PluginSpec newPluginSpec = servlet.newPluginSpec("plugin1", simpleParentSpec);
		assertEquals(WebRootPluginSpec.class.getName(), newPluginSpec.getClass().getName());
	}

}
