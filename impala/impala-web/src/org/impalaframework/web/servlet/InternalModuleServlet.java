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

import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.web.WebConstants;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;

public class InternalModuleServlet extends BaseImpalaServlet implements ApplicationContextAware {
	
	private static final long serialVersionUID = 1L;
	
	private WebApplicationContext applicationContext;
	
	public InternalModuleServlet() {
		super();
	}

	@Override
	protected WebApplicationContext createWebApplicationContext() throws BeansException {
		return this.applicationContext;
	}

	@Override
	protected WebApplicationContext initWebApplicationContext()
			throws BeansException {
		WebApplicationContext initContext = super.initWebApplicationContext();
		getServletContext().setAttribute(WebConstants.SERVLET_MODULE_ATTRIBUTE + getServletName(), this);
		return initContext;
	}

	@Override
	public void destroy() {
		getServletContext().removeAttribute(WebConstants.SERVLET_MODULE_ATTRIBUTE + getServletName());
		super.destroy();
	}

	/* ************ injected properties ************ */

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		if (!(applicationContext instanceof WebApplicationContext)) {
			//FIXME test
			throw new ConfigurationException("Servlet " + getServletName() + " is not backed by a " + WebApplicationContext.class.getName() + ": " + applicationContext);
		}
		
		this.applicationContext = (WebApplicationContext) applicationContext;
	}
	
}
