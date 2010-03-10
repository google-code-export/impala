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
package org.impalaframework.command.framework;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton which holds shared global command properties
 * 
 * @author Phil Zoio
 */
public class GlobalCommandState {

    private GlobalCommandState() {
        super();
    }

    private static GlobalCommandState instance = new GlobalCommandState();

    public static GlobalCommandState getInstance() {
        return instance;
    }

    private Map<String, CommandPropertyValue> properties = new HashMap<String, CommandPropertyValue>();
    private Map<String, Object> values = new HashMap<String, Object>();

    public CommandPropertyValue getProperty(String key) {
        return this.properties.get(key);
    }

    public void addProperty(String key, CommandPropertyValue value) {
        this.properties.put(key, value);
    }
    
    public Object getValue(String key) {
        return this.values.get(key);
    }

    public void addValue(String key, Object value) {
        this.values.put(key, value);
    }

    public void clearProperty(String propertyName) {
        this.properties.remove(propertyName);
    }   

    public void clearValue(String propertyName) {
        this.values.remove(propertyName);
    }   


    public void reset() {
        properties.clear();
        values.clear();
    }

}
