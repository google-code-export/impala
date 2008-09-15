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

package org.impalaframework.web.integration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.impalaframework.web.helper.FrameworkServletContextCreator;
import org.impalaframework.web.helper.ImpalaServletUtils;
import org.impalaframework.web.servlet.invoker.ReadWriteLockingInvoker;
import org.impalaframework.web.servlet.invoker.ThreadContextClassLoaderHttpServiceInvoker;
import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.FrameworkServlet;

/**
 * Servlet base class which preforms similar function to <code>BaseImpalaServlet</code>
 * except that it does not participate in the Spring MVC dispatch infrastructure, hence
 * it subclasses directly from <code>FrameworkServlet</code>, and not from
 * <code>DispatcherServlet</code>.
 * @author Phil Zoio
 */
public class ExternalFrameworkIntegrationServlet extends FrameworkServlet {

	private static final long serialVersionUID = 1L;
	
	private boolean setClassLoader = true;

	private FrameworkServletContextCreator helper;
	private ReadWriteLockingInvoker invoker;
	private ClassLoader currentClassLoader;
	
	public ExternalFrameworkIntegrationServlet() {
		super();
		this.helper = new FrameworkServletContextCreator(this);
	}
	
	@Override
	protected void doService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//FIXME - need to test and create example for this
		WebApplicationContext wac = this.getWebApplicationContext();
		HttpServlet delegateServlet = (HttpServlet) wac.getBean("delegateServlet");
		
		ClassLoader moduleClassLoader = wac.getClassLoader();
		if (this.invoker == null || this.currentClassLoader != moduleClassLoader) {
			ThreadContextClassLoaderHttpServiceInvoker threadContextClassLoaderInvoker 
				= new ThreadContextClassLoaderHttpServiceInvoker(delegateServlet, setClassLoader, moduleClassLoader);
			
			this.invoker = new ReadWriteLockingInvoker(threadContextClassLoaderInvoker);
			this.currentClassLoader = moduleClassLoader;
		}
		
		this.invoker.invoke(request, response);
	}

	@Override
	protected WebApplicationContext initWebApplicationContext() throws BeansException {
		if (invoker != null) invoker.writeLock();
		try {
			return createContext();
		}
		finally {
			if (invoker != null) invoker.writeUnlock();
		}
	}

	private WebApplicationContext createContext() {
		WebApplicationContext wac = this.helper.createWebApplicationContext();
		return ImpalaServletUtils.publishWebApplicationContext(this, wac);
	}
}
