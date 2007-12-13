package org.impalaframework.plugin.web;

import static org.easymock.EasyMock.*;

import javax.servlet.ServletContext;

import org.impalaframework.plugin.bootstrap.BootstrapBeanFactory;
import org.impalaframework.plugin.builder.SingleStringPluginSpecBuilder;
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
		System.clearProperty(WebConstants.BOOTSTRAP_LOCATIONS_PROPERTY_PARAM);
		System.clearProperty(WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM);
		System.clearProperty(WebConstants.BOOTSTRAP_PLUGINS_RESOURCE_PARAM);
	}
	
	public void testWebXmlBasedContextLoader() throws Exception {
		expect(servletContext.getInitParameter(ContextLoader.CONFIG_LOCATION_PARAM)).andReturn("parentTestContext.xml");
		expect(servletContext.getInitParameter(WebConstants.PLUGIN_NAMES_PARAM)).andReturn("impala-sample-dynamic-plugin1");
		servletContext.setAttribute(eq(WebConstants.IMPALA_FACTORY_PARAM), isA(BootstrapBeanFactory.class));
		servletContext.setAttribute(eq(WebConstants.PLUGIN_SPEC_BUILDER_PARAM), isA(SingleStringPluginSpecBuilder.class));
		
		replay(servletContext);

		WebXmlBasedContextLoader loader = new WebXmlBasedContextLoader(){
			@Override
			protected String[] getBootstrapContextLocations(ServletContext servletContext) {
				String[] locations = new String[] { 
						"META-INF/impala-bootstrap.xml",
						"META-INF/impala-web-bootstrap.xml"};
				return locations;
			}
		};
		WebApplicationContext context = loader.createWebApplicationContext(servletContext, null);
		
		assertNotNull(context);
		assertTrue(context instanceof GenericWebApplicationContext);
		verify(servletContext);
	}	
	
	public void testConfigurableWebXmlBasedContextLoader() throws Exception {
		expect(servletContext.getInitParameter(WebConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM)).andReturn("org/impalaframework/plugin/web/bootstrap_locations.properties");
		expect(servletContext.getInitParameter(WebConstants.BOOTSTRAP_PLUGINS_RESOURCE_PARAM)).andReturn("org/impalaframework/plugin/web/plugin_locations.properties");
		expect(servletContext.getInitParameter(WebConstants.BOOTSTRAP_PLUGINS_RESOURCE_PARAM)).andReturn("org/impalaframework/plugin/web/plugin_locations.properties");
		servletContext.setAttribute(eq(WebConstants.IMPALA_FACTORY_PARAM), isA(BootstrapBeanFactory.class));		servletContext.setAttribute(eq(WebConstants.PLUGIN_SPEC_BUILDER_PARAM), isA(SingleStringPluginSpecBuilder.class));
		
		replay(servletContext);

		ConfigurableWebXmlBasedContextLoader loader = new ConfigurableWebXmlBasedContextLoader();
		WebApplicationContext context = loader.createWebApplicationContext(servletContext, null);
		
		assertNotNull(context);
		assertTrue(context instanceof GenericWebApplicationContext);
		verify(servletContext);
	}	
	
	public void testExternalXmlBasedContextLoader() throws Exception {
		expect(servletContext.getInitParameter(WebConstants.BOOTSTRAP_PLUGINS_RESOURCE_PARAM)).andReturn("xmlspec/xmlspec.xml");
		servletContext.setAttribute(eq(WebConstants.IMPALA_FACTORY_PARAM), isA(BootstrapBeanFactory.class));		
		servletContext.setAttribute(eq(WebConstants.PLUGIN_SPEC_BUILDER_PARAM), isA(WebXmlPluginSpecBuilder.class));
		
		replay(servletContext);

		ExternalXmlBasedImpalaContextLoader loader = new ExternalXmlBasedImpalaContextLoader() {

			@Override
			protected String[] getBootstrapContextLocations(ServletContext servletContext) {
				String[] locations = new String[] { 
						"META-INF/impala-bootstrap.xml",
						"META-INF/impala-web-bootstrap.xml"};
				return locations;
			}
			
		};
		WebApplicationContext context = loader.createWebApplicationContext(servletContext, null);
		
		assertNotNull(context);
		assertTrue(context instanceof GenericWebApplicationContext);
		verify(servletContext);
	}
}
