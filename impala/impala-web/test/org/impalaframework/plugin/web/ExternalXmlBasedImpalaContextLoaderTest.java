package org.impalaframework.plugin.web;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

public class ExternalXmlBasedImpalaContextLoaderTest extends TestCase {

	private ExternalXmlBasedImpalaContextLoader loader;

	private ServletContext servletContext;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		loader = new ExternalXmlBasedImpalaContextLoader();
		servletContext = createMock(ServletContext.class);
		System.clearProperty(WebConstants.BOOTSTRAP_PLUGINS_RESOURCE_PARAM);

	}

	public final void testNoParameterSpecified() {
		expect(servletContext.getInitParameter(WebConstants.BOOTSTRAP_PLUGINS_RESOURCE_PARAM)).andReturn(null);

		replay(servletContext);
		try {
			loader.getPluginSpec(servletContext);
		}
		catch (IllegalStateException e) {
			assertEquals(
					"Unable to resolve locations resource name parameter 'bootstrapPluginsResource' from either a system property or a 'context-param' entry in the web application's WEB-INF/web.xml",
					e.getMessage());
		}
		verify(servletContext);
	}

	public final void testResourceNotPresent() {
		expect(servletContext.getInitParameter(WebConstants.BOOTSTRAP_PLUGINS_RESOURCE_PARAM)).andReturn("notpresent");
		
		replay(servletContext);
		try {
			loader.getPluginSpec(servletContext);
		}
		catch (IllegalStateException e) {
			assertEquals("Plugin spec XML resource 'class path resource [notpresent]' does not exist", e.getMessage());
		}

		verify(servletContext);
	}

	public final void testGetPluginSpec() {
		doSucceedingTest("xmlspec/webspec.xml");
		doSucceedingTest("classpath:xmlspec/webspec.xml");
	}

	private void doSucceedingTest(String resourceName) {
		expect(servletContext.getInitParameter(WebConstants.BOOTSTRAP_PLUGINS_RESOURCE_PARAM)).andReturn(resourceName);

		replay(servletContext);

		assertNotNull(loader.getPluginSpec(servletContext));

		verify(servletContext);
		reset(servletContext);
	}
}
