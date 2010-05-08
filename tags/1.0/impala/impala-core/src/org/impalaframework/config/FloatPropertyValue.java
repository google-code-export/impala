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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * State holder for injectible, dynamically modifiable <code>float</code>
 * value.
 * 
 * @author Phil Zoio
 */
public class FloatPropertyValue extends BasePropertyValue {
    
    private static final Log logger = LogFactory.getLog(FloatPropertyValue.class);  

    private float defaultValue;
    private String rawValue;
    private float value;

    public FloatPropertyValue() {
        super();
    }

    public FloatPropertyValue(PropertySource propertySource, String name, float defaultValue) {
        super(propertySource, name, defaultValue);
        this.defaultValue = defaultValue;
    }

    public synchronized float getValue() {
        String rawValue = super.getRawValue();
        if (rawValue == null) {
            value = defaultValue;
        }
        else if (!rawValue.equals(this.rawValue)) {
            try {
                this.value = Float.parseFloat(rawValue);
                this.rawValue = rawValue;
            } catch (NumberFormatException e) {
                logger.error("Property " + rawValue + " is not a number");
            }
        }
        return value;
    }

    public void setDefaultValue(float defaultValue) {
        this.defaultValue = defaultValue;
    }
    
}
