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

package org.impalaframework.registry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.impalaframework.exception.NoServiceException;
import org.impalaframework.util.ObjectUtils;
import org.springframework.util.Assert;

/**
 * Base class for registry implementations
 * @author Phil Zoio
 */
@SuppressWarnings("unchecked")
public class RegistrySupport {

    private final Map entries;

    public RegistrySupport() {
        super();
        this.entries = new HashMap();
    }

    public RegistrySupport(Map entries) {
        super();
        Assert.notNull(entries, "entries cannot be null");
        this.entries = entries;
    }


    public <T extends Object> T getEntry(String key, Class<T> type) {
        
        return getEntry(key, type, true);
    }   
    
    public <T extends Object> T getEntry(String key, Class<T> type, boolean mandatory) {
        
        Assert.notNull(key, "key cannot be null");
        Assert.notNull(type, "type cannot be null");
        
        key = key.toLowerCase();
        
        Object value = entries.get(key);
        if (mandatory && value == null) {
            throw new NoServiceException("No instance of " + type.getName()
                    + " available for key '" + key + "'. Available entries: " + entries.keySet());
        }
        
        return ObjectUtils.cast(value, type);
    }
    
    public void addRegistryItem(String key, Object value) {
        
        Assert.notNull(key, "key cannot be null");
        Assert.notNull(value, "value cannot be null");
        
        this.entries.put(key.toLowerCase(), value);
    }
    
    public void setEntries(Map entries) {
        
        Assert.notNull(entries, "entries cannot be null");
        
        final Set<String> keySet = entries.keySet();
        for (String key : keySet) {
            this.entries.put(key.toLowerCase(), entries.get(key));
        }
    }
    
    public <T extends Object> Map<String, T> getEntries() {
        return Collections.unmodifiableMap(entries);
    }
    
    public <T extends Object> Object removeEntry(String key) {
        return this.entries.remove(key);
    }
    
    public void clear() {
        this.entries.clear();
    }
    
}
