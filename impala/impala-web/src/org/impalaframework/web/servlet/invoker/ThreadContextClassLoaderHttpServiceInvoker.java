package org.impalaframework.web.servlet.invoker;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ThreadContextClassLoaderHttpServiceInvoker implements HttpServiceInvoker {

	private static final Log logger = LogFactory.getLog(ThreadContextClassLoaderHttpServiceInvoker.class);	
	
	private final boolean setClassLoader;
	private final ClassLoader classLoader;
	private final Object delegate;

	public ThreadContextClassLoaderHttpServiceInvoker(Object delegate, boolean setClassLoader, ClassLoader classLoader) {
		super();
		this.setClassLoader = setClassLoader;
		this.classLoader = classLoader;
		this.delegate = delegate;
	}

	public void invoke(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (delegate != null) {			
			
			ClassLoader existingClassLoader = null;
			
			if (setClassLoader) {
				existingClassLoader = Thread.currentThread().getContextClassLoader();
				Thread.currentThread().setContextClassLoader(classLoader);
			}
			try {
				ServletInvokerUtils.invoke(delegate, request, response);
			}
			finally {
				if (setClassLoader) {
					Thread.currentThread().setContextClassLoader(existingClassLoader);
				}
			}
		} else {
			logger.warn(this + " has no delegate to invoke");
		}
	}
}
