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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * State holder for injectible, dynamically modifiable {@link Date} value.
 * 
 * @author Phil Zoio
 */
public class DatePropertyValue extends BasePropertyValue implements InitializingBean {
    
    private static final Log logger = LogFactory.getLog(DatePropertyValue.class);   

    private Date defaultValue;
    private String rawValue;
    private String pattern;
    private Date value;
    
    public DatePropertyValue() {
        super();
    }

    public DatePropertyValue(PropertySource propertySource, String name, String pattern, Date defaultValue) {
        super(propertySource, name, defaultValue);
        Assert.notNull(pattern);
        this.pattern = pattern;
        this.defaultValue = defaultValue;
    }

    public void init() {
        Assert.notNull(pattern, "Pattern cannot be null");
    }

    public void afterPropertiesSet() throws Exception {
        init();
    }

    public synchronized Date getValue() {
        String rawValue = super.getRawValue();
        if (rawValue == null) {
            value = defaultValue;
        }
        else if (!rawValue.equals(this.rawValue)) {
            try {
                this.value = new SimpleDateFormat(pattern).parse(rawValue);
                this.rawValue = rawValue;
            } catch (ParseException e) {
                logger.error("Property " + rawValue + " is not a number");
            }
        }
        return value;
    }

    public void setDefaultValue(Date defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
    
}
