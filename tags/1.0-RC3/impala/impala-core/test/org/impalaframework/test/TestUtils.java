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

package org.impalaframework.test;

import java.io.File;

import org.impalaframework.constants.LocationConstants;
import org.impalaframework.util.PathUtils;

import junit.framework.TestCase;

public class TestUtils extends TestCase {

    public static String getCompileRoot() {
        String workspaceRoot = System.getProperty("compile." + LocationConstants.WORKSPACE_ROOT_PROPERTY);
        if (workspaceRoot != null) {
            return workspaceRoot;
        }
        return "../";       
    }
    
    public static String getCompileDirectory(String projectName) {
        String prefix = getWorkspaceRoot();
        String suffix = projectName + "/bin";
        return PathUtils.getPath(prefix, suffix);
    }

    public static File getCompileFile(String projectName) {
        return new File(getCompileDirectory(projectName));
    }
    
    public static String getWorkspaceRoot() {
        String workspaceRoot = System.getProperty("test." + LocationConstants.WORKSPACE_ROOT_PROPERTY);
        if (workspaceRoot != null) {
            return workspaceRoot;
        }
        return "../";
    }
    
    public static String getWorkspacePath(String relativePath) {
        String prefix = getWorkspaceRoot();
        String suffix = relativePath;
        return PathUtils.getPath(prefix, suffix);
    }
    
    public static File getWorkspaceFile(String relativePath) {
        return new File(getWorkspacePath(relativePath));
    }
    
}
