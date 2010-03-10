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

package org.impalaframework.web.module;

import javax.servlet.ServletContext;

import org.impalaframework.web.utils.ServletContextUtils;

public class WebModuleUtils {

    public static String getParamValue(ServletContext servletContext, String paramName) {
        String resourceName = null;
        
        // first look for System property which contains module definitions location
        final boolean supportsContextPath = ServletContextUtils.isAtLeast25(servletContext);
        
        if (supportsContextPath) {
            String contextPath = ServletContextUtils.getContextPathWithoutSlash(servletContext);
            resourceName = System.getProperty(contextPath + "." + paramName);
        }
        
        if (resourceName == null) {
            resourceName = System.getProperty(paramName);
        }
        
        if (resourceName == null) {
            resourceName = servletContext.getInitParameter(paramName);
        }
        return resourceName;
    }

}
