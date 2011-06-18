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

package org.impalaframework.config;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

/**
 * Implementation of {@link PropertySource} which simply as a {@link Properties} instance
 * wired in statically.
 * 
 * @author Phil Zoio
 */
public class StaticPropertiesPropertySource implements PropertySource {

    private static final Log logger = LogFactory.getLog(StaticPropertiesPropertySource.class);
    
    private Properties properties;
    
    public StaticPropertiesPropertySource() {
        super();
    }
    
    public StaticPropertiesPropertySource(Properties properties) {
        super();
        Assert.notNull(properties, "properties cannot be null");
        this.properties = properties;
    }

    public String getValue(String name) {
        Assert.notNull(name, "name cannot be null");
        if (properties == null) {
            logger.warn("Properties is null for property keyed by name '" + name + "'");
            return null;
        }
        return properties.getProperty(name);
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Properties getProperties() {
        return properties;
    }

}
