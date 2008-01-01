package org.impalaframework.web.loader;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.impalaframework.web.WebConstants;

public class ExternalXmlBasedImpalaContextLoaderTest extends TestCase {

	private ExternalXmlBasedImpalaContextLoader loader;

	private ServletContext servletContext;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		loader = new ExternalXmlBasedImpalaContextLoader();
		servletContext = createMock(ServletContext.class);
		System.clearProperty(WebConstants.BOOTSTRAP_MODULES_RESOURCE_PARAM);

	}	
	
	public final void testResolutionStrategy() {
		expect(servletContext.getInitParameter(WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM)).andReturn(null);
		
		replay(servletContext);
		String[] locations = loader.getBootstrapContextLocations(servletContext);
		assertEquals(2, locations.length);
		assertEquals("META-INF/impala-bootstrap.xml", locations[0]);
		verify(servletContext);
	}

	public final void testNoParameterSpecified() {
		expect(servletContext.getInitParameter(WebConstants.BOOTSTRAP_MODULES_RESOURCE_PARAM)).andReturn(null);

		replay(servletContext);
		try {
			loader.getModuleDefinitionSource(servletContext);
		}
		catch (IllegalStateException e) {
			assertEquals(
					"Unable to resolve locations resource name parameter 'bootstrapModulesResource' from either a system property or a 'context-param' entry in the web application's WEB-INF/web.xml",
					e.getMessage());
		}
		verify(servletContext);
	}

	public final void testResourceNotPresent() {
		expect(servletContext.getInitParameter(WebConstants.BOOTSTRAP_MODULES_RESOURCE_PARAM)).andReturn("notpresent");
		
		replay(servletContext);
		try {
			loader.getModuleDefinitionSource(servletContext);
		}
		catch (IllegalStateException e) {
			assertEquals("Plugin spec XML resource 'class path resource [notpresent]' does not exist", e.getMessage());
		}

		verify(servletContext);
	}

	public final void testGetModuleDefinition() {
		doSucceedingTest("xmlspec/webspec.xml");
		doSucceedingTest("classpath:xmlspec/webspec.xml");
	}

	private void doSucceedingTest(String resourceName) {
		expect(servletContext.getInitParameter(WebConstants.BOOTSTRAP_MODULES_RESOURCE_PARAM)).andReturn(resourceName);

		replay(servletContext);

		assertNotNull(loader.getModuleDefinitionSource(servletContext));

		verify(servletContext);
		reset(servletContext);
	}
}
