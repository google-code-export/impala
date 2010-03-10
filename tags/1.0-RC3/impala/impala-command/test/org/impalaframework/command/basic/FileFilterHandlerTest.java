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
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class FileFilterHandlerTest extends TestCase {
    
    public void testFiltering() throws Exception {
        FileFilterHandler handler = new FileFilterHandler();
        final ArrayList<File> files = new ArrayList<File>();
        files.add(new File("../impala"));
        files.add(new File("../impala-core"));
        files.add(new File("../impala-interactive"));
        files.add(new File("../impala-web"));
        final List<File> result = handler.filter(files, new FileFilter() {
            int count;
            public boolean accept(File pathname) {
                count++;
                return count % 2 == 0;
            }
        });
        
        assertEquals(2, result.size());
    }
}
