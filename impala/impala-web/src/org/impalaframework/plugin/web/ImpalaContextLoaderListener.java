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

//FIXME add spring copyright notice

package org.impalaframework.plugin.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.springframework.util.ClassUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;

public class ImpalaContextLoaderListener extends ContextLoaderListener {
	
	//FIXME test
	
	private ContextLoader contextLoader;
	
	private Class defaultContextLoaderClass = WebXmlBasedContextLoader.class;

	/**
	 * Initialize the root web application context.
	 */
	public void contextInitialized(ServletContextEvent event) {
		ServletContext servletContext = event.getServletContext();
		this.contextLoader = createContextLoader(servletContext);
		this.contextLoader.initWebApplicationContext(servletContext);
	}

	/**
	 * Create the ContextLoader to use. Can be overridden in subclasses.
	 * @param servletContext 
	 * @return the new ContextLoader
	 */
	protected ContextLoader createContextLoader(ServletContext servletContext) {
		String contextLoaderClassName = servletContext.getInitParameter(WebConstants.CONTEXT_LOADER_CLASS_NAME);
		
		Class<? extends ContextLoader> contextLoaderClass = defaultContextLoaderClass;
		
		if (contextLoaderClassName != null) {
			try {
				contextLoaderClass = ClassUtils.forName(contextLoaderClassName);
			}
			catch (Throwable e) {
				// FIXME Auto-generated catch block
				throw new RuntimeException(e);
			}
		}
		
		ContextLoader contextLoader = null;
		try {
			contextLoader = contextLoaderClass.newInstance();
		}
		catch (Exception e) {
			// FIXME Auto-generated catch block
			throw new RuntimeException(e);
		}
		
		return contextLoader;
	}

	/**
	 * Return the ContextLoader used by this listener.
	 * @return the current ContextLoader
	 */
	public ContextLoader getContextLoader() {
		return this.contextLoader;
	}

	/**
	 * Close the root web application context.
	 */
	public void contextDestroyed(ServletContextEvent event) {
		if (this.contextLoader != null) {
			this.contextLoader.closeWebApplicationContext(event.getServletContext());
		}
	}

}
