package org.impalaframework.plugin.web;

import org.impalaframework.plugin.spec.PluginSpec;
import org.impalaframework.plugin.spec.SimpleParentSpec;

import junit.framework.TestCase;

public class ImpalaRootServletTest extends TestCase {

	public final void testNewPluginSpec() {
		ImpalaRootServlet servlet = new ImpalaRootServlet();
		SimpleParentSpec simpleParentSpec = new SimpleParentSpec("context.xml");
		PluginSpec newPluginSpec = servlet.newPluginSpec("plugin1", simpleParentSpec);
		assertEquals(WebRootPluginSpec.class.getName(), newPluginSpec.getClass().getName());
	}

}
