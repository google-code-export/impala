/*
 * Copyright 2007 the original author or authors.
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

package net.java.impala.spring.web;

import javax.servlet.ServletContext;

import net.java.impala.spring.SpringContextHolder;
import net.java.impala.spring.plugin.PluginSpec;

import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;

public class WebDynamicContextHolder extends SpringContextHolder {

	private WebApplicationContext parentWebContext;
	private ServletContext servletContext;
	
	//FIXME convert to use interface for DefaultWebApplicationContextLoader
	public WebDynamicContextHolder(ServletContext servletContext, DefaultWebApplicationContextLoader applicationContextLoader) {
		super(applicationContextLoader);
		Assert.notNull(servletContext);
		this.servletContext = servletContext;
	}

	public WebApplicationContext getParentRootContext() {
		return (WebApplicationContext) getContext();
	}

	public DefaultWebApplicationContextLoader getApplicationContextLoader() {
		return (DefaultWebApplicationContextLoader) super.getContextLoader();
	}

	public void loadParentWebContext(PluginSpec pluginSpec) {
		final WebApplicationContextLoader contextLoader = getApplicationContextLoader();
		this.parentWebContext = contextLoader.loadParentWebContext(getParentRootContext(), pluginSpec, servletContext);
	}

	public WebApplicationContext getParentWebContext() {
		return parentWebContext;
	}
	
	
}
