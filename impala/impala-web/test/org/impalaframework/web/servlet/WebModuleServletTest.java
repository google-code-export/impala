package org.impalaframework.web.servlet;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import org.impalaframework.web.module.ServletModuleDefinition;
import org.impalaframework.web.module.WebRootModuleDefinition;
import org.impalaframework.web.servlet.WebModuleServlet;

public class WebModuleServletTest extends TestCase {

	private ServletContext servletContext;

	private ServletConfig servletConfig;

	private String servletName;

	private WebModuleServlet servlet;

	@Override
	@SuppressWarnings("serial")
	protected void setUp() throws Exception {
		super.setUp();
		servletContext = createMock(ServletContext.class);
		servletConfig = createMock(ServletConfig.class);
		servletName = "servletName";
		servlet = new WebModuleServlet() {
			public ServletConfig getServletConfig() {
				return servletConfig;
			}
		};
	}

	public final void testNewPluginSpec() {

		SimpleRootModuleDefinition simpleRootModuleDefinition = new SimpleRootModuleDefinition("context.xml");
		new WebRootModuleDefinition(simpleRootModuleDefinition, "web-root", new String[] { "web-context.xml" });

		expect(servletConfig.getServletContext()).andReturn(servletContext);
		expect(servletContext.getInitParameter("rootWebPlugin")).andReturn("web-root");
		expect(servletConfig.getServletName()).andReturn(servletName);

		replayMocks();

		ModuleDefinition newPluginSpec = servlet.newPluginSpec("plugin1", simpleRootModuleDefinition);
		assertEquals(ServletModuleDefinition.class.getName(), newPluginSpec.getClass().getName());

		verifyMocks();
	}
	
	public final void testMissingPlugin() {

		SimpleRootModuleDefinition simpleRootModuleDefinition = new SimpleRootModuleDefinition("context.xml");

		expect(servletConfig.getServletContext()).andReturn(servletContext);
		expect(servletContext.getInitParameter("rootWebPlugin")).andReturn("web-root");

		replayMocks();

		try {
			servlet.newPluginSpec("plugin1", simpleRootModuleDefinition);
			fail();
		}
		catch (IllegalStateException e) {
			assertEquals("Unable to find root plugin 'web-root' specified using the web.xml parameter 'rootWebPlugin'", e.getMessage());
		}

		verifyMocks();
	}

	private void verifyMocks() {
		verify(servletConfig);
		verify(servletContext);
	}

	private void replayMocks() {
		replay(servletConfig);
		replay(servletContext);
	}

}