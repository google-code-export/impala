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

import java.util.Date;
import java.util.Properties;

import junit.framework.TestCase;

public class PropertyValueTest extends TestCase {

    private Properties properties;
    private IntPropertyValue intValue;
    private LongPropertyValue longValue;
    private FloatPropertyValue floatValue;
    private DoublePropertyValue doubleValue;
    private BooleanPropertyValue booleanValue;
    private StaticPropertiesPropertySource source;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        source = new StaticPropertiesPropertySource();
        properties = new Properties();
        source.setProperties(properties);

        intValue = new IntPropertyValue();
        intValue.setName("intProperty");
        intValue.setPropertySource(source);
        
        longValue = new LongPropertyValue();
        longValue.setName("longProperty");
        longValue.setPropertySource(source);
        
        floatValue = new FloatPropertyValue();
        floatValue.setName("floatProperty");
        floatValue.setPropertySource(source);
        
        doubleValue = new DoublePropertyValue();
        doubleValue.setName("doubleProperty");
        doubleValue.setPropertySource(source);
        
        booleanValue = new BooleanPropertyValue();
        booleanValue.setName("booleanProperty");
        booleanValue.setPropertySource(source);
    }

    public void testConstructors() throws Exception {
        IntPropertyValue intValue = new IntPropertyValue(source, "name", 1);
        assertEquals(1, intValue.getValue());
        LongPropertyValue longValue = new LongPropertyValue(source, "name", 1L);
        assertEquals(1L, longValue.getValue());
        FloatPropertyValue floatValue = new FloatPropertyValue(source, "name", 1.0F);
        assertEquals(1.0F, floatValue.getValue());
        DoublePropertyValue doubleValue = new DoublePropertyValue(source, "name", 1.0);
        assertEquals(1.0, doubleValue.getValue());
        StringPropertyValue stringValue = new StringPropertyValue(source, "name", "somevalue");
        assertEquals("somevalue", stringValue.getValue());
        Date date = new Date();
        DatePropertyValue dateValue = new DatePropertyValue(source, "name", "MM/dd/yy", date);
        assertEquals(date, dateValue.getValue());
        BooleanPropertyValue booleanValue = new BooleanPropertyValue(source, "name", true);
        assertEquals(true, booleanValue.getValue());
    }
    
    public void testNoValueSet() {
        assertEquals(0, intValue.getValue());
        intValue.setDefaultValue(2);
        assertEquals(2, intValue.getValue());

        assertEquals(0L, longValue.getValue());
        longValue.setDefaultValue(2L);
        assertEquals(2L, longValue.getValue());
        
        assertEquals(0.0F, floatValue.getValue());
        floatValue.setDefaultValue(2);
        assertEquals(2.0F, floatValue.getValue());
        
        assertEquals(0.0, doubleValue.getValue());
        doubleValue.setDefaultValue(2);
        assertEquals(2.0, doubleValue.getValue());
    }
    
    public void testIntSetValue() {
        intValue.setDefaultValue(2);
        properties.setProperty("intProperty", "1");
        assertEquals(1, intValue.getValue());

        properties.setProperty("intProperty", "3");
        assertEquals(3, intValue.getValue());
        
        properties.clear();
        assertEquals(2, intValue.getValue());
    }
    
    public void testIntInvalid() {
        intValue.setDefaultValue(2);
        properties.setProperty("intProperty", "1");
        assertEquals(1, intValue.getValue());

        properties.setProperty("intProperty", "invalid");
        assertEquals(1, intValue.getValue());
        
        properties.clear();
        assertEquals(2, intValue.getValue());
    }
    
    public void testLongSetValue() {
        longValue.setDefaultValue(2);
        properties.setProperty("longProperty", "1");
        assertEquals(1, longValue.getValue());

        properties.setProperty("longProperty", "3");
        assertEquals(3, longValue.getValue());
        
        properties.clear();
        assertEquals(2, longValue.getValue());
    }
    
    public void testLongInvalid() {
        longValue.setDefaultValue(2);
        properties.setProperty("longProperty", "1");
        assertEquals(1, longValue.getValue());

        properties.setProperty("longProperty", "invalid");
        assertEquals(1, longValue.getValue());
        
        properties.clear();
        assertEquals(2, longValue.getValue());
    }
    
    public void testFloatSetValue() {
        floatValue.setDefaultValue(2);
        properties.setProperty("floatProperty", "1");
        assertEquals(1.0F, floatValue.getValue());

        properties.setProperty("floatProperty", "3");
        assertEquals(3.0F, floatValue.getValue());
        
        properties.clear();
        assertEquals(2.0F, floatValue.getValue());
    }
    
    public void testFloatInvalid() {
        floatValue.setDefaultValue(2);
        properties.setProperty("floatProperty", "1");
        assertEquals(1.0F, floatValue.getValue());

        properties.setProperty("floatProperty", "invalid");
        assertEquals(1.0F, floatValue.getValue());
        
        properties.clear();
        assertEquals(2.0F, floatValue.getValue());
    }
    
    public void testDoubleSetValue() {
        doubleValue.setDefaultValue(2);
        properties.setProperty("doubleProperty", "1");
        assertEquals(1.0, doubleValue.getValue());

        properties.setProperty("doubleProperty", "3");
        assertEquals(3.0, doubleValue.getValue());
        
        properties.clear();
        assertEquals(2.0, doubleValue.getValue());
    }
    
    public void testDoubleInvalid() {
        doubleValue.setDefaultValue(2);
        properties.setProperty("doubleProperty", "1");
        assertEquals(1.0, doubleValue.getValue());

        properties.setProperty("doubleProperty", "invalid");
        assertEquals(1.0, doubleValue.getValue());
        
        properties.clear();
        assertEquals(2.0, doubleValue.getValue());
    }
    
    public void testBooleanSetValue() {
        booleanValue.setDefaultValue(true);
        properties.setProperty("booleanProperty", "true");
        assertEquals(true, booleanValue.getValue());

        properties.setProperty("booleanProperty", "false");
        assertEquals(false, booleanValue.getValue());
        
        properties.clear();
        assertEquals(true, booleanValue.getValue());
    }
    
    public void testBooleanInvalid() {
        booleanValue.setDefaultValue(true);
        properties.setProperty("booleanProperty", "false");
        assertEquals(false, booleanValue.getValue());

        properties.setProperty("booleanProperty", "invalid");
        assertEquals(false, booleanValue.getValue());
        
        properties.clear();
        assertEquals(true, booleanValue.getValue());
    }
}
