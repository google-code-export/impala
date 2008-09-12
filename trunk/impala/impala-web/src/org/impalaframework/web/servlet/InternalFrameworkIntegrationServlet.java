/*
 * Copyright 2007-2008 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.web.servlet;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.web.WebConstants;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.FrameworkServlet;

/**
 * Servlet designed to integrate with existing servlet
 * @author Phil Zoio
 */
public class InternalFrameworkIntegrationServlet extends FrameworkServlet implements ApplicationContextAware {
	
	//FIXME need to test this
	
	private static final long serialVersionUID = 1L;

	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	private final Lock r = rwl.readLock();
	private final Lock w = rwl.writeLock();

	private WebApplicationContext applicationContext;
	
	private HttpServlet delegateServlet;
	
	private boolean setContextClassLoader = true;
	
	public InternalFrameworkIntegrationServlet() {
		super();
	}

	@Override
	protected void doService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean setClassLoader = this.setContextClassLoader;
		ClassLoader existingClassLoader = null;
		
		if (setClassLoader) {
			existingClassLoader = Thread.currentThread().getContextClassLoader();
			
			//TODO can we guarrantee this is the same as the bean class loader
			Thread.currentThread().setContextClassLoader(applicationContext.getClassLoader());
		}
		r.lock();
		try {
			delegateServlet.service(request, response);
		}
		finally {
			r.unlock();
			if (setClassLoader) {
				Thread.currentThread().setContextClassLoader(existingClassLoader);
			}
		}
	}

	@Override
	protected WebApplicationContext initWebApplicationContext() throws BeansException {
		w.lock();
		try {
			WebApplicationContext context = createContext();
			getServletContext().setAttribute(WebConstants.SERVLET_MODULE_ATTRIBUTE + getServletName(), this);
			return context;
		}
		finally {
			w.unlock();
		}		
	}
	
	@Override
	public void destroy() {
		getServletContext().removeAttribute(WebConstants.SERVLET_MODULE_ATTRIBUTE + getServletName());
		super.destroy();
	}	
	
	protected WebApplicationContext createContext() {
		return this.applicationContext;
	}
	
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		if (!(applicationContext instanceof WebApplicationContext)) {
			// FIXME test
			throw new ConfigurationException("Servlet " + getServletName()
					+ " is not backed by a "
					+ WebApplicationContext.class.getName() + ": "
					+ applicationContext);
		}

		this.applicationContext = (WebApplicationContext) applicationContext;
	}
	
	/* ************************ injected setters ************************** */

	public void setDelegateServlet(HttpServlet delegateServlet) {
		this.delegateServlet = delegateServlet;
	}

	public void setSetContextClassLoader(boolean setContextClassLoader) {
		this.setContextClassLoader = setContextClassLoader;
	}

}
