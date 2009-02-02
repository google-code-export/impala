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

package org.impalaframework.web.spring.integration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.util.ObjectUtils;
import org.impalaframework.web.servlet.invoker.ReadWriteLockingInvoker;
import org.impalaframework.web.servlet.invoker.ThreadContextClassLoaderHttpServiceInvoker;
import org.impalaframework.web.spring.helper.FrameworkServletContextCreator;
import org.impalaframework.web.spring.helper.ImpalaServletUtils;
import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.FrameworkServlet;

/**
 * Servlet base class which performs similar function to <code>BaseImpalaServlet</code>
 * except that it does not participate in the Spring MVC dispatch infrastructure, hence
 * it subclasses directly from <code>FrameworkServlet</code>, and not from
 * <code>DispatcherServlet</code>.
 * 
 * @author Phil Zoio
 */
public class ExternalFrameworkIntegrationServlet extends FrameworkServlet {

	private static final long serialVersionUID = 1L;
	
	private boolean setClassLoader = true;

	private FrameworkServletContextCreator frameworkContextCreator;
	private ReadWriteLockingInvoker invoker;
	private ClassLoader currentClassLoader;
	
	private String delegateServletBeanName = "delegateServlet";
	
	public ExternalFrameworkIntegrationServlet() {
		super();
		this.frameworkContextCreator = new FrameworkServletContextCreator(this);
	}
	
	@Override
	protected void doService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
		WebApplicationContext wac = this.getWebApplicationContext();
		
		HttpServlet delegateServlet = ObjectUtils.cast(wac.getBean(delegateServletBeanName),
				HttpServlet.class);
		
		if (delegateServlet == null) {
			throw new ConfigurationException("No Servlet registered under name " + delegateServletBeanName);
		}
		
		ClassLoader moduleClassLoader = wac.getClassLoader();
		if (this.invoker == null || this.currentClassLoader != moduleClassLoader) {
			ThreadContextClassLoaderHttpServiceInvoker threadContextClassLoaderInvoker 
				= new ThreadContextClassLoaderHttpServiceInvoker(delegateServlet, setClassLoader, moduleClassLoader);
			
			this.invoker = new ReadWriteLockingInvoker(threadContextClassLoaderInvoker);
			this.currentClassLoader = moduleClassLoader;
		}
		
		this.invoker.invoke(request, response, null);
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
	
	@Override
	public void destroy() {
		ImpalaServletUtils.unpublishWebApplicationContext(this);
		super.destroy();
	}

	/* *************** Helper methods ************** */
	
	protected WebApplicationContext createContext() {
		WebApplicationContext wac = this.frameworkContextCreator.createWebApplicationContext();
		publishContext(wac);
		return wac;
	}

	protected void publishContext(WebApplicationContext wac) {
		ImpalaServletUtils.publishWebApplicationContext(wac, this);
	}

	void setFrameworkContextCreator(FrameworkServletContextCreator helper) {
		this.frameworkContextCreator = helper;
	}

	/**
	 * Delegate servlet bean name in module Spring configuration file
	 */
	public void setDelegateServletBeanName(String delegateServletBean) {
		this.delegateServletBeanName = delegateServletBean;
	}
}
