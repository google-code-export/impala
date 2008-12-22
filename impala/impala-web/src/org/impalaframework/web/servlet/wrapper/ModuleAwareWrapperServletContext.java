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

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletContext;

import org.impalaframework.classloader.BaseURLClassLoader;
import org.impalaframework.classloader.NonDelegatingResourceClassLoader;
import org.impalaframework.web.spring.helper.ImpalaServletUtils;

/**
 * Implementation of <code>ServletContext</code> which overrides some methods
 * in the <code>DelegatingWrapperServletContext</code> superclass. Note that
 * this class does not include any special behaviour for
 * <code>getRealPath</code>. Calls to this method are delegated to the
 * wrapped <code>ServletContext</code>.
 * 
 * @author Phil Zoio
 */
public class ModuleAwareWrapperServletContext extends
		DelegatingWrapperServletContext {
	
	private final ClassLoader moduleClassLoader;
	private final String moduleName;

	public ModuleAwareWrapperServletContext(ServletContext realContext, String moduleName, ClassLoader moduleClassLoader) {
		super(realContext);
		if (moduleClassLoader instanceof BaseURLClassLoader) {
			//use NonDelegatingResourceClassLoader in order that it only looks in locations of the moduleClassLoader, but not it's parents
			this.moduleClassLoader = new NonDelegatingResourceClassLoader((BaseURLClassLoader) moduleClassLoader);
		} else {
			this.moduleClassLoader = moduleClassLoader;
		}
		this.moduleName = moduleName;
	}
	
	/**
	 * First attempts to find resource in module's class path. If not found,
	 * calls the superclass, which results in a search to the usual
	 * <code>ServletContext</code> resource directory. This allows resources
	 * which would otherwise need to be placed in a servlet context folder (e.g.
	 * WEB-INF) to instead be placed in the module class path. Note that only
	 * the locations associated explicitly with the module's class loader are
	 * searched. Parent locations are not searched.
	 */
	@Override
	public URL getResource(String path) throws MalformedURLException {
		
		String tempPath = path;
		
		//remove the leading slash
		if (tempPath.startsWith("/")) {
			tempPath = tempPath.substring(1);
		}
		
		URL resource = moduleClassLoader.getResource(tempPath);
		if (resource != null) return resource;
		
		return super.getResource(path);
	}

	/**
	 * Exhibits same behaviour are {@link #getResource(String)}, but
	 * returns <code>InputStream</code> instead of <code>URL</code>.
	 */
	@Override
    public InputStream getResourceAsStream(String path)
    {
        try
        {
            URL url= this.getResource(path);
            if (url == null)
                return null;
            return url.openStream();
        }
        catch(Exception e)
        {
            log(e.getMessage(), e);
            return null;
        }
    }

	@Override
	public Object getAttribute(String name) {
		
		String moduleKey = ImpalaServletUtils.getModuleServletContextKey(moduleName, name);
		Object moduleAttribute = super.getAttribute(moduleKey);
		if (moduleAttribute != null) {
			return moduleAttribute;
		}
		
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

}
