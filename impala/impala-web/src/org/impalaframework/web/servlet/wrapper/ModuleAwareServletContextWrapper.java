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

import org.impalaframework.module.ModuleDefinition;
import org.springframework.util.Assert;

/**
 * Implementation of <code>ServletContextWrapper</code> which returns a
 * <code>ModuleAwareWrapperServletContext</code> instance.
 * 
 * @see org.impalaframework.web.servlet.wrapper.ModuleAwareWrapperServletContext
 * @author Phil Zoio
 */
public class ModuleAwareServletContextWrapper implements ServletContextWrapper {
    
    private boolean enablePartitionedServletContext;

    public ServletContext wrapServletContext(ServletContext servletContext,
            ModuleDefinition moduleDefinition, 
            ClassLoader classLoader) {
        
        if (enablePartitionedServletContext) {
            Assert.notNull(moduleDefinition, "moduleDefinition cannot be null");
            return new ModuleAwareWrapperServletContext(servletContext, moduleDefinition.getName(), classLoader);
        }
        return servletContext;
    }

    public void setEnablePartitionedServletContext(boolean enablePartitionedServletContext) {
        this.enablePartitionedServletContext = enablePartitionedServletContext;
    }

}
