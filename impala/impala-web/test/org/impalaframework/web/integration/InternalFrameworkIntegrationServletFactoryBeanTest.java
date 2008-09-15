package org.impalaframework.web.integration;

import static org.easymock.EasyMock.createMock;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.springframework.web.context.WebApplicationContext;

public class InternalFrameworkIntegrationServletFactoryBeanTest extends
		TestCase {
	
	private InternalFrameworkIntegrationServletFactoryBean factoryBean;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		factoryBean = new InternalFrameworkIntegrationServletFactoryBean();
		factoryBean.setModuleDefinition(new SimpleModuleDefinition("mymodule"));
		factoryBean.setServletClass(InternalFrameworkIntegrationServlet.class);
		ServletContext servletContext = createMock(ServletContext.class);
		WebApplicationContext applicationContext = createMock(WebApplicationContext.class);
		factoryBean.setServletContext(servletContext);
		factoryBean.setApplicationContext(applicationContext);
		factoryBean.setDelegateServlet(new ModuleProxyServlet());
	}

	public void testWrongType() throws Exception {	
		
		factoryBean.setServletClass(ModuleProxyServlet.class);
		
		try {
			factoryBean.afterPropertiesSet();
		} catch (ConfigurationException e) {
			assertTrue(e.getMessage().contains("must be an instanceof org.impalaframework.web.integration.InternalFrameworkIntegrationServlet"));
		}
	}

	public void testAfterPropertiesSet() throws Exception {	
		
		assertEquals(InternalFrameworkIntegrationServlet.class, factoryBean.getObjectType());
		assertTrue(factoryBean.isSingleton());
		
		factoryBean.afterPropertiesSet();
		
		assertTrue(factoryBean.getObject() instanceof InternalFrameworkIntegrationServlet);
	}

}
