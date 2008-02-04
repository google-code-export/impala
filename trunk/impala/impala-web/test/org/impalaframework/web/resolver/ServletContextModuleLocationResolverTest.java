package org.impalaframework.web.resolver;

import static org.easymock.EasyMock.*;

import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.core.io.Resource;
import org.springframework.web.context.support.ServletContextResource;

import junit.framework.TestCase;

public class ServletContextModuleLocationResolverTest extends TestCase {

	private ServletContextModuleLocationResolver resolver;
	private ServletContext servletContext;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		resolver = new ServletContextModuleLocationResolver();
		servletContext = createMock(ServletContext.class);
		resolver.setServletContext(servletContext);
		resolver.setApplicationVersion("1.0");
	}
	
	public final void testGetApplicationModuleClassLocations() {
		List<Resource> locations = resolver.getApplicationModuleClassLocations("mymodule");
		assertEquals(1, locations.size());
		assertTrue(locations.get(0) instanceof ServletContextResource);
	}

}
