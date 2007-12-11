package org.impalaframework.plugin.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.springframework.beans.BeansException;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

import static org.easymock.classextension.EasyMock.*;

import junit.framework.TestCase;

public class ImpalaContextLoaderListenerTest extends TestCase {

	private ServletContext servletContext;

	private ImpalaContextLoaderListener contextLoader;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		contextLoader = new ImpalaContextLoaderListener();
		servletContext = createMock(ServletContext.class);
	}

	public final void testContextInitializedServletContextEvent() {
		expect(servletContext.getInitParameter(WebConstants.CONTEXT_LOADER_CLASS_NAME)).andReturn(
				TestContextLoader.class.getName());

		replay(servletContext);
		contextLoader.contextInitialized(new ServletContextEvent(servletContext));
		assertEquals(1, TestContextLoader.getAccessCount());
		verify(servletContext);
	}

	public final void testClassNotFound() {
		expect(servletContext.getInitParameter(WebConstants.CONTEXT_LOADER_CLASS_NAME)).andReturn("unknownclass");

		replay(servletContext);
		try {
			contextLoader.contextInitialized(new ServletContextEvent(servletContext));
			fail();
		}
		catch (IllegalStateException e) {
			assertEquals("Unable to instantiate context loader class unknownclass", e.getMessage());
		}
		verify(servletContext);
	}

	public final void testInvalidClassType() {
		expect(servletContext.getInitParameter(WebConstants.CONTEXT_LOADER_CLASS_NAME)).andReturn(
				String.class.getName());

		replay(servletContext);
		try {
			contextLoader.contextInitialized(new ServletContextEvent(servletContext));
			fail();
		}
		catch (IllegalStateException e) {
			assertEquals(
					"Error instantiating context loader class java.lang.String: java.lang.String cannot be cast to org.springframework.web.context.ContextLoader",
					e.getMessage());
		}
		verify(servletContext);
	}

	public final void testContextDestroyedWithNoLoader() {
		replay(servletContext);
		contextLoader.contextDestroyed(new ServletContextEvent(servletContext));
		verify(servletContext);
	}
	
	public final void testContextDestroyed() {
		expect(servletContext.getInitParameter(WebConstants.CONTEXT_LOADER_CLASS_NAME)).andReturn(
				TestContextLoader.class.getName());
		
		replay(servletContext);
		contextLoader.contextInitialized(new ServletContextEvent(servletContext));
		contextLoader.contextDestroyed(new ServletContextEvent(servletContext));
		assertEquals(1, TestContextLoader.getCloseCount());
		verify(servletContext);
	}

}

class TestContextLoader extends ContextLoader {

	static int accessCount;

	static int closeCount;

	TestContextLoader() {
		super();
		accessCount = 0;
		closeCount = 0;
	}

	@Override
	public WebApplicationContext initWebApplicationContext(ServletContext servletContext) throws IllegalStateException,
			BeansException {
		accessCount++;
		return new GenericWebApplicationContext();
	}

	@Override
	public void closeWebApplicationContext(ServletContext servletContext) {
		closeCount++;
	}

	public static int getAccessCount() {
		return accessCount;
	}

	public static int getCloseCount() {
		return closeCount;
	}

	public static void setAccessCount(int accessCount) {
		TestContextLoader.accessCount = accessCount;
	}

}
