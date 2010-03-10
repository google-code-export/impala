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

import org.impalaframework.file.handler.BaseFileRecurseHandler;


/**
 * Class with logic for figuring out the last modified date of files in a
 * directory
 * 
 * @author Phil Zoio
 */
public class FileMonitorRecurserHandler extends BaseFileRecurseHandler {
    
    long date = 0L;
    
    private FileFilter fileFilter;
    
    public FileMonitorRecurserHandler() {
    }
    
    public FileMonitorRecurserHandler(FileFilter fileFilter) {
        super();
        this.fileFilter = fileFilter;
    }

    public FileFilter getDirectoryFilter() {
        return this.fileFilter;
    }

    public void handleFile(File subfile) {
        date = Math.max(date, subfile.lastModified());
    }

    public void handleDirectory(File directory) {
    }

    public long getLastModified() {
        return date;
    }

}
