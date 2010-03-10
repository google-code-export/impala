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

import java.util.List;


import junit.framework.TestCase;

import org.impalaframework.constants.LocationConstants;
import org.impalaframework.exception.ConfigurationException;
import org.springframework.core.io.Resource;

public class AbstractModuleLocationResolverTest extends TestCase {

    private StandaloneModuleLocationResolver resolver;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.clearProperty(LocationConstants.WORKSPACE_ROOT_PROPERTY);
        resolver = new TestModuleLocationResolver();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        System.clearProperty(LocationConstants.WORKSPACE_ROOT_PROPERTY);
    }
    
    public final void testGetRootDirectoryPath() {
        try {
            resolver.getRootDirectoryPath();
        }
        catch (ConfigurationException e) {
            assertEquals("Unable to determine application's root directory. Has the property 'workspace.root' been set?", e.getMessage());
        }
    }
    
    public void testGetRootDirectoryPathPresent() throws Exception {
        System.setProperty(LocationConstants.WORKSPACE_ROOT_PROPERTY, System.getProperty("java.io.tmpdir"));
        String rootDirectoryPath = resolver.getRootDirectoryPath();
        assertNotNull(rootDirectoryPath);
    }

}

class TestModuleLocationResolver extends StandaloneModuleLocationResolver {

    @Override
    protected String getWorkspaceRoot() {
        return System.getProperty(LocationConstants.WORKSPACE_ROOT_PROPERTY);
    }

    public List<Resource> getApplicationModuleClassLocations(String moduleName) {
        return null;
    }

    public List<Resource> getModuleTestClassLocations(String moduleName) {
        return null;
    }

    public List<String> getRootProjects() {
        return null;
    }

}
