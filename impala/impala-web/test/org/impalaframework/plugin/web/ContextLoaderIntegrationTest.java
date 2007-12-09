package org.impalaframework.plugin.web;

import static org.easymock.EasyMock.*;

import javax.servlet.ServletContext;

import org.impalaframework.plugin.bootstrap.BootstrapBeanFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

import junit.framework.TestCase;

public class ContextLoaderIntegrationTest extends TestCase {

	private ServletContext servletContext;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		servletContext = createMock(ServletContext.class);
	}
	
	public void testWebXmlBasedImpalaContextLoader() throws Exception {
		expect(servletContext.getInitParameter(ContextLoader.CONFIG_LOCATION_PARAM)).andReturn("parentTestContext.xml");
		expect(servletContext.getInitParameter(WebConstants.PLUGIN_NAMES_PARAM)).andReturn("impala-sample-dynamic-plugin1");
		servletContext.setAttribute(eq(WebConstants.IMPALA_FACTORY_PARAM), isA(BootstrapBeanFactory.class));
		
		replay(servletContext);

		WebXmlBasedImpalaContextLoader loader = new WebXmlBasedImpalaContextLoader();
		WebApplicationContext context = loader.createWebApplicationContext(servletContext, null);
		
		assertNotNull(context);
		assertTrue(context instanceof GenericWebApplicationContext);
		verify(servletContext);
	}
}
