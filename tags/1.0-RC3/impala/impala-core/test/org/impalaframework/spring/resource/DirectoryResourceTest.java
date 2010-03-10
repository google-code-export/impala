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

package org.impalaframework.spring.resource;

import java.io.File;

import org.springframework.core.io.FileSystemResource;

import junit.framework.TestCase;

public class DirectoryResourceTest extends TestCase {

    public void testDirectoryResource() throws Exception {
        FileSystemResource resource = new DirectoryResource("../impala-core/src/");
        assertTrue(resource.getURL().getFile().endsWith("/impala-core/src/"));
    }
    
    public void testNotDirectory() throws Exception {

        File file = new File("../impala-repository/test/junit-3.8.1.jar");
        try {
            new DirectoryResource(file);
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            assertTrue(e.getMessage().contains("junit-3.8.1.jar' is not a directory"));
        }
        
        try {
            new DirectoryResource(file.getAbsolutePath());
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            assertTrue(e.getMessage().contains("junit-3.8.1.jar' is not a directory"));
        }
    }

}
