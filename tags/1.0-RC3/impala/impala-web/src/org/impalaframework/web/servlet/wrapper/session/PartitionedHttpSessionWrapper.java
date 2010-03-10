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

package org.impalaframework.web.servlet.wrapper.session;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.module.RuntimeModule;
import org.impalaframework.module.spi.Application;
import org.impalaframework.web.helper.WebServletUtils;
import org.impalaframework.web.servlet.qualifier.IdentityWebAttributeQualifier;
import org.impalaframework.web.servlet.qualifier.WebAttributeQualifier;
import org.impalaframework.web.servlet.wrapper.HttpSessionWrapper;

/**
 * Implementation of {@link HttpSessionWrapper} which returns a wrapped session.
 * If {@link #enablePartitionedServletContext} is set, then
 * {@link PartitionedHttpSession} is returned with attribute getters and setters
 * qualified as determined using the {@link WebAttributeQualifier} instance. If
 * {@link #enableModuleSessionProtection} is set, then state is protected over
 * module reloads using {@link StateProtectingHttpSession}.
 * 
 * @author Phil Zoio
 */
public class PartitionedHttpSessionWrapper implements HttpSessionWrapper {
    
    private Log logger = LogFactory.getLog(PartitionedHttpSessionWrapper.class);
    
    private ServletContext servletContext;
    
    private WebAttributeQualifier webAttributeQualifier;
    
    private boolean enablePartitionedServletContext;
    
    private boolean enableModuleSessionProtection;
    
    private static IdentityWebAttributeQualifier identityQualifier = new IdentityWebAttributeQualifier();
    
    public HttpSession wrapSession(HttpSession session, String moduleName, String applicationId) {
        
        if (session == null) {
            return null;
        }            
        
        final WebAttributeQualifier webAttributeQualifier = getWebAttributeQualifier();
        
        if (enableModuleSessionProtection) {
            
            if (logger.isDebugEnabled()) {
                logger.debug("Session module protection turned on for session wrapping");
            }
            
            ModuleManagementFacade moduleManagementFacade = WebServletUtils.getModuleManagementFacade(servletContext);
            if (moduleManagementFacade != null) {
                
                Application application = moduleManagementFacade.getApplicationManager().getApplication(applicationId);
                RuntimeModule currentModuleContext = application.getModuleStateHolder().getModule(moduleName);
                
                if (currentModuleContext != null) {
                    return new StateProtectingHttpSession(session, webAttributeQualifier, applicationId, moduleName, currentModuleContext.getClassLoader());
                } else {
                    logger.warn("No module application context associated with module: " + moduleName + ". Using unwrapped session");
                    return session;
                }
            }
        } else if (enablePartitionedServletContext) {
            
            if (logger.isDebugEnabled()) {
                logger.debug("Servlet context partitioning set, but session module protection not enabled");
            }
            
            return new PartitionedHttpSession(session, webAttributeQualifier, applicationId, moduleName);
        }
        
        return session;
    }

    private WebAttributeQualifier getWebAttributeQualifier() {
        final WebAttributeQualifier webAttributeQualifier;
        
        if (enablePartitionedServletContext && this.webAttributeQualifier != null) {
            
            if (logger.isDebugEnabled()) {
                logger.debug("Servlet context partitioning and web attribute qualifier set to " + this.webAttributeQualifier.getClass().getName());
            }
            
            webAttributeQualifier = this.webAttributeQualifier;
            
        } else {
            
            if (logger.isDebugEnabled()) {
                logger.debug("Servlet context partitioning set to " + enablePartitionedServletContext +
                        " and web attribute qualifier set to " + 
                        (this.webAttributeQualifier != null ?
                        this.webAttributeQualifier.getClass().getName() :
                        null));
            }
            
            webAttributeQualifier = identityQualifier;
        }
        return webAttributeQualifier;
    }   
    
    public ServletContext getServletContext() {
        return this.servletContext;
    }
    
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
    
    public void setWebAttributeQualifier(WebAttributeQualifier webAttributeQualifier) {
        this.webAttributeQualifier = webAttributeQualifier;
    }
    
    public void setEnablePartitionedServletContext(boolean enablePartitionedServletContext) {
        this.enablePartitionedServletContext = enablePartitionedServletContext;
    }
    
    public void setEnableModuleSessionProtection(boolean enableModuleSessionProtection) {
        this.enableModuleSessionProtection = enableModuleSessionProtection;
    }
}
