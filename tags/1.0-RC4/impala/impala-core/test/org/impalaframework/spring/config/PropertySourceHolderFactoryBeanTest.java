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

import junit.framework.TestCase;

import org.impalaframework.config.PropertySource;
import org.impalaframework.config.PropertySourceHolder;
import org.impalaframework.config.StaticPropertiesPropertySource;

public class PropertySourceHolderFactoryBeanTest extends TestCase {

    public void testBasicMethods() throws Exception {
        PropertySourceHolderFactoryBean bean = new PropertySourceHolderFactoryBean();
        assertEquals(PropertySource.class, bean.getObjectType());
        assertEquals(true, bean.isSingleton());
        assertTrue(bean.getObject() instanceof StaticPropertiesPropertySource);
        StaticPropertiesPropertySource source = (StaticPropertiesPropertySource) bean.getObject();
        assertTrue(source.getProperties().isEmpty());
    }

    public void testSetProperties() throws Exception {
        final Properties properties = new Properties();
        PropertySourceHolder.getInstance().setPropertySource(new StaticPropertiesPropertySource(properties));
        
        PropertySourceHolderFactoryBean bean = new PropertySourceHolderFactoryBean();

        assertTrue(bean.getObject() instanceof StaticPropertiesPropertySource);
        StaticPropertiesPropertySource source = (StaticPropertiesPropertySource) bean.getObject();
        assertEquals(properties, source.getProperties());
    }
    
}
