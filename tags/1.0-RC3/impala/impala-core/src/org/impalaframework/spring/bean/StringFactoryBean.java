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

/**
 * A {@link FactoryBean} implementation which returns a
 * <code>String</code>.
 * @author Phil Zoio
 */
public class StringFactoryBean implements FactoryBean {

    private String value;

    public Object getObject() throws Exception {
        return value;
    }

    public Class<?> getObjectType() {
        return String.class;
    }

    public boolean isSingleton() {
        return true;
    }

    /**
     * Sets the value of the String to be returned
     * @param value the value returned using <code>getObject()</code>
     */
    public void setValue(String value) {
        this.value = value;
    }
}
