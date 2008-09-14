package org.impalaframework.web.servlet.invoker;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.easymock.classextension.EasyMock;

public class ReadWriteLockInvokerTest extends TestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testInvoke() throws Exception {
		HttpServlet servlet = new HttpServlet() {
			private static final long serialVersionUID = 1L;

			public void service(HttpServletRequest req, HttpServletResponse res)
					throws ServletException, IOException {
				
			}
		};
		ReadWriteLockingInvoker invoker = new ReadWriteLockingInvoker(servlet);
		//FIXME should you be able to do this?
		invoker.writeLock();
		
		invoker.invoke(EasyMock.createMock(HttpServletRequest.class), EasyMock.createMock(HttpServletResponse.class));
	}
}
