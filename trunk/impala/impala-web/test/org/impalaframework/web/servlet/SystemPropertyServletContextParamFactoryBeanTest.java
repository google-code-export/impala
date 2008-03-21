package org.impalaframework.web.servlet;

import javax.servlet.ServletContext;

import static org.easymock.EasyMock.*;

import junit.framework.TestCase;

public class SystemPropertyServletContextParamFactoryBeanTest extends TestCase {

	private ServletContext servletContext;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		servletContext = createMock(ServletContext.class);
	}
	
	public void testGetObject() {
		replay(servletContext);
		
		verify(servletContext);
	}

}
