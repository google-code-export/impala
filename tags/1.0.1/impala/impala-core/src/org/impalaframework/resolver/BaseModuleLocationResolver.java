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

package org.impalaframework.resolver;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.constants.LocationConstants;
import org.springframework.util.Assert;

/**
 * Base implementation of {@link ModuleLocationResolver} which uses {@link LocationConstants#WORKSPACE_ROOT_PROPERTY}
 * to determine the root property. This property will either be a system property, or wired in via the {@link Properties}
 * constructor. System property applies if present.
 * 
 * @author Phil Zoio
 */
public abstract class BaseModuleLocationResolver implements ModuleLocationResolver {

    private static final Log logger = LogFactory.getLog(BaseModuleLocationResolver.class);

    private Properties properties;

    public BaseModuleLocationResolver() {
        super();
        this.properties = new Properties();
    }

    public BaseModuleLocationResolver(Properties properties) {
        super();
        Assert.notNull(properties);
        this.properties = (Properties) properties.clone();
    }

    protected void init() {
        mergeProperty(LocationConstants.WORKSPACE_ROOT_PROPERTY, null, null);
    }

    protected void mergeProperty(String propertyName, String defaultValue, String extraToSupplied) {
        String systemProperty = System.getProperty(propertyName);
        String value = null;

        if (systemProperty != null) {

            if (logger.isInfoEnabled()) {
                logger.info("Resolved location property '" + propertyName + "' from system property: " + systemProperty);
            }
            value = systemProperty;
        }
        else {
            String suppliedValue = this.properties.getProperty(propertyName);
            if (suppliedValue != null) {
                if (logger.isInfoEnabled())
                    logger.info("Resolved location property '" + propertyName + "' from supplied properties: " + suppliedValue);
                value = suppliedValue;
            }
            else {

                if (logger.isInfoEnabled())
                    logger.info("Unable to resolve location '" + 
                            propertyName + 
                            "' from system property or supplied properties. Using default value: " + 
                            defaultValue);
                value = defaultValue;
            }
        }
        if (value != null) {
            if (extraToSupplied != null) {
                if (!value.endsWith(extraToSupplied)) {
                    value += extraToSupplied;
                }
            }

            this.properties.put(propertyName, value);
        }
    }

    protected String getWorkspaceRoot() {
        return properties.getProperty(LocationConstants.WORKSPACE_ROOT_PROPERTY);
    }

    protected String getProperty(String key) {
        return properties.getProperty(key);
    }

}
