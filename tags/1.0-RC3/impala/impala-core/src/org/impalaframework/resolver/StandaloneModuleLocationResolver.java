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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.impalaframework.constants.LocationConstants;
import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.util.CollectionStringUtils;
import org.impalaframework.util.PathUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

/**
 * Implementation of {@link ModuleLocationResolver} designed for use in standalone environments. Used in interactive test runner.
 * Module classes and tests both by default go in the <i>bin</i> directory. These can be overridden, either as system property
 * or wired in via {@link Properties} in constructor. Relevant property names are:
 * {@link LocationConstants#MODULE_CLASS_DIR_PROPERTY} and  {@link LocationConstants#MODULE_TEST_DIR_PROPERTY}
 * @author Phil Zoio
 */
public class StandaloneModuleLocationResolver extends BaseModuleLocationResolver {

    public StandaloneModuleLocationResolver() {
        super();
        init();
    }

    public StandaloneModuleLocationResolver(Properties properties) {
        super(properties);
        init();
    }
    
    protected void init() {
        super.init();

        // the module directory which is expected to contain classes
        mergeProperty(LocationConstants.MODULE_CLASS_DIR_PROPERTY, LocationConstants.MODULE_CLASS_DIR_DEFAULT, null);

        // the parent directory in which tests are expected to be found
        mergeProperty(LocationConstants.MODULE_TEST_DIR_PROPERTY, LocationConstants.MODULE_TEST_DIR_DEFAULT, null);
    }

    public List<Resource> getModuleTestClassLocations(String moduleName) {
        String classDir = StringUtils.cleanPath(getProperty(LocationConstants.MODULE_TEST_DIR_PROPERTY));
        return getResources(moduleName, classDir);
    }

    public List<Resource> getApplicationModuleClassLocations(String moduleName) {
        String classDir = getProperty(LocationConstants.MODULE_CLASS_DIR_PROPERTY);
        return getResources(moduleName, classDir);
    }
    

    /**
     * Returns the file representing the workspace root as as a {@link FileSystemResource}
     * @throws ConfigurationException if workspace root resource location does not exist.
     * Uses abstract {@link #getWorkspaceRoot()} to determine what the workspace root location is.
     */
    public Resource getRootDirectory() {
        String workspace = getWorkspaceRoot();
        if (workspace != null) {
            File candidate = new File(workspace);

            if (!candidate.exists()) {
                throw new ConfigurationException("'workspace.root' (" + workspace + ") does not exist");
            }
            if (!candidate.isDirectory()) {
                throw new ConfigurationException("'workspace.root' (" + workspace + ") is not a directory");
            }
            return new FileSystemResource(candidate);
        }
        return new FileSystemResource("../");
    }

    /**
     * Returns the workspace root directory, determined from {@link #getRootDirectory()}, as an absolute path String
     */
    protected String getRootDirectoryPath() {
        Resource rootDirectory = getRootDirectory();
        
        if (rootDirectory == null) {
            throw new ConfigurationException("Unable to determine application's root directory. Has the property 'workspace.root' been set?");
        }
        
        String absolutePath = null;
        try {
            absolutePath = rootDirectory.getFile().getAbsolutePath();
        }
        catch (IOException e) {
            throw new ConfigurationException("Unable to obtain path for root directory: " + rootDirectory);
        }
        return StringUtils.cleanPath(absolutePath);
    }

    protected List<Resource> getResources(String moduleName, String classDir) {
        List<String> classDirs = CollectionStringUtils.parseStringList(classDir);
        List<Resource> resources = new ArrayList<Resource>();
        
        for (String cdir : classDirs) {
            String path = PathUtils.getPath(getRootDirectoryPath(), moduleName);
            path = PathUtils.getPath(path, cdir);
            Resource resource = new FileSystemResource(path);
            if (resource.exists()) {
                resources.add(resource);
            }
        }
        return resources;
    }

}
