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

/**
 * Singleton which holds a set of properties. Typically used to hold Impala
 * configuration properties before the Impala Spring application context is
 * bootstrapped.
 * 
 * Has synchronized getter and setter for {@link Properties} instance.
 * 
 * @author Phil Zoio
 */
public final class PropertiesHolder {
    
    private static final PropertiesHolder instance = new PropertiesHolder();
    
    private Properties properties;

    /**
     * Private constructor to enforce constructor
     */
    private PropertiesHolder() {
        super();
    }

    public synchronized Properties getProperties() {
        return properties;
    }

    public synchronized void setProperties(Properties properties) {
        this.properties = properties;
    }

    public static PropertiesHolder getInstance() {
        return instance;
    }

    public void clearProperties() {
        this.setProperties(null);
    }

}
