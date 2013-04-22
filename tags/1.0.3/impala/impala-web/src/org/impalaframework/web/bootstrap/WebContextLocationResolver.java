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
        addJmxOperations(configSettings, propertySource);
        
        BooleanPropertyValue servletContextPartitioned = new BooleanPropertyValue(propertySource, WebBootstrapProperties.PARTITIONED_SERVLET_CONTEXT, WebBootstrapProperties.PARTITIONED_SERVLET_CONTEXT_DEFAULT);
        BooleanPropertyValue sessionModuleProtected = new BooleanPropertyValue(propertySource, WebBootstrapProperties.SESSION_MODULE_PROTECTION, WebBootstrapProperties.SESSION_MODULE_PROTECTION_DEFAULT);
        BooleanPropertyValue preserveSessionOnReloadFailure = new BooleanPropertyValue(propertySource, WebBootstrapProperties.PRESERVE_SESSION_ON_RELOAD_FAILURE, WebBootstrapProperties.PRESERVE_SESSION_ON_RELOAD_FAILURE_DEFAULT);

        configSettings.addProperty(WebBootstrapProperties.PARTITIONED_SERVLET_CONTEXT, servletContextPartitioned);
        configSettings.addProperty(WebBootstrapProperties.SESSION_MODULE_PROTECTION, sessionModuleProtected);
        configSettings.addProperty(WebBootstrapProperties.PRESERVE_SESSION_ON_RELOAD_FAILURE, preserveSessionOnReloadFailure);
        
        addPathModuleMapper(configSettings, propertySource);
    }

    protected void addJarModuleLocation(ConfigurationSettings configSettings, PropertySource propertySource) {
        BooleanPropertyValue embeddedMode = new BooleanPropertyValue(propertySource, WebBootstrapProperties.EMBEDDED_MODE, WebBootstrapProperties.EMBEDDED_MODE_DEFAULT);
        configSettings.addProperty(WebBootstrapProperties.EMBEDDED_MODE, embeddedMode);
        
        if (!embeddedMode.getValue()) {
            configSettings.add("META-INF/impala-web-jar-module-bootstrap.xml");
        }
    }

    protected void addJmxOperations(ConfigurationSettings configSettings, PropertySource propertySource) {
        BooleanPropertyValue enableJmxOperations = new BooleanPropertyValue(propertySource, WebBootstrapProperties.ENABLE_WEB_JMX_OPERATIONS, WebBootstrapProperties.ENABLE_WEB_JMX_OPERATIONS_DEFAULT);
        configSettings.addProperty(WebBootstrapProperties.ENABLE_WEB_JMX_OPERATIONS, enableJmxOperations);
        
        if (enableJmxOperations.getValue()) {
            configSettings.add("META-INF/impala-web-jmx-bootstrap.xml");
        }
    }

    protected void addAutoReloadListener(ConfigurationSettings configSettings, PropertySource propertySource) {
        BooleanPropertyValue autoReloadModules = new BooleanPropertyValue(propertySource, WebBootstrapProperties.AUTO_RELOAD_MODULES, WebBootstrapProperties.AUTO_RELOAD_MODULES_DEFAULT);
        configSettings.addProperty(WebBootstrapProperties.AUTO_RELOAD_MODULES, autoReloadModules);
        
        if (autoReloadModules.getValue()) {
            configSettings.add("META-INF/impala-web-listener-bootstrap.xml");
    
            BooleanPropertyValue useTouchFile = new BooleanPropertyValue(propertySource, WebBootstrapProperties.USE_TOUCH_FILE, WebBootstrapProperties.USE_TOUCH_FILE_DEFAULT);
            configSettings.addProperty(WebBootstrapProperties.USE_TOUCH_FILE, useTouchFile);
            
            StringPropertyValue touchFile = new StringPropertyValue(propertySource, WebBootstrapProperties.TOUCH_FILE, WebBootstrapProperties.TOUCH_FILE_DEFAULT);
            configSettings.addProperty(WebBootstrapProperties.TOUCH_FILE, touchFile);
            
            IntPropertyValue delay = new IntPropertyValue(propertySource, WebBootstrapProperties.AUTO_RELOAD_CHECK_DELAY, WebBootstrapProperties.AUTO_RELOAD_CHECK_DELAY_DEFAULT);
            configSettings.addProperty(WebBootstrapProperties.AUTO_RELOAD_CHECK_DELAY, delay);
            
            IntPropertyValue interval = new IntPropertyValue(propertySource, WebBootstrapProperties.AUTO_RELOAD_CHECK_INTERVAL, WebBootstrapProperties.AUTO_RELOAD_CHECK_INTERVAL_DEFAULT);
            configSettings.addProperty(WebBootstrapProperties.AUTO_RELOAD_CHECK_INTERVAL, interval);
            
            StringPropertyValue monitoringType = new StringPropertyValue(propertySource, WebBootstrapProperties.AUTO_RELOAD_MONITORING_TYPE, WebBootstrapProperties.AUTO_RELOAD_MONITORING_TYPE_DEFAULT);
            configSettings.addProperty(WebBootstrapProperties.AUTO_RELOAD_MONITORING_TYPE, monitoringType);
            
            if ("stagingDirectory".equals(monitoringType.getValue())) {
                StringPropertyValue stagingDirectory = new StringPropertyValue(propertySource, WebBootstrapProperties.AUTO_RELOAD_STAGING_DIRECTORY, WebBootstrapProperties.AUTO_RELOAD_STAGING_DIRECTORY_DEFAULT);
                configSettings.addProperty(WebBootstrapProperties.AUTO_RELOAD_STAGING_DIRECTORY, stagingDirectory);
            }
            
            StringPropertyValue fileIncludes = new StringPropertyValue(propertySource, WebBootstrapProperties.AUTO_RELOAD_FILE_INCLUDES, WebBootstrapProperties.AUTO_RELOAD_FILE_INCLUDES_DEFAULT);
            configSettings.addProperty(WebBootstrapProperties.AUTO_RELOAD_FILE_INCLUDES, fileIncludes);

            StringPropertyValue fileExcludes = new StringPropertyValue(propertySource, WebBootstrapProperties.AUTO_RELOAD_FILE_EXCLUDES, WebBootstrapProperties.AUTO_RELOAD_FILE_EXCLUDES_DEFAULT);
            configSettings.addProperty(WebBootstrapProperties.AUTO_RELOAD_FILE_EXCLUDES, fileExcludes);
        }
    }

    protected void addPathModuleMapper(ConfigurationSettings configSettings, PropertySource propertySource) {
        BooleanPropertyValue pathMapperEnabled = new BooleanPropertyValue(propertySource, WebBootstrapProperties.MODULE_PREFIX_MAPPING_ENABLED, WebBootstrapProperties.MODULE_PREFIX_MAPPING_ENABLED_DEFAULT);
        configSettings.addProperty(WebBootstrapProperties.MODULE_PREFIX_MAPPING_ENABLED, pathMapperEnabled);
        
        if (pathMapperEnabled.getValue()) {
            configSettings.add("META-INF/impala-web-path-mapper-bootstrap.xml");
            
            StringPropertyValue webModulePrefix = new StringPropertyValue(propertySource, WebBootstrapProperties.WEB_MODULE_PREFIX, WebBootstrapProperties.WEB_MODULE_PREFIX_DEFAULT);
            configSettings.addProperty(WebBootstrapProperties.WEB_MODULE_PREFIX, webModulePrefix);
        }   
    }

    protected void addDefaultLocations(ConfigurationSettings configSettings) {
        configSettings.add("META-INF/impala-bootstrap.xml");
        configSettings.add("META-INF/impala-web-bootstrap.xml");
    }

}
