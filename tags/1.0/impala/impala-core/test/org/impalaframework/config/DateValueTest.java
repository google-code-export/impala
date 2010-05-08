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

import java.sql.Date;
import java.util.Properties;

import junit.framework.TestCase;

public class DateValueTest extends TestCase {

    private Properties properties;
    private DatePropertyValue dateValue;
    private Date defaultValue;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        StaticPropertiesPropertySource source = new StaticPropertiesPropertySource();

        properties = new Properties();
        source.setProperties(properties);

        dateValue = new DatePropertyValue();
        dateValue.setName("intProperty");
        dateValue.setPropertySource(source);
        dateValue.setPattern("yyyy-MM-dd");
        
        defaultValue = Date.valueOf("1999-12-12");
    }

    public void testNoValueSet() {
        assertEquals(null, dateValue.getValue());
        dateValue.setDefaultValue(defaultValue);
        assertEquals(Date.valueOf("1999-12-12"), dateValue.getValue());

        properties.setProperty("intProperty", "1999-11-11");
        Date expected = Date.valueOf("1999-11-11");
        Date actual = new Date(dateValue.getValue().getTime());
        assertEquals(expected.getTime(), actual.getTime());
    }
}
