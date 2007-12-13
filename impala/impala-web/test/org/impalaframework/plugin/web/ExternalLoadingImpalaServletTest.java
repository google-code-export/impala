package org.impalaframework.plugin.web;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.impalaframework.plugin.bootstrap.ImpalaBootstrapFactory;
import org.impalaframework.plugin.transition.PluginStateManager;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

public class ExternalLoadingImpalaServletTest extends TestCase {

	private ServletConfig servletConfig;

	private ServletContext servletContext;

	private ImpalaBootstrapFactory factory;

	private PluginStateManager pluginStateManager;

	private ExternalLoadingImpalaServlet servlet;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		servletConfig = createMock(ServletConfig.class);
		servletContext = createMock(ServletContext.class);
		factory = createMock(ImpalaBootstrapFactory.class);
		pluginStateManager = createMock(PluginStateManager.class);

		servlet = new ExternalLoadingImpalaServlet() {
			private static final long serialVersionUID = 1L;

			@Override
			public ServletConfig getServletConfig() {
				return servletConfig;
			}
		};
	}

	public final void testNull() {
		commonExpections();
		expect(pluginStateManager.getPlugin("servletName")).andReturn(null);

		replayMocks();

		try {
			servlet.createWebApplicationContext();
			fail();
		}
		catch (IllegalStateException e) {
			assertEquals("No plugin registered under the name of servlet 'servletName'", e.getMessage());
		}

		verifyMocks();
	}

	public final void testNot() {
		commonExpections();
		expect(pluginStateManager.getPlugin("servletName")).andReturn(createMock(ConfigurableApplicationContext.class));

		replayMocks();

		try {
			servlet.createWebApplicationContext();
			fail();
		}
		catch (IllegalStateException e) {
			assertEquals("Plugin registered under name of servlet 'servletName' needs to be an instance of org.springframework.web.context.WebApplicationContext", e.getMessage());
		}

		verifyMocks();
	}

	public final void testWeb() {
		commonExpections();
		GenericWebApplicationContext applicationContext = new GenericWebApplicationContext();
		expect(pluginStateManager.getPlugin("servletName")).andReturn(applicationContext);

		replayMocks();

		assertSame(applicationContext, servlet.createWebApplicationContext());

		verifyMocks();
	}

	private void commonExpections() {
		expect(servletConfig.getServletContext()).andReturn(servletContext);
		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(factory);
		expect(factory.getPluginStateManager()).andReturn(pluginStateManager);
		expect(servletConfig.getServletName()).andReturn("servletName");
	}

	private void verifyMocks() {
		verify(servletConfig);
		verify(servletContext);
		verify(factory);
		verify(pluginStateManager);
	}

	private void replayMocks() {
		replay(servletConfig);
		replay(servletContext);
		replay(factory);
		replay(pluginStateManager);
	}

}
