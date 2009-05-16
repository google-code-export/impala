/*
 * Copyright 2007-2008 the original author or authors.
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

import org.impalaframework.file.FileMonitor;
import org.impalaframework.file.FileRecurser;


/**
 * Class with logic for figuring out the last modified date of files in a
 * directory
 * @author Phil Zoio
 */
public class FileMonitorImpl implements FileMonitor {

    public long lastModified(File file) {
        FileMonitorRecurserHandler handler = new FileMonitorRecurserHandler();
        new FileRecurser().recurse(handler, file);
        return handler.getLastModified();
    }

    public long lastModified(File[] files) {
        FileMonitorRecurserHandler handler = new FileMonitorRecurserHandler();
        final FileRecurser fileRecurser = new FileRecurser();
        long lastModified = 0L;
        for (File file : files) {
            fileRecurser.recurse(handler, file);
            lastModified = Math.max(lastModified, handler.getLastModified());
        }
        return lastModified;
    }

}
