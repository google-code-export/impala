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

package org.impalaframework.web;

import java.io.File;

import junit.framework.TestCase;

public class StartJettyTest extends TestCase {

    public void testInvalidPort() {
        try {
            StartJetty.getPort("notanumber");
            fail();
        }
        catch (IllegalArgumentException e) {
            assertEquals("Invalid port: notanumber", e.getMessage());
        }
    }
    
    public void testValidPort() {
        assertEquals(1000, StartJetty.getPort("1000"));
    }
    
    public void testFileNotExist() {
        try {
            StartJetty.getContextLocation("a_file_does_not_exist");
            fail();
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            assertTrue(e.getMessage().contains("Invalid context directory"));
        }
    }
    
    public void testValidPath() {
        File configLocation = StartJetty.getContextLocation(System.getProperty("java.io.tmpdir"));
        assertTrue(configLocation.exists());
    }
    
    public void testContext() {
        assertEquals("/path",StartJetty.getContextPath("path"));
        assertEquals("/path",StartJetty.getContextPath("/path"));
    }

}
