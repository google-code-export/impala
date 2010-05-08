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

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class ExternalPropertiesFactoryBeanTest extends TestCase {

    private ExternalPropertiesFactoryBean source;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        source = new ExternalPropertiesFactoryBean();
        source.setFileName("reload/reloadable.properties");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        System.clearProperty("property.folder");
    }

    public void testGetLocations() {
        Resource[] locations = source.getLocations();
        assertEquals(1, locations.length);
        assertTrue(locations[0] instanceof ClassPathResource);
    }

    public void testGetLocationsWithNonExistFile() {
        System.setProperty("property.folder", "nonexist");
        Resource[] locations = source.getLocations();
        assertEquals(1, locations.length);
    }

    public void testGetLocationsWithValidFile() {
        System.setProperty("property.folder", "../impala-core/files");
        Resource[] locations = source.getLocations();
        assertEquals(2, locations.length);
        assertTrue(locations[1] instanceof FileSystemResource);
    }
    
    public void testAfterPropertiesSetWithFileSystemValue() throws Exception {
        System.setProperty("property.folder", "../impala-core/files");
        source.afterPropertiesSet();
        Properties properties = (Properties) source.getObject();
        System.out.println(properties);
        assertEquals("value4", properties.getProperty("property1"));
    }
    
    public void testAfterPropertiesSetWithClassPathValue() throws Exception {
        source.afterPropertiesSet();
        Properties properties = (Properties) source.getObject();
        System.out.println(properties);
        assertEquals("value7", properties.getProperty("property1"));
    }

}
