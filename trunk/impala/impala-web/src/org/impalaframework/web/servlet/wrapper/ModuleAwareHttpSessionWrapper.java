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
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.module.RuntimeModule;
import org.impalaframework.module.spi.Application;
import org.impalaframework.web.helper.WebServletUtils;
import org.impalaframework.web.servlet.qualifier.WebAttributeQualifier;

/**
 * Factory interface for wrapping {@link HttpSession} object.
 * @author Phil Zoio
 */
public class ModuleAwareHttpSessionWrapper implements HttpSessionWrapper {
    
    private Log logger = LogFactory.getLog(ModuleAwareHttpSessionWrapper.class);
    
    private ServletContext servletContext;
    
    private WebAttributeQualifier webAttributeQualifier;
    
    public HttpSession wrapSession(HttpSession session, String moduleName) {
        
        System.out.println(webAttributeQualifier);
        
        if (session == null) {
            return null;
        }
        ModuleManagementFacade moduleManagementFacade = WebServletUtils.getModuleManagementFacade(servletContext);
        if (moduleManagementFacade != null) {
            
            Application currentApplication = moduleManagementFacade.getApplicationManager().getCurrentApplication();
            RuntimeModule currentModuleContext = currentApplication.getModuleStateHolder().getModule(moduleName);
            
            if (currentModuleContext != null) {
                return new ModuleAwareWrapperHttpSession(session, currentModuleContext.getClassLoader());
            } else {
                logger.warn("No module application context associated with module: " + moduleName + ". Using unwrapped session");
                return session;
            }
        }
        return session;
    }   
    
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
    
    public void setWebAttributeQualifier(WebAttributeQualifier webAttributeQualifier) {
        this.webAttributeQualifier = webAttributeQualifier;
    }
}
