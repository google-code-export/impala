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

import javax.servlet.ServletContext;

import org.impalaframework.web.helper.WebServletUtils;

/**
 * Extension of {@link BaseModuleAwareWrapperServletContext}
 * which provides specialised implementation of {@link #getAttribute(String)}.
 * 
 * @author Phil Zoio
 */
public class ModuleAwareWrapperServletContext extends
        BaseModuleAwareWrapperServletContext {

	public ModuleAwareWrapperServletContext(ServletContext realContext,
            String moduleName, ClassLoader moduleClassLoader) {
        super(realContext, moduleName, moduleClassLoader);
    }

    /**
     * For a given attribute name, first looks using a key constructed using the current module name 
     * as prefix. Only if not finding the attribute using this key, it searches using the
     * raw value supplied through the <code>name</code> parameter.
     */
    @Override
	public Object getAttribute(String name) {
		
		String moduleKey = WebServletUtils.getModuleServletContextKey(this.getModuleName(), name);
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
