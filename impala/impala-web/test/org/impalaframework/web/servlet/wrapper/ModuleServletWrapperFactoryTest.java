package org.impalaframework.web.servlet.wrapper;

import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;

import org.easymock.EasyMock;

public class ModuleServletWrapperFactoryTest extends TestCase {

	public void testWrapRequest() {
		ModuleServletWrapperFactory factory = new ModuleServletWrapperFactory();
		HttpServletRequest request = factory.wrapRequest(EasyMock.createMock(HttpServletRequest.class));
		request.getSession();
	}

}
