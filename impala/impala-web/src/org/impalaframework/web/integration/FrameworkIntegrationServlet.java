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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.impalaframework.web.helper.FrameworkServletContextCreator;
import org.impalaframework.web.helper.ImpalaServletUtils;
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
public abstract class FrameworkIntegrationServlet extends FrameworkServlet {

	private static final long serialVersionUID = 1L;

	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	private final Lock r = rwl.readLock();
	private final Lock w = rwl.writeLock();

	private FrameworkServletContextCreator helper;
	
	public FrameworkIntegrationServlet() {
		super();
		this.helper = new FrameworkServletContextCreator(this);
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		r.lock();
		try {
			//FIXME wire in connectivity
			WebApplicationContext wac = this.getWebApplicationContext();
			//look for servlet instance
			
			HttpServlet servlet = null;
			
			servlet.service(request, response);
		}
		finally {
			r.unlock();
		}
	}

	@Override
	protected WebApplicationContext initWebApplicationContext() throws BeansException {
		w.lock();
		try {
			return createContext();
		}
		finally {
			w.unlock();
		}
	}

	private WebApplicationContext createContext() {
		WebApplicationContext wac = this.helper.createWebApplicationContext();
		return ImpalaServletUtils.initWithContext(this, wac);
	}
}
