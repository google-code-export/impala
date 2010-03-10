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

import java.util.Properties;

import junit.framework.TestCase;

public class StaticPropertiesPropertySourceTest extends TestCase {

    public void testSetProperties() {
        StaticPropertiesPropertySource source = new StaticPropertiesPropertySource();
        
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        source.setProperties(properties);
        
        BasePropertyValue value = new BasePropertyValue();
        value.setPropertySource(source);
        value.setName("property1");
        
        assertEquals("value1", value.getRawValue());
    }
    
    public void testNullProperties() throws Exception {
        StaticPropertiesPropertySource source = new StaticPropertiesPropertySource();
        assertNull(source.getValue("property1"));
    }
    
    public void testProperties() throws Exception {
        BasePropertyValue value = new BasePropertyValue();
        try {
            value.getRawValue();
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("propertySource must be specified", e.getMessage());
        }
        
        StaticPropertiesPropertySource source = new StaticPropertiesPropertySource();
        value.setPropertySource(source);
        try {
            value.getRawValue();
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("name must be specified", e.getMessage());
        }
    }

}
