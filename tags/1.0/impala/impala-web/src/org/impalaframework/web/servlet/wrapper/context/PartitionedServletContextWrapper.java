/*
 * Copyright 2007-2010 the original author or authors.
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

package org.impalaframework.web.servlet.wrapper.context;

import javax.servlet.ServletContext;

import org.impalaframework.module.ModuleDefinition;
import org.impalaframework.web.servlet.qualifier.WebAttributeQualifier;
import org.impalaframework.web.servlet.wrapper.ServletContextWrapper;
import org.springframework.util.Assert;

/**
 * Implementation of {@link ServletContextWrapper} which returns a
 * {@link PartitionedServletContext} instance if
 * {@link #enablePartitionedServletContext} is set to true. If not, simply
 * returns the passed in {@link ServletContext} instance.
 * 
 * @see org.impalaframework.web.servlet.wrapper.context.PartitionedServletContext
 * @author Phil Zoio
 */
public class PartitionedServletContextWrapper implements ServletContextWrapper {
    
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
            return new PartitionedServletContext(servletContext, applicationId, moduleDefinition.getName(), webAttributeQualifier, classLoader);
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
