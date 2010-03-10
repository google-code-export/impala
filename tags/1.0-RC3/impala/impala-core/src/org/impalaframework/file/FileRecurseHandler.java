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

package org.impalaframework.file;

import java.io.File;
import java.io.FileFilter;

/**
 * Defines callbacks to be used with {@link FileRecurser}. 
 * 
 * @author Phil Zoio
 */
public interface FileRecurseHandler {

    /**
     * Returns the {@link FileFilter} used to filter the list of files in a directory for
     * which {@link #handleFile(File)} will be called.
     */
    FileFilter getDirectoryFilter();

    /**
     * Callback method on a file within a directory found by {@link FileRecurser}
     */
    void handleFile(File subfile);

    /**
     * Callback method on a directory found by {@link FileRecurser}
     */
    void handleDirectory(File directory);

}
