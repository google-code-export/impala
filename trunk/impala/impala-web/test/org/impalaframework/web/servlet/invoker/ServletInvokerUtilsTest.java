package org.impalaframework.web.servlet.invoker;

import static org.easymock.classextension.EasyMock.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

public class ServletInvokerUtilsTest extends TestCase {

	private HttpServiceInvoker invoker;
	private HttpServlet servlet;
	private HttpServletRequest request;
	private HttpServletResponse response;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		invoker = createMock(HttpServiceInvoker.class);
		servlet = createMock(HttpServlet.class);
		request = createMock(HttpServletRequest.class);
		response = createMock(HttpServletResponse.class);
	}
	
	public void testWithInvoker() throws Exception {
		invoker.invoke(request, response);
		
		replayMocks();
		
		ServletInvokerUtils.invoke(invoker, request, response);
		
		verifyMocks();
	}
	
	public void testWithServlet() throws Exception {
		servlet.service(request, response);
		
		replayMocks();
		
		ServletInvokerUtils.invoke(servlet, request, response);
		
		verifyMocks();
	}
	
	private void replayMocks() {
		replay(invoker);
		replay(servlet);
		replay(request);
		replay(response);
	}

	private void verifyMocks() {
		verify(invoker);
		verify(servlet);
		verify(request);
		verify(response);
	}


}
