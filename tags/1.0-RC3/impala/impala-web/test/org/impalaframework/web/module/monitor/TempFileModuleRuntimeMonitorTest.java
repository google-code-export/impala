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

package org.impalaframework.web.module.monitor;

import java.io.File;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

public class TempFileModuleRuntimeMonitorTest extends TestCase {
    
    private TempFileModuleRuntimeMonitor runtimeMonitor;
    private FileSystemResource resource;
    private Resource tempResource;
    private SimpleModuleDefinition definition;
    private File jarFile;
    private File tempFile;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        runtimeMonitor = new TempFileModuleRuntimeMonitor();
        resource = new FileSystemResource("../impala-repository/main/commons-io-1.4.jar");
        tempResource = runtimeMonitor.getTempFileResource(resource);
        definition = new SimpleModuleDefinition("mymod");

        jarFile = new File(System.getProperty("java.io.tmpdir"), "myfile.jar");
        tempFile = new File(System.getProperty("java.io.tmpdir"), "myfile.tmp");
        
        deleteIfExists(jarFile);
        deleteIfExists(tempFile);
    }

    private void deleteIfExists(final File file) {
        if (file.exists()) file.delete();
    }

    public void testCreateTempResource() throws Exception {
        assertTrue(resource.exists());
        
        final Resource relative = resource.createRelative("commons-io-1.4-sources.jar");
        assertTrue(relative.exists());
        
        String path = StringUtils.cleanPath(tempResource.getFile().getPath());
        assertTrue(path.endsWith("main/commons-io-1.4.tmp"));
    }
    
    public void testGetMonitorableLocations() throws Exception {
        final ArrayList<Resource> locations = new ArrayList<Resource>();
        final ArrayList<Resource> monitorable = new ArrayList<Resource>();
        assertEquals(0, runtimeMonitor.getMonitorableLocations(definition, locations).size());
        
        //add jar resource and notice that tempResource is added
        locations.add(resource);
        monitorable.add(tempResource);
        assertEquals(monitorable, runtimeMonitor.getMonitorableLocations(definition, locations));
        
        //add another non-jar resource and does not affect monitorable
        locations.add(0, new FileSystemResource("../"));
        assertEquals(monitorable, runtimeMonitor.getMonitorableLocations(definition, locations));
    }
    
    public void testMaybeCopyResource() throws Exception {
        
        FileCopyUtils.copy(resource.getFile(), jarFile);
        FileCopyUtils.copy(resource.getFile(), tempFile);
        
        assertEquals(2, runtimeMonitor.maybeCopyToResource(new FileSystemResource(jarFile), jarFile));
    }
    
    public void testNoneToCopy() throws Exception {
        
        FileCopyUtils.copy(resource.getFile(), jarFile);
        assertEquals(0, runtimeMonitor.maybeCopyToResource(new FileSystemResource(jarFile), jarFile));
    }
    
}
