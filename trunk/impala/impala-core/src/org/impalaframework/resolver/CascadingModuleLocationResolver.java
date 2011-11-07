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

import java.util.Arrays;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * Extension of {@link SimpleBaseModuleLocationResolver} whose class and test directories 
 * are wired in through dependency injection.
 * @author Phil Zoio
 */
public class CascadingModuleLocationResolver extends SimpleBaseModuleLocationResolver {

    /**
     * Finds module-specific classes and resources
     */
    private List<ModuleResourceFinder> classResourceFinders;
    
    /**
     * Finds third party module-specific libraries
     */
    private List<ModuleResourceFinder> libraryResourceFinders;
    
    private String applicationVersion;

    public List<Resource> getApplicationModuleClassLocations(String moduleName) {
        return getResources(moduleName, this.classResourceFinders, true);
    }
    
    public List<Resource> getModuleSpecificJarLocations(String moduleName) {
        return getResources(moduleName, this.libraryResourceFinders, false);
    }

    /**
     * Invoke {@link ModuleResourceFinder} instances to find {@link Resource} instances
     * @param moduleName the name of the current module
     * @param resourceFinders the list of resource finders to use
     * @param check whether to invoke {@link #checkResources(List, String, String, String, String)}
     * @return the list of resources, which may be empty
     */
    protected List<Resource> getResources(String moduleName, List<ModuleResourceFinder> resourceFinders, boolean check) {
        Assert.notNull(resourceFinders);

        String[] rootPaths = getWorkspaceRoots();   
        
        List<Resource> resources = null;
        
        for (String rootPath : rootPaths) {     
            for (ModuleResourceFinder moduleResourceFinder : resourceFinders) {
                resources = moduleResourceFinder.findResources(rootPath, moduleName, applicationVersion);
                if (!resources.isEmpty()) break;
            }
        }
        
        if (check) {
            //FIXME test
            checkResources(resources, moduleName, applicationVersion, Arrays.toString(rootPaths), "application class");
        }
        return resources;
    }
    
    public List<Resource> getModuleTestClassLocations(String moduleName) {
        throw new UnsupportedOperationException();
    }

    /* ********************* Spring setters ********************* */
    
    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public void setClassResourceFinders(List<ModuleResourceFinder> moduleResourceFinders) {
        this.classResourceFinders = moduleResourceFinders;
    }
    
    public void setLibraryResourceFinders(List<ModuleResourceFinder> libraryResourceFinders) {
        this.libraryResourceFinders = libraryResourceFinders;
    }

}
