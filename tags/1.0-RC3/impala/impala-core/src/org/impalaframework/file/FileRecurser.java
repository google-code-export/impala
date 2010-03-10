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

/**
 * Class which handles recursion through a file system. 
 * {@link #recurse(FileRecurseHandler, File)} calls recursively through 
 * directories.
 * 
 * @author Phil Zoio
 */
public class FileRecurser {

    public void recurse(FileRecurseHandler handler, File file) {
        if (file.isDirectory()) {
            handler.handleDirectory(file);

            File[] files = file.listFiles(handler.getDirectoryFilter());
            for (File subfile : files) {
                if (subfile.isFile()) {
                    handler.handleFile(subfile);
                }
                else if (subfile.isDirectory()) {
                    recurse(handler, subfile);
                }
            }
        }
        else {
            handler.handleFile(file);
        }
    }

}
