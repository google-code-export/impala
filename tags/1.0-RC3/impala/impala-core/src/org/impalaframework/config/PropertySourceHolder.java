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


/**
 * Singleton which holds a {@link PropertySource} instance. Typically used to hold Impala
 * configuration properties before the Impala Spring application context is
 * bootstrapped.
 * 
 * Has synchronized getter and setter for {@link PropertySource} instance.
 * 
 * @author Phil Zoio
 */
public final class PropertySourceHolder {
    
    private static final PropertySourceHolder instance = new PropertySourceHolder();
    
    private PropertySource propertySource;

    /**
     * Private constructor to enforce constructor
     */
    private PropertySourceHolder() {
        super();
    }

    public synchronized PropertySource getPropertySource() {
        return propertySource;
    }

    public synchronized void setPropertySource(PropertySource propertySource) {
        this.propertySource = propertySource;
    }

    public synchronized void clearPropertySource() {
        this.propertySource = null;
    }

    public static PropertySourceHolder getInstance() {
        return instance;
    }

}
