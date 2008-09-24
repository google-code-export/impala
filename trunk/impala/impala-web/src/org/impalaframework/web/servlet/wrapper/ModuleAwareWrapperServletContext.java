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

package org.impalaframework.web.servlet.wrapper;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletContext;

import org.impalaframework.web.helper.ImpalaServletUtils;

/**
 * Implementation of <code>ServletContext</code> which overrides some methods
 * in the <code>DelegatingWrapperServletContext</code> superclass.
 * 
 * @author Phil Zoio
 */
public class ModuleAwareWrapperServletContext extends
		DelegatingWrapperServletContext {

	// MIXME test and flesh out methods to be implemented
	
	private final ClassLoader moduleClassLoader;
	private final String moduleName;

	public ModuleAwareWrapperServletContext(ServletContext realContext, String moduleName, ClassLoader moduleClassLoader) {
		super(realContext);
		this.moduleClassLoader = moduleClassLoader;
		this.moduleName = moduleName;
	}
	
	/**
	 * First attempts to find resource in module's class path. If not found,
	 * calls the superclass, which results in a search to the usual
	 * <code>ServletContext</code> resource directory. This allows resources which 
	 * would otherwise need to be placed in a servlet context folder (e.g. WEB-INF) to 
	 * instead be placed in the module class path. 
	 */
	@Override
	public URL getResource(String path) throws MalformedURLException {
		
		String tempPath = path;
		
		//remove the leading slash
		if (tempPath.startsWith("/")) {
			tempPath = tempPath.substring(1);
		}
		
		//FIXME use non-delegating class loader
		URL resource = moduleClassLoader.getResource(tempPath);
		if (resource != null) return resource;
		
		return super.getResource(path);
	}

	@Override
	public Object getAttribute(String name) {
		
		String moduleKey = ImpalaServletUtils.getModuleServletContextKey(moduleName, name);
		Object moduleAttribute = super.getAttribute(moduleKey);
		if (moduleAttribute != null) {
			return moduleAttribute;
		}
		
		//FIXME want attribute to be module specific
		return super.getAttribute(name);
	}

	@Override
	public void removeAttribute(String name) {
		super.removeAttribute(name);
	}

	@Override
	public void setAttribute(String name, Object value) {
		super.setAttribute(name, value);
	}	
	
	/*
	@Override
	public String getRealPath(String path) {
		//FIXME what to do here?
		return super.getRealPath(path);
	}

	@Override
	public InputStream getResourceAsStream(String path) {
		return super.getResourceAsStream(path);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set getResourcePaths(String path) {
		return super.getResourcePaths(path);
	}
	*/

}
