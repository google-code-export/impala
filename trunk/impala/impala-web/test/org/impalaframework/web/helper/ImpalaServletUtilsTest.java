package org.impalaframework.web.helper;


import junit.framework.TestCase;

import org.easymock.classextension.EasyMock;
import org.impalaframework.exception.ConfigurationException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

public class ImpalaServletUtilsTest extends TestCase {

	public void testCheckIsWebApplicationContext() {
		ImpalaServletUtils.checkIsWebApplicationContext("myservlet", EasyMock.createMock(WebApplicationContext.class));
		try {
			ImpalaServletUtils.checkIsWebApplicationContext("myservlet", EasyMock.createMock(ApplicationContext.class));
		} catch (ConfigurationException e) {
			assertEquals("Servlet 'myservlet' is not backed by an application context of type org.springframework.web.context.WebApplicationContext: EasyMock for interface org.springframework.context.ApplicationContext", e.getMessage());
		}
	}
	
	public void testGetModuleServletContextKey() throws Exception {
		assertEquals("module_moduleName:attributeName", ImpalaServletUtils.getModuleServletContextKey("moduleName", "attributeName"));
	}

}
