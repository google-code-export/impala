package org.impalaframework.web.servlet.invoker;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.impalaframework.classloader.ModuleClassLoader;

public class ThreadContextClassLoaderHttpServiceInvokerTest extends TestCase {
	
	private ClassLoader contextClassLoader;
	private ClassLoader originalClassLoader;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		originalClassLoader = Thread.currentThread().getContextClassLoader();
	}
	
	public void testInvokeWithTrue() throws Exception {
		ClassLoader classLoader = new ModuleClassLoader(new File[]{new File("./")});
		
		HttpServlet servlet = new HttpServlet() {
			private static final long serialVersionUID = 1L;

			public void service(HttpServletRequest req, HttpServletResponse res)
					throws ServletException, IOException {
				contextClassLoader = Thread.currentThread().getContextClassLoader();
			}
		};
		ThreadContextClassLoaderHttpServiceInvoker invoker = new ThreadContextClassLoaderHttpServiceInvoker(servlet, true, classLoader);
		
		invoker.invoke(EasyMock.createMock(HttpServletRequest.class), EasyMock.createMock(HttpServletResponse.class));
		
		//check that the context class loader was correctly set in 
		assertSame(contextClassLoader, classLoader);
		
		//assert that the current thread now has the original class loader
		assertSame(originalClassLoader, Thread.currentThread().getContextClassLoader());
	}
}
