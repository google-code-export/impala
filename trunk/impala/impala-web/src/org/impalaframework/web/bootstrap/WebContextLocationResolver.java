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

package org.impalaframework.web.bootstrap;

import org.impalaframework.bootstrap.ConfigurationSettings;
import org.impalaframework.bootstrap.SimpleContextLocationResolver;
import org.impalaframework.config.BooleanPropertyValue;
import org.impalaframework.config.IntPropertyValue;
import org.impalaframework.config.PropertySource;
import org.impalaframework.config.StringPropertyValue;

public class WebContextLocationResolver extends SimpleContextLocationResolver {
    
    @Override
    public void addCustomLocations(ConfigurationSettings configSettings, PropertySource propertySource) {
        
        super.addCustomLocations(configSettings, propertySource);
        addJarModuleLocation(configSettings, propertySource);
        addAutoReloadListener(configSettings, propertySource);
        
        BooleanPropertyValue servletContextPartitioned = new BooleanPropertyValue(propertySource, WebBootstrapProperties.PARTITIONED_SERVLET_CONTEXT, WebBootstrapProperties.PARTITIONED_SERVLET_CONTEXT_DEFAULT);
        BooleanPropertyValue sessionModuleProtected = new BooleanPropertyValue(propertySource, WebBootstrapProperties.SESSION_MODULE_PROTECTION, WebBootstrapProperties.SESSION_MODULE_PROTECTION_DEFAULT);
        BooleanPropertyValue preserveSessionOnReloadFailure = new BooleanPropertyValue(propertySource, WebBootstrapProperties.PRESERVE_SESSION_ON_RELOAD_FAILURE, WebBootstrapProperties.PRESERVE_SESSION_ON_RELOAD_FAILURE_DEFAULT);

        configSettings.addProperty(WebBootstrapProperties.PARTITIONED_SERVLET_CONTEXT, servletContextPartitioned);
        configSettings.addProperty(WebBootstrapProperties.SESSION_MODULE_PROTECTION, sessionModuleProtected);
        configSettings.addProperty(WebBootstrapProperties.PRESERVE_SESSION_ON_RELOAD_FAILURE, preserveSessionOnReloadFailure);
        
        addPathModuleMapper(configSettings, propertySource);
    }

    protected void addJarModuleLocation(ConfigurationSettings configSettings, PropertySource propertySource) {
        BooleanPropertyValue embeddedMode = new BooleanPropertyValue(propertySource, WebBootstrapProperties.EMBEDDED_MODE, false);
        configSettings.addProperty(WebBootstrapProperties.EMBEDDED_MODE, embeddedMode);
        
        if (!embeddedMode.getValue()) {
            configSettings.add("META-INF/impala-web-jar-module-bootstrap.xml");
        }
    }

    protected void addAutoReloadListener(ConfigurationSettings configSettings, PropertySource propertySource) {
        BooleanPropertyValue autoReloadModules = new BooleanPropertyValue(propertySource, WebBootstrapProperties.AUTO_RELOAD_MODULES, false);
        configSettings.addProperty(WebBootstrapProperties.AUTO_RELOAD_MODULES, autoReloadModules);
        
        if (autoReloadModules.getValue()) {
            configSettings.add("META-INF/impala-web-listener-bootstrap.xml");
    
            BooleanPropertyValue useTouchFile = new BooleanPropertyValue(propertySource, WebBootstrapProperties.USE_TOUCH_FILE, false);
            configSettings.addProperty(WebBootstrapProperties.USE_TOUCH_FILE, useTouchFile);
            
            StringPropertyValue touchFile = new StringPropertyValue(propertySource, WebBootstrapProperties.TOUCH_FILE, "/WEB-INF/modules/touch.txt");
            configSettings.addProperty(WebBootstrapProperties.TOUCH_FILE, touchFile);
            
            IntPropertyValue delay = new IntPropertyValue(propertySource, WebBootstrapProperties.AUTO_RELOAD_CHECK_DELAY, 10);
            configSettings.addProperty(WebBootstrapProperties.AUTO_RELOAD_CHECK_DELAY, delay);
            
            IntPropertyValue interval = new IntPropertyValue(propertySource, WebBootstrapProperties.AUTO_RELOAD_CHECK_INTERVAL, 2);
            configSettings.addProperty(WebBootstrapProperties.AUTO_RELOAD_CHECK_INTERVAL, interval);
            
            StringPropertyValue monitoringType = new StringPropertyValue(propertySource, WebBootstrapProperties.AUTO_RELOAD_MONITORING_TYPE, "default");
            configSettings.addProperty(WebBootstrapProperties.AUTO_RELOAD_MONITORING_TYPE, monitoringType);
        }
    }

    protected void addPathModuleMapper(ConfigurationSettings configSettings, PropertySource propertySource) {
        BooleanPropertyValue pathMapperEnabled = new BooleanPropertyValue(propertySource, WebBootstrapProperties.SPRING_PATH_MAPPING_ENABLED, false);
        configSettings.addProperty(WebBootstrapProperties.SPRING_PATH_MAPPING_ENABLED, pathMapperEnabled);
        
        if (pathMapperEnabled.getValue()) {
            configSettings.add("META-INF/impala-web-path-mapper-bootstrap.xml");
            
            BooleanPropertyValue webModulePrefix = new BooleanPropertyValue(propertySource, WebBootstrapProperties.WEB_MODULE_PREFIX, false);
            BooleanPropertyValue topLevelModuleSuffixes = new BooleanPropertyValue(propertySource, WebBootstrapProperties.TOP_LEVEL_MODULE_SUFFIXES, true);
            
            configSettings.addProperty(WebBootstrapProperties.WEB_MODULE_PREFIX, webModulePrefix);
            configSettings.addProperty(WebBootstrapProperties.TOP_LEVEL_MODULE_SUFFIXES, topLevelModuleSuffixes);
        }   
    }

    protected void addDefaultLocations(ConfigurationSettings configSettings) {
        configSettings.add("META-INF/impala-bootstrap.xml");
        configSettings.add("META-INF/impala-web-bootstrap.xml");
    }

}
