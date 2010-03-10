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

import java.util.Collection;

import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * Implementation of {@link PropertySource} which will attempt to retrieve a
 * non-null value from a succession of wired in {@link PropertySource}
 * instances. If any of these return a non-null value from a
 * {@link #getValue(String)} call, then {@link #getValue(String)} returns with
 * this returned value.
 * 
 * Implements Gang of Four Composite pattern.
 * 
 * @author Phil Zoio
 */
public class CompositePropertySource implements PropertySource {

    private final Collection<PropertySource> propertySources;
    
    public CompositePropertySource(Collection<PropertySource> propertySources) {
        super();
        Assert.notNull(propertySources, "propertySources cannot be null");
        Assert.notEmpty(propertySources, "propertySources cannot be empty");
        this.propertySources = propertySources;
    }

    public String getValue(String name) {
        Assert.notNull(name, "name cannot be null");
        for (PropertySource propertySource : propertySources) {
            String value = getValue(propertySource, name);
            if (value != null) return value;
        }
        return null;
    }

    protected String getValue(PropertySource propertySource, String name) {
        final String value = propertySource.getValue(name);
        if (value != null) {
            return value;
        }
        return null;
    }
    
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(ObjectUtils.identityToString(this));
        buffer.append(" - propertySources: ");
        buffer.append(propertySources);
        return buffer.toString();
    }

}
