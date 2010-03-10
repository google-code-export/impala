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

import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.util.PathUtils;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * {@link ModuleLocationResolver} implementation whose {@link #workspaceRoots} property is directly wired in.
 * @author Phil Zoio
 */
public abstract class SimpleBaseModuleLocationResolver implements ModuleLocationResolver {
    
    private String[] workspaceRoots;
    
    public void init() {
        Assert.notNull(workspaceRoots, "workspaceRoots cannot be null");
    }

    protected String[] getWorkspaceRoots() {
        return workspaceRoots;
    }

    public void setWorkspaceRoot(String workspaceRoot) {
        Assert.notNull(workspaceRoot, "workspaceRoot cannot be null");
        String[] rootsArray = StringUtils.tokenizeToStringArray(workspaceRoot, ", ");
        for (int i = 0; i < rootsArray.length; i++) {
            rootsArray[i] = rootsArray[i].trim();
        }
        this.workspaceRoots = rootsArray;
    }

    protected void checkResources(List<Resource> resources, String moduleName,
            String moduleVersion, String rootDirectoryPath, String resourceType) {
        if (resources == null || resources.isEmpty()) {
            throw new InvalidStateException("Unable to find any " + resourceType + " resources in workspace root directory '" 
                    + PathUtils.getAbsolutePath(rootDirectoryPath)
                    + "' for module named '" + moduleName
                    + "' with version '" + moduleVersion + "'");
        }
    }

}
