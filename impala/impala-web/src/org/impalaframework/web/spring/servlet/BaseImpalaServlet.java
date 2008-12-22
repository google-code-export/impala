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

package org.impalaframework.web.spring.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.impalaframework.web.spring.helper.ImpalaServletUtils;
import org.impalaframework.web.spring.servlet.invoker.HttpServiceInvoker;
import org.impalaframework.web.spring.servlet.invoker.ReadWriteLockingInvoker;
import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.util.NestedServletException;

/**
 * Abstract base servlet for extending Spring MVC capabilities with Impala.
 * Wraps <code>doService</code> with a read-write lock so that application context refresh is
 * properly synchronized
 * @author Phil Zoio
 */
public abstract class BaseImpalaServlet extends DispatcherServlet implements HttpServiceInvoker {

	private static final long serialVersionUID = 1L;

	private final ReadWriteLockingInvoker invoker = new ReadWriteLockingInvoker(this);

	public BaseImpalaServlet() {
		super();
	}

	@Override
	protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
		invoker.invoke(request, response, null);
	}
	
	public void invoke(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		try {
			super.doService(request, response);
		} catch (Exception e) {
			throw new NestedServletException("Request processing failed", e);
		}
	}

	@Override
	protected WebApplicationContext initWebApplicationContext() throws BeansException {
		invoker.writeLock();
		try {
			WebApplicationContext wac = createWebApplicationContext();
			//FIXME this probably shouldn't automatically be being called. How to stop it from being called inappropriately
			//However, it must be called
			onRefresh(wac);
			
			ImpalaServletUtils.publishWebApplicationContext(wac, this);
			return wac;
		}
		finally {
			invoker.writeUnlock();
		}
	}
	
	@Override
	public void destroy() {
		ImpalaServletUtils.unpublishWebApplicationContext(this);
		super.destroy();
	}

	protected abstract WebApplicationContext createWebApplicationContext() throws BeansException;

}
