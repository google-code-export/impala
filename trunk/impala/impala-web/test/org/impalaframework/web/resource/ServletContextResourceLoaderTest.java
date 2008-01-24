package org.impalaframework.web.resource;

import javax.servlet.ServletContext;

import org.springframework.core.io.Resource;
import org.springframework.web.context.support.ServletContextResource;

import static org.easymock.EasyMock.*;

import junit.framework.TestCase;

public class ServletContextResourceLoaderTest extends TestCase {

	private ServletContextResourceLoader resourceLoader;

	private ServletContext servletContext;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		resourceLoader = new ServletContextResourceLoader();
		servletContext = createMock(ServletContext.class);
		resourceLoader.setServletContext(servletContext);
	}

	public final void testGetResourceForPath() {		
		replay(servletContext);
		Resource resource = resourceLoader.getResource("/mypath", null);
		assertTrue(resource instanceof ServletContextResource);
		ServletContextResource r = (ServletContextResource)resource;
		assertSame(servletContext, r.getServletContext());
		verify(servletContext);
	}

}
