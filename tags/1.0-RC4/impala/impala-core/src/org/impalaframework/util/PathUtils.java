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

package org.impalaframework.util;

import java.io.File;
import java.io.IOException;

import org.springframework.util.StringUtils;

/**
 * @author Phil Zoio
 */
public class PathUtils {
    private static final String FOLDER_SEPARATOR = "/";

    public static String getCurrentDirectoryName() {
        File file = new File(new File("").getAbsolutePath());
        return file.getName();
    }

    public static String getPath(String root, String suffix) {
        if (root == null) {
            root = "";
        }
        if (root.endsWith(FOLDER_SEPARATOR)) {
            root = root.substring(0, root.length()-1);
        }
        if (suffix == null) {
            suffix = FOLDER_SEPARATOR;
        }
        if (!suffix.startsWith(FOLDER_SEPARATOR)) {
            suffix = FOLDER_SEPARATOR + suffix;
        }
        String path = root + suffix;
        return path;
    }
    
    public static String trimPrefix(String value, String prefix) {
        if (value == null) return null;
        
        if (prefix != null && value.startsWith(prefix)) {
            return value.substring(prefix.length());
        }
        
        return value;
    }

    public static String getAbsolutePath(File file) {
        try {
            String canonicalPath = file.getCanonicalPath();
            return StringUtils.cleanPath(canonicalPath);
        } catch (IOException e) {
            return StringUtils.cleanPath(file.getAbsolutePath());
        }
    }

    public static String getAbsolutePath(String path) {
        return getAbsolutePath(new File(path));
    }

}
