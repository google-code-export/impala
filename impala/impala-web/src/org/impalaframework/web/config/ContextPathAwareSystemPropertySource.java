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

package org.impalaframework.web.config;

import javax.servlet.ServletContext;

import org.impalaframework.config.PropertySource;
import org.impalaframework.web.utils.ServletContextUtils;
import org.springframework.util.Assert;

/**
 * Implementation of {@link PropertySource} which will attempt to look for properties
 * prefixed using the context path, as determined using {@link ServletContext#getContextPath()}
 * @author Phil Zoio
 */
public class ContextPathAwareSystemPropertySource implements PropertySource {

    private final ServletContext servletContext;

    public ContextPathAwareSystemPropertySource(ServletContext servletContext) {
        super();
        Assert.notNull(servletContext, "servletContext cannot be null");
        this.servletContext = servletContext;
    }

    /**
     * If current version of {@link ServletContext} is 2.5 or greater, 
     * attempts to retrieve property with '%contextPath%.name', with %contextPath% resolved using
     * {@link ServletContext#getContextPath()}. Otherwise returns null,
     */
    public String getValue(String name) {
        final boolean supportsContextPath = ServletContextUtils.isAtLeast25(servletContext);
        if (supportsContextPath) {
            String contextPath = ServletContextUtils.getContextPathWithoutSlash(servletContext);
            return System.getProperty(contextPath + "." + name);
        }
        
        return null;
    }

}
