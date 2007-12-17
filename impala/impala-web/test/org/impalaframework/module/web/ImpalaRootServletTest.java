package org.impalaframework.module.web;

import junit.framework.TestCase;

import org.impalaframework.module.spec.PluginSpec;
import org.impalaframework.module.spec.SimpleParentSpec;
import org.impalaframework.module.web.ImpalaRootServlet;
import org.impalaframework.module.web.WebRootPluginSpec;

public class ImpalaRootServletTest extends TestCase {

	public final void testNewPluginSpec() {
		ImpalaRootServlet servlet = new ImpalaRootServlet();
		SimpleParentSpec simpleParentSpec = new SimpleParentSpec("context.xml");
		PluginSpec newPluginSpec = servlet.newPluginSpec("plugin1", simpleParentSpec);
		assertEquals(WebRootPluginSpec.class.getName(), newPluginSpec.getClass().getName());
	}

}
