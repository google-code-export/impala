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

package org.impalaframework.web.resolver;

import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;

import org.impalaframework.resolver.ModuleLocationResolver;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.ServletContextResource;

/**
 * Implementation of {@link ModuleLocationResolver} designed for use when modules are deployed as JARs in 
 * <code>WEB-INF/modules</code>. The scheme depends on the application version, specified by default in 
 * <i>impala.properties</i>
 * 
 * For a module name 'mymodule', and <i>application.version</i> = 1.0, the module 'mymodule' will be found in 
 * <code>WEB-INF/modules/mymodule-1.0.jar</code>
 * 
 * @author Phil Zoio
 */
public class ServletContextModuleLocationResolver implements ModuleLocationResolver, ServletContextAware, InitializingBean {

    private String applicationVersion;

    private String relativeModuleRootLocation = "/WEB-INF/modules";

    private ServletContext servletContext;

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(relativeModuleRootLocation);
    }

    public Resource getRootDirectory() {
        return new ServletContextResource(servletContext, relativeModuleRootLocation);
    }
    
    public List<Resource> getApplicationModuleClassLocations(String moduleName) {
        String applicationVersionString = StringUtils.hasText(applicationVersion) ? "-" + applicationVersion : "";
        String fullResourceName = relativeModuleRootLocation + "/" + moduleName + applicationVersionString + ".jar";
        Resource servletContextResource = new ServletContextResource(servletContext, fullResourceName);
        return Collections.singletonList(servletContextResource);
    }

    public List<Resource> getModuleTestClassLocations(String moduleName) {
        throw new UnsupportedOperationException();
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public void setRelativeModuleRootLocation(String relativeModuleRootLocation) {
        this.relativeModuleRootLocation = relativeModuleRootLocation;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
