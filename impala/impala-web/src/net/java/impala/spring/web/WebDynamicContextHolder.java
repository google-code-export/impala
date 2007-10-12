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

import net.java.impala.spring.SpringContextHolder;
import net.java.impala.spring.plugin.SpringContextSpec;

import org.springframework.web.context.WebApplicationContext;

public class WebDynamicContextHolder extends SpringContextHolder {

	private WebApplicationContext parentWebContext;
	
	public WebDynamicContextHolder(DefaultWebApplicationContextLoader applicationContextLoader) {
		super(applicationContextLoader);
	}

	public WebApplicationContext getParentWebApplicationContext() {
		return (WebApplicationContext) getContext();
	}

	public DefaultWebApplicationContextLoader getApplicationContextLoader() {
		return (DefaultWebApplicationContextLoader) super.getContextLoader();
	}

	public void loadParentWebContext(SpringContextSpec pluginSpec) {
		final ClassLoader classLoader = getContext().getClassLoader();
		final DefaultWebApplicationContextLoader contextLoader = getApplicationContextLoader();
		
		
	}
}
