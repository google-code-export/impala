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

package org.impalaframework.web.spring.helper;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.web.WebConstants;
import org.impalaframework.web.helper.WebServletUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.FrameworkServlet;

/**
 * Class with static convenience methods for publish, accessing, and removing <code>ServletContext</code>-based state. 
 * @author Phil Zoio
 */
public abstract class ImpalaServletUtils {
    
    public static final Log logger = LogFactory.getLog(ImpalaServletUtils.class);

    public static WebApplicationContext checkIsWebApplicationContext(String servletName, ApplicationContext applicationContext) {
        if (!(applicationContext instanceof WebApplicationContext)) {
            throw new ConfigurationException("Servlet '" + servletName + "' is not backed by an application context of type " + WebApplicationContext.class.getName() + ": " + applicationContext);
        }
        return (WebApplicationContext) applicationContext;
    }
    
    public static void publishWebApplicationContext(WebApplicationContext wac, FrameworkServlet servlet) {

        String attrName = servlet.getServletContextAttributeName();
        servlet.getServletContext().setAttribute(attrName, wac);
        
        if (logger.isDebugEnabled()) {
            logger.debug("Published WebApplicationContext of servlet '" + servlet.getServletName()
                    + "' as ServletContext attribute with name [" + attrName + "]");
        }
    }
    
    public static void unpublishWebApplicationContext(FrameworkServlet servlet) {

        String attrName = servlet.getServletContextAttributeName();
        servlet.getServletContext().removeAttribute(attrName);
        
        if (logger.isDebugEnabled()) {
            logger.debug("Removed WebApplicationContext of servlet '" + servlet.getServletName()
                    + "' as ServletContext attribute with name [" + attrName + "]");
        }
    }

    public static ModuleManagementFacade getModuleManagementFacade(ServletContext servletContext) {
        ModuleManagementFacade facade = WebServletUtils.getModuleManagementFacade(servletContext);
    
        if (facade == null) {
            throw new ConfigurationException("Unable to load " + FrameworkServletContextCreator.class.getName()
                    + " as no attribute '" + WebConstants.IMPALA_FACTORY_ATTRIBUTE
                    + "' has been set up. Have you set up your Impala ContextLoader correctly?");
        }
        return facade;
    }
    
}
