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

import org.impalaframework.util.CollectionStringUtils;
import org.impalaframework.util.PathUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class FileModuleResourceFinder implements ModuleResourceFinder {

    private String classDirectory;
    
    private String resourceDirectory;

    public List<Resource> findResources(String workspaceRootPath,
            String moduleName, String moduleVersion) {
        return getResources(workspaceRootPath, moduleName);
    }
    
    protected List<Resource> getResources(String workspaceRootPath, String moduleName) {
        List<Resource> resources = new ArrayList<Resource>();
        
        maybeAddResources(resources, moduleName, workspaceRootPath, classDirectory); 
        maybeAddResources(resources, moduleName, workspaceRootPath, resourceDirectory); 
        return resources;
    }

    private void maybeAddResources(List<Resource> resources, String moduleName,
            String workspaceRootPath, String moduleClassDirectory) {
        
        if (moduleClassDirectory != null) {
        
            //split using "," separator
            List<String> classDirectories = CollectionStringUtils.parseStringList(moduleClassDirectory);
            
            for (String classDirectory : classDirectories) {
                
                String path = PathUtils.getPath(workspaceRootPath, moduleName);
                path = PathUtils.getPath(path, classDirectory);
                
                Resource resource = new FileSystemResource(path);
                if (resource.exists()) {
                    resources.add(resource);
                }                
            }
        }
    }

    public void setClassDirectory(String classDirectory) {
        this.classDirectory = classDirectory;
    }
    
    public void setResourceDirectory(String resourceDirectory) {
        this.resourceDirectory = resourceDirectory;
    }

}
