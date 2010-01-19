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

import org.impalaframework.config.SimplePropertiesLoader;
import org.impalaframework.constants.LocationConstants;
import org.impalaframework.web.module.WebModuleUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

public class ServletContextPropertiesLoader extends SimplePropertiesLoader {
    
    private final ServletContext servletContext;
    
    public ServletContextPropertiesLoader(
            ServletContext servletContext,
            String defaultBootstrapResource) {
        super(defaultBootstrapResource);
        Assert.notNull(servletContext, "servletContext cannot be null");
        this.servletContext = servletContext;
    }

    protected String getResourceName() {
        String bootstrapLocationsResource = WebModuleUtils.getParamValue(servletContext,
                LocationConstants.BOOTSTRAP_LOCATIONS_RESOURCE_PARAM);
        return bootstrapLocationsResource;
    }
    
    protected ResourceLoader getResourceLoader() {
        return new DefaultResourceLoader();
    }
}
