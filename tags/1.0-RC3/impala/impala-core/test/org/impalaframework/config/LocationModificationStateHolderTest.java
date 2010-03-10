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

import java.io.File;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import junit.framework.TestCase;

public class LocationModificationStateHolderTest extends TestCase {

    private LocationModificationStateHolder holder;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        holder = new LocationModificationStateHolder();
    }

    public void testNoResource() {
        holder.setLocations(new Resource[0]);
        assertFalse(holder.isModifiedSinceLastCheck());
        assertFalse(holder.isModifiedSinceLastCheck());
    }
    
    public void testIsModified() throws Exception {
        FileSystemResource resource = new FileSystemResource("../impala-core/files/reload/tomodify.txt");
        holder.setLocation(resource);
        assertFalse(holder.isModifiedSinceLastCheck());
        assertFalse(holder.isModifiedSinceLastCheck());
        
        File file = resource.getFile();
        Thread.sleep(20);
        FileCopyUtils.copy(("some text").getBytes(), file);
        assertTrue(holder.isModifiedSinceLastCheck());
        assertFalse(holder.isModifiedSinceLastCheck());
    }
    
    public void testNotOnFirstCheck() throws Exception {
        FileSystemResource resource = new FileSystemResource("../impala-core/files/reload/tomodify.txt");
        holder.setLocation(resource);
        holder.setReturnOnFirstCheck(true);
        assertTrue(holder.isModifiedSinceLastCheck());
        assertFalse(holder.isModifiedSinceLastCheck());
    }

}
