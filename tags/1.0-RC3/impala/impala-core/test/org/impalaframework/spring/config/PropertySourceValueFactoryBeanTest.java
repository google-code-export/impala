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

package org.impalaframework.spring.config;

import java.util.Properties;

import org.impalaframework.config.StaticPropertiesPropertySource;

import junit.framework.TestCase;

public class PropertySourceValueFactoryBeanTest extends TestCase {
    
    private PropertySourceValueFactoryBean factoryBean;
    private StaticPropertiesPropertySource propertySource;
    private Properties properties;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        factoryBean = new PropertySourceValueFactoryBean();
        properties = new Properties();
        propertySource = new StaticPropertiesPropertySource(properties);
        factoryBean.setPropertySource(propertySource);
        factoryBean.setName("name");
    }

    public void testDefaultMethods() throws Exception {
        assertEquals(String.class, factoryBean.getObjectType());
        assertEquals(true, factoryBean.isSingleton());
        assertNull(factoryBean.getObject());
    }
    
    public void testDefaultValue() throws Exception {
        factoryBean.setDefaultValue("defValue");
        assertEquals("defValue", factoryBean.getObject());
    }
    
    public void testSuppliedValue() throws Exception {
        factoryBean.setDefaultValue("defValue");
        properties.setProperty("name", "othervalue");
        assertEquals("othervalue", factoryBean.getObject());
    }

    public void testNull() {
        
    }

}
