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

package org.impalaframework.resolver;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.exception.InvalidStateException;
import org.springframework.core.io.Resource;

public class CascadingModuleLocationResolverTest extends TestCase {
    
    private CascadingModuleLocationResolver resolver;
    private FileModuleResourceFinder fileFinder;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        resolver = new CascadingModuleLocationResolver();
        resolver.setWorkspaceRoot("../impala-core/files/impala-classloader");
        
        fileFinder = new FileModuleResourceFinder();
        fileFinder.setClassDirectory("bin");
        JarModuleResourceFinder jarFinder = new JarModuleResourceFinder();
        
        List<ModuleResourceFinder> resourceFinders = new ArrayList<ModuleResourceFinder>();
        resourceFinders.add(fileFinder);
        resourceFinders.add(jarFinder);
        
        resolver.setClassResourceFinders(resourceFinders);
        resolver.setApplicationVersion("1.0");
        
        resolver.init();
    }
    
    public void testMultipleRoots() {
        resolver.setWorkspaceRoot("../impala-interactive,../impala-core/files/impala-classloader");
    }
    
    public void testResourceDir() {
        fileFinder.setClassDirectory("duff");
        fileFinder.setResourceDirectory("bin");
        final List<ModuleResourceFinder> singletonList = new ArrayList<ModuleResourceFinder>();
        singletonList.add(fileFinder);
        resolver.setClassResourceFinders(singletonList);
        final List<Resource> moduleLocations = resolver.getApplicationModuleClassLocations("module-a");
        System.out.println(moduleLocations);
    }
    
    public void testGetApplicationModuleClassLocations() {
        doTests();
    }

    private void doTests() {
        assertFalse(resolver.getApplicationModuleClassLocations("module-a").isEmpty());
        assertFalse(resolver.getApplicationModuleClassLocations("module-i").isEmpty());
        assertFalse(resolver.getApplicationModuleClassLocations("module-h").isEmpty());
        
        try {
            resolver.getApplicationModuleClassLocations("module-k").isEmpty();
            fail();
        } catch (InvalidStateException e) {
            System.out.println(e.getMessage());
        }
    }

}
