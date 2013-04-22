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

package org.impalaframework.build.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileNameMapper;

/**
 * Implementation of {@link FileNameMapper} which copies files to the target
 * package as specified in the {@link #to} property.
 * 
 * @author Phil Zoio
 */
public class CopyPackageMapper implements FileNameMapper {
    
    private String to;

    public String[] mapFileName(String sourceFileName) {
        if (to == null) {
            throw new BuildException("No 'to' attribute set for " + this.getClass().getName());
        }
        
        //simply add package name to source file name
        final String replace = to.replace('.', '/') + (to.endsWith("/") ? "" : "/");
        return new String[] { replace + sourceFileName };
    }

    public void setFrom(String from) {
    }

    public void setTo(String to) {
        this.to = to;
    }

}
