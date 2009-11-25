/*
 * Copyright 2007-2008 the original author or authors.
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

import junit.framework.TestCase;

import org.impalaframework.config.BooleanPropertyValue;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class DynamicPropertiesNamespaceHandlerTest extends TestCase {
    
    public void testDefault() throws Exception {
        
        GenericApplicationContext context = new GenericApplicationContext();
        
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
        reader.loadBeanDefinitions(new ClassPathResource("org/impalaframework/spring/config/dynamicproperties-context.xml"));
        
        context.refresh();
        
        BooleanPropertyValue booleanProperty = (BooleanPropertyValue) context.getBean("booleanProperty");
        assertEquals(true, booleanProperty.getValue());
    }
    
}
