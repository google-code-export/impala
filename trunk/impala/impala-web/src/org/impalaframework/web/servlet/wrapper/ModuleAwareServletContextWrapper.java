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
import org.impalaframework.web.servlet.qualifier.WebAttributeQualifier;
import org.springframework.util.Assert;

/**
 * Implementation of <code>ServletContextWrapper</code> which returns a
 * <code>ModuleAwareWrapperServletContext</code> instance.
 * 
 * @see org.impalaframework.web.servlet.wrapper.PartitionedWrapperServletContext
 * @author Phil Zoio
 */
public class ModuleAwareServletContextWrapper implements ServletContextWrapper {
    
    private boolean enablePartitionedServletContext;
    
    private WebAttributeQualifier webAttributeQualifier;

    public ServletContext wrapServletContext(
            ServletContext servletContext,
            String applicationId,
            ModuleDefinition moduleDefinition, 
            ClassLoader classLoader) {
        
        Assert.notNull(webAttributeQualifier, "webAttributeQualifier cannot be null");
        
        if (enablePartitionedServletContext) {
            Assert.notNull(moduleDefinition, "moduleDefinition cannot be null");
            return new PartitionedWrapperServletContext(servletContext, applicationId, moduleDefinition.getName(), webAttributeQualifier, classLoader);
        }
        return servletContext;
    }

    public void setEnablePartitionedServletContext(boolean enablePartitionedServletContext) {
        this.enablePartitionedServletContext = enablePartitionedServletContext;
    }
    
    public void setWebAttributeQualifier(WebAttributeQualifier webAttributeQualifier) {
        this.webAttributeQualifier = webAttributeQualifier;
    }

}
