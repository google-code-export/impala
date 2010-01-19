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

package org.impalaframework.web.spring.loader;

import javax.servlet.ServletContext;

import org.impalaframework.constants.LocationConstants;
import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.facade.ModuleManagementFacade;
import org.impalaframework.module.ModuleDefinitionSource;
import org.impalaframework.web.module.WebModuleUtils;
import org.impalaframework.web.module.source.InternalWebXmlModuleDefinitionSource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * Extension of Spring <code>ContextLoader</code> which initializes Spring
 * application context by loading module definitions from a module definitions
 * XML file located on the web application class path (e.g. in WEB-INF/classes).
 * By default, the name of this file will be <code>moduledefinitions.xml</code>,
 * but this can be overridden by supplying the <i>web.xml</i>.
 * <code>init-parameter</code> named <code>bootstrapModulesResource</code>.
 * 
 * @author Phil Zoio
 */
public class ExternalModuleContextLoader extends BaseImpalaContextLoader {
    
    private static final String defaultModuleResourceName = "moduledefinitions.xml";    
    
    @Override
    public ModuleDefinitionSource getModuleDefinitionSource(ServletContext servletContext, ModuleManagementFacade factory) {

        String locationsResourceName = WebModuleUtils.getParamValue(servletContext,
                LocationConstants.BOOTSTRAP_MODULES_RESOURCE_PARAM);

        if (locationsResourceName == null) {
            locationsResourceName = defaultModuleResourceName;
        }

        ResourceLoader resourceLoader = getResourceLoader();
        Resource resource = resourceLoader.getResource(locationsResourceName);

        if (!resource.exists()) {
            throw new ConfigurationException("Module definition XML resource '" + resource.getDescription()
                    + "' does not exist");
        }

        return newModuleDefinitionSource(resource, factory);
    }

    protected ModuleDefinitionSource newModuleDefinitionSource(Resource resource, ModuleManagementFacade factory) {
        InternalWebXmlModuleDefinitionSource moduleDefinitionSource = new InternalWebXmlModuleDefinitionSource(
                factory.getModuleLocationResolver(), 
                factory.getTypeReaderRegistry());
        
        moduleDefinitionSource.setResource(resource);
        return moduleDefinitionSource;
    }

    protected ResourceLoader getResourceLoader() {
        return new DefaultResourceLoader();
    }

}
