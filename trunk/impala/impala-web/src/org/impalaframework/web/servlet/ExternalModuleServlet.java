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

import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;

public class ExternalModuleServlet extends BaseImpalaServlet {
	
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

}
