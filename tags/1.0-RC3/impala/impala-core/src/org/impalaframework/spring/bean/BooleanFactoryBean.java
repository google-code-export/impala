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
 * <code>Boolean</code>. Also gives opportunity to return reverse of
 * wired in value.
 * @author Phil Zoio
 */
public class BooleanFactoryBean implements FactoryBean {

    private boolean reverse;
    
    private boolean value;

    public Object getObject() throws Exception {
        return (reverse ? !value : value);
    }

    public Class<?> getObjectType() {
        return Boolean.class;
    }

    public boolean isSingleton() {
        return true;
    }

    /**
     * Sets the value of the Boolean to be returned
     * @param value the value returned using <code>getObject()</code>
     */
    public void setValue(boolean value) {
        this.value = value;
    }

    /**
     * Whether to return the reverse of the wired in value
     */
    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

}
