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
 * State holder for injectible, dynamically modifiable <code>boolean</code>
 * value.
 * 
 * @author Phil Zoio
 */
public class BooleanPropertyValue extends BasePropertyValue {

    private boolean defaultValue;
    private String rawValue;
    private boolean value;

    public BooleanPropertyValue() {
        super();
    }

    public BooleanPropertyValue(PropertySource propertySource, String name, boolean defaultValue) {
        super(propertySource, name, defaultValue);
        this.defaultValue = defaultValue;
    }

    public synchronized boolean getValue() {
        String rawValue = super.getRawValue();
        if (rawValue == null) {
            value = defaultValue;
        } else if (!rawValue.equals(this.rawValue)) {
            this.value = Boolean.parseBoolean(rawValue);
            this.rawValue = rawValue;
        }
        return value;
    }

    public void setDefaultValue(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

}
