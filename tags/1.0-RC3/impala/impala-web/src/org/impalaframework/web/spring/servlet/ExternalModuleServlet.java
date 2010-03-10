/*
 * Copyright 2007-2010 the original author or authors.
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

import org.impalaframework.web.spring.helper.FrameworkServletContextCreator;
import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;

/**
 * <p>
 * This Spring MVC dispatcher servlet is designed to be used in a
 * <code>servlet</code> module. It's name derives from the fact that it needs
 * to be defined externally to the module which contains it's resources (spring
 * config file, controllers, etc.), specifically in <code>WEB-INF/web.xml</code>.
 * </p>
 * <p>
 * Unlike <code>DispatcherServlet</code> and other subclasses of
 * <code>FrameworkServlet</code>, <code>ExternalModuleServlet</code> is NOT
 * responsible for instantiating it's own application context. Instead, it is
 * connected in a one to one manner with an Impala module whose name matches the
 * servlet name as defined in web.xml.
 * </p>
 * <p>
 * In order to use <code>ExternalModuleServlet</code>, you will need a module
 * definition, typically in <code>moduledefinitions.xml</code>, for the web
 * application, as well as an entry in <code>web.xml</code>.
 * </p>
 * <p>
 * Note that if you publish this servlet in web.xml with the init parameter
 * <code>publishServlet</code>, it can be found via a
 * <code>ModuleProxyServlet</code> mapping. Normally, however, this will
 * be of much more use for an <code>InternalModuleServlet</code>, which has
 * no corresponding <code>web.xml</code> definition. See the documentation of
 * this class to determine how this mapping is made.
 * </p>
 * 
 * @see org.impalaframework.web.spring.integration.ModuleProxyServlet
 * @see org.springframework.web.servlet.FrameworkServlet;
 * @author Phil Zoio
 */
public class ExternalModuleServlet extends BaseExternalModuleServlet {

	private static final long serialVersionUID = 1L;

	private FrameworkServletContextCreator helper;

	public ExternalModuleServlet() {
		super();
		this.helper = new FrameworkServletContextCreator(this);
	}

	@Override
	protected WebApplicationContext createWebApplicationContext() throws BeansException {
		return this.helper.createWebApplicationContext();
	}

	@Override
	protected WebApplicationContext initWebApplicationContext()
			throws BeansException {
		WebApplicationContext initContext = super.initWebApplicationContext();
		return initContext;
	}
	
}
