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

package org.impalaframework.spring.bean;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * {@link FactoryBean} implementation which allows String value to be extracted from a system property, 
 * falling back to an optional default value if no system property is specified.
 * @author philzoio
 */
public class SystemPropertyFactoryBean implements FactoryBean, InitializingBean {

    private String defaultValue;
    
    private String propertyName;
    
    private String value;

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(propertyName, "propertyName must be set");
        String value = System.getProperty(propertyName);
        
        if (value == null) {
            value = getValueIfPropertyNotFound();
        }
        this.value = value;
    }

    protected String getValueIfPropertyNotFound() {
        return defaultValue;
    }
    
    public Object getObject() throws Exception {
        return value;
    }

    public Class<?> getObjectType() {
        return String.class;
    }

    public boolean isSingleton() {
        return true;
    }

    protected String getPropertyName() {
        return propertyName;
    }

    /* **************** injected setters ************** */
    
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

}
