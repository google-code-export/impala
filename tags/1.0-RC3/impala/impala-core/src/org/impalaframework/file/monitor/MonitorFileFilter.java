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

package org.impalaframework.file.monitor;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

/**
 * Implementation of {@link FileFilter} which uses includes and excludes passed in via constructor
 * to determine whether a particular file should be accepted in {@link #accept(File)}.
 * 
 * Note that if file name ends with one of the includes list, {@link #accept(File)} will return true
 * Otherwise, if file name ends with one of the excludes list, {@link #accept(File)} will return false
 * 
 * If file name matches none of passed in excludes, then what happens depends on whether includes are specified.
 * If includes are not empty, false is returned, otherwise, true is returned.
 * 
 * Note that hidden files (beginning with .) are always excluded.
 * 
 * @author Phil Zoio
 */
public class MonitorFileFilter implements FileFilter {
    
    private Log logger = LogFactory.getLog(MonitorFileFilter.class);
    
    private final List<String> includes;
    
    private final List<String> excludes;

    public MonitorFileFilter(List<String> includes, List<String> excludes) {
        super();
        Assert.notNull(includes);
        Assert.notNull(excludes);
        this.includes = new ArrayList<String>(includes);
        this.excludes = new ArrayList<String>(excludes);
    }

    public boolean accept(File file) {

        final boolean doAccept = doAccept(file);
        if (doAccept) {
            if (logger.isDebugEnabled())
                logger.debug("Checking for timestamp for file: " + file.getName());
        } else {
            if (logger.isTraceEnabled())
            logger.trace("Not checking for timestamp for file: " + file.getName());
        }
        return doAccept;
    }

    private boolean doAccept(File file) {

        String name = file.getName();
        if (name.startsWith(".")) {
            return false;
        }  
        
        if (file.isDirectory()) {
            return true;
        }
            
        if (includes.isEmpty() && excludes.isEmpty()) {
            return true;
        }
        
        for (String include : includes) {
            if (name.endsWith(include)) {
                return true;
            }
        }
        
        for (String exclude : excludes) {
            if (name.endsWith(exclude)) {
                return false;
            }
        }
        
        return includes.isEmpty();
    }

}
