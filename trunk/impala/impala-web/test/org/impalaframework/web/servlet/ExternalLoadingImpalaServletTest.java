package org.impalaframework.web.servlet;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.impalaframework.module.bootstrap.ModuleManagementSource;
import org.impalaframework.module.holder.ModuleStateHolder;
import org.impalaframework.web.WebConstants;
import org.impalaframework.web.servlet.ExternalLoadingImpalaServlet;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

public class ExternalLoadingImpalaServletTest extends TestCase {

	private ServletConfig servletConfig;

	private ServletContext servletContext;

	private ModuleManagementSource factory;

	private ModuleStateHolder moduleStateHolder;

	private ExternalLoadingImpalaServlet servlet;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		servletConfig = createMock(ServletConfig.class);
		servletContext = createMock(ServletContext.class);
		factory = createMock(ModuleManagementSource.class);
		moduleStateHolder = createMock(ModuleStateHolder.class);

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
		expect(moduleStateHolder.getModule("servletName")).andReturn(null);

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
		expect(moduleStateHolder.getModule("servletName")).andReturn(createMock(ConfigurableApplicationContext.class));

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
		expect(moduleStateHolder.getModule("servletName")).andReturn(applicationContext);

		replayMocks();

		assertSame(applicationContext, servlet.createWebApplicationContext());

		verifyMocks();
	}

	private void commonExpections() {
		expect(servletConfig.getServletContext()).andReturn(servletContext);
		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(factory);
		expect(factory.getModuleStateHolder()).andReturn(moduleStateHolder);
		expect(servletConfig.getServletName()).andReturn("servletName");
	}

	private void verifyMocks() {
		verify(servletConfig);
		verify(servletContext);
		verify(factory);
		verify(moduleStateHolder);
	}

	private void replayMocks() {
		replay(servletConfig);
		replay(servletContext);
		replay(factory);
		replay(moduleStateHolder);
	}

}
