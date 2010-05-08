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

import java.text.SimpleDateFormat;
import java.util.Properties;

import junit.framework.TestCase;

import org.impalaframework.config.StaticPropertiesPropertySource;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class DynamicPropertiesNamespaceHandlerTest extends TestCase {
    
    public void testDefault() throws Exception {
        
        GenericApplicationContext context = new GenericApplicationContext();
        
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
        reader.loadBeanDefinitions(new ClassPathResource("org/impalaframework/spring/config/dynamicproperties-context.xml"));
        
        context.refresh();
        DynamicPropertiesBean bean = (DynamicPropertiesBean) context.getBean("testBean");
        bean.print();
        
        assertEquals(true, bean.getBooleanValue());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse("1999-12-12"), bean.getDateValue());
        assertEquals(100.1, bean.getDoubleValue());
        assertEquals(10.2F, bean.getFloatValue());
        assertEquals(100, bean.getIntValue());
        assertEquals(2010L, bean.getLongValue());
        assertEquals("Phil", bean.getStringValue());
        
        StaticPropertiesPropertySource propertySource = (StaticPropertiesPropertySource) context.getBean("propertySource");
        final Properties properties = propertySource.getProperties();
        properties.setProperty("string.property", "Phil Z");
        
        //check that this updates
        assertEquals("Phil Z", bean.getStringValue());
    }
    
}
