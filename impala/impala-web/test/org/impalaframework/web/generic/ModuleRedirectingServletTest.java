package org.impalaframework.web.generic;

import junit.framework.TestCase;

public class ModuleRedirectingServletTest extends TestCase {

	private ModuleRedirectingServlet servlet;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		servlet = new ModuleRedirectingServlet();
	}

	public void testGetModuleName() {
		assertEquals("somepath", servlet.getModuleName("/somepath/more.htm"));
		assertEquals(null, servlet.getModuleName("/apage.htm"));
	}

	public void testGetModuleNameWithPrefix() {
		servlet.setModulePrefix("prefix-");
		assertEquals("prefix-somepath", servlet.getModuleName("/somepath/more.htm"));
		assertEquals(null, servlet.getModuleName("/apage.htm"));
	}


}
