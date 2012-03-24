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

package org.impalaframework.command.basic;

import java.io.File;
import java.io.FileFilter;

public class ClassFindCommandFilter implements FileFilter {

    private String rootPath;
    private String classSegment;
    private String packageSegment;
    
    private String className;
    
    public ClassFindCommandFilter(String rootPath, String classSegment, String packageSegment) {
        super();
        this.rootPath = rootPath;
        this.classSegment = classSegment;
        this.packageSegment = packageSegment;
    }

    public boolean accept(File file) {
        
        if (file.getName().toLowerCase().contains(classSegment.toLowerCase())) {
            
            // calculate relative path
            String absolute = file.getAbsolutePath();
            String relative = absolute.substring(rootPath.length() + 1, absolute.length() - 6);

            boolean add = true;

            className = relative.replace(File.separatorChar, '.');

            if (packageSegment != null) {
                // add if packageSegment matches
                int packagePartDot = className.lastIndexOf('.');
                if (packagePartDot >= 0) {
                    String packagePart = className.substring(0, packagePartDot);
                    add = (packagePart.endsWith(packageSegment));
                }

            }

            return add;
        }
        return false;
    }

    public String getClassName() {
        return className;
    }

}
