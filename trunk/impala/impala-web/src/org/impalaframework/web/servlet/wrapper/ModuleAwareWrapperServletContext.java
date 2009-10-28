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

import org.springframework.util.Assert;

/**
 * Extension of {@link BaseModuleAwareWrapperServletContext}
 * which provides specialized implementation of {@link #getAttribute(String)}.
 * 
 * @author Phil Zoio
 */
public class ModuleAwareWrapperServletContext extends
        BaseModuleAwareWrapperServletContext {

	public ModuleAwareWrapperServletContext(ServletContext realContext,
            String moduleName, 
            ClassLoader moduleClassLoader) {
        super(realContext, moduleName, moduleClassLoader);
    }
	
	protected String getWriteKeyToUse(String name) {

        Assert.notNull(name);
        
        final String keyToUse;
        if (name.startsWith(SHARED_PREFIX)) {
            keyToUse = name.substring(SHARED_PREFIX.length());
        } else {
            keyToUse = name;
        }
        return keyToUse;
    }

}
