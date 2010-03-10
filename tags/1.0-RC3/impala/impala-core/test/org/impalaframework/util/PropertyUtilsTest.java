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

package org.impalaframework.util;

import java.net.URL;
import java.util.Properties;

import junit.framework.TestCase;

import org.impalaframework.exception.ExecutionException;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Phil Zoio
 */
public class PropertyUtilsTest extends TestCase {
    
    private ClassPathResource resource;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        resource = new ClassPathResource("reload/reloadable.properties");
    }
    
    public void testResourceLoadProperties() throws Exception {
        Properties props = PropertyUtils.loadProperties(resource);
        assertNotNull(props.getProperty("property1"));
    }

    public void testResourceLoadDuffProperties() throws Exception {
        try {
            ClassPathResource resource = new ClassPathResource("propertiesthatdontexist");
            PropertyUtils.loadProperties(resource);
        }
        catch (ExecutionException e) {
            assertEquals("Unable to load properties file class path resource [propertiesthatdontexist]", e.getMessage());
        }
    }
    
    public void testURLLoadProperties() throws Exception {
        Properties props = PropertyUtils.loadProperties(resource.getURL());
        assertNotNull(props.getProperty("property1"));
    }

    public void notestURLLoadDuffProperties() throws Exception {
        try {
            URL resource = new URL("http://dontexist.com");
            PropertyUtils.loadProperties(resource);
        }
        catch (ExecutionException e) {
            assertEquals("Unable to load properties file http://dontexist.com", e.getMessage());
        }
    }
}
