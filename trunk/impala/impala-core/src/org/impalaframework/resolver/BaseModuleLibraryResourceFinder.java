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

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.file.ExtensionFileFilter;
import org.impalaframework.util.PathUtils;
import org.impalaframework.util.ResourceUtils;
import org.springframework.core.io.Resource;

/**
 * Searches for jar in expanded module directory, under the assumption that the
 * module will be found in
 * 
 * workspace_root/module_name/lib_dir
 * 
 * where lib_dir is parameterised
 * 
 * @author Phil Zoio
 */
public abstract class BaseModuleLibraryResourceFinder implements
        ModuleResourceFinder {
    
    private static final Log logger = LogFactory.getLog(BaseModuleLibraryResourceFinder.class);

    protected abstract String getLibraryDirectory();
    
    public BaseModuleLibraryResourceFinder() {
	}

    public List<Resource> findResources(String workspaceRootPath, String moduleName, String moduleVersion) {

        String libraryPath = getLibraryPath(workspaceRootPath, moduleName);
        File internalModulesDirectory = new File(libraryPath);
        if (internalModulesDirectory.exists()) {
            
            final File[] listFiles = internalModulesDirectory.listFiles(new ExtensionFileFilter(".jar"));
            
            if (logger.isDebugEnabled()) {
                logger.debug("Found internal lib directories for module '" + moduleName + "'");
                for (File file : listFiles) {
                    logger.debug("\t"+file.getAbsolutePath());
                }
            }
            
            final Resource[] resources = ResourceUtils.getResources(listFiles);
            return Arrays.asList(resources);
        } else {
            logger.debug("Found no internal lib directories for module '" + moduleName + "'");
        }
        
        return Collections.emptyList();
    }

    protected String getLibraryPath(String workspaceRootPath,
            String moduleName) {
        String path = PathUtils.getPath(workspaceRootPath, moduleName);
        path = PathUtils.getPath(path, getLibraryDirectory());
        return path;
    }

}
