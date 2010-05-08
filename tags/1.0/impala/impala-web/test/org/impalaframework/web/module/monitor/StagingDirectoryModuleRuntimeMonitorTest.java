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

import junit.framework.TestCase;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class StagingDirectoryModuleRuntimeMonitorTest extends TestCase {

    private StagingDirectoryFileModuleRuntimeMonitor runtimeMonitor;
    private FileSystemResource resource;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        runtimeMonitor = new StagingDirectoryFileModuleRuntimeMonitor();
        resource = new FileSystemResource("../impala-repository/main/commons-io-1.3.jar");
    }
    
    public void testGetTempResource() throws Exception {
       Resource tempFileResource = runtimeMonitor.getTempFileResource(resource);
       String canonicalPath = tempFileResource.getFile().getCanonicalPath();
       System.out.println(canonicalPath);
       assertTrue(canonicalPath.endsWith("/impala-repository/staging/commons-io-1.3.jar"));
    }

}
