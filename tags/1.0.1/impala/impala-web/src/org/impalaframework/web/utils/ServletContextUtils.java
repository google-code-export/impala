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

package org.impalaframework.web.utils;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class holding utility {@link ServletContext} related methods.
 * @author Phil Zoio
 */
public abstract class ServletContextUtils {
    
    private static final Log logger = LogFactory.getLog(ServletContextUtils.class);

    public static boolean isAtLeast25(ServletContext servletContext) {
        return servletContext.getMajorVersion() >= 2 && servletContext.getMinorVersion() >= 5;
    }

    public static String getContextPathWithoutSlash(ServletContext servletContext) {
        String contextPath;
        try {
            contextPath = servletContext.getContextPath();
            if (contextPath.startsWith("/")) {
                contextPath = contextPath.substring(1);
            }
            return contextPath;
        }
        catch (NoSuchMethodError e) {
            logger.warn(NoSuchMethodError.class.getName() + " called when attempting to call ServletContext.getContextPath(). Check that you have packaged your application with a Servlet API jar for version 2.5 or later");
            return null;
        }
    }

}
