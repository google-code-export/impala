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

import org.springframework.util.Assert;

/**
 * Base implementation class for property value holder classes such as
 * {@link StringPropertyValue}, {@link IntPropertyValue}, etc.
 * 
 * @author Phil Zoio
 */
public class BasePropertyValue implements PropertyValue {
    
    private String name;
    
    private String rawDefaultValue;
    
    private PropertySource propertySource;
    
    public BasePropertyValue() {
        super();
    }

    public BasePropertyValue(PropertySource propertySource, String name, Object defaultValue) {
        super();
        this.name = name;
        this.propertySource = propertySource;
        this.rawDefaultValue = (defaultValue != null ? defaultValue.toString() : null);
    }

    public final String getRawValue() {
        Assert.notNull(propertySource, "propertySource must be specified");
        Assert.notNull(name, "name must be specified");
        String value = propertySource.getValue(name);
        return value;
    }

    public String getRawDefaultValue() {
        return rawDefaultValue;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPropertySource(PropertySource propertiesSource) {
        this.propertySource = propertiesSource;
    }

}
