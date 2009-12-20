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

package org.impalaframework.osgi.test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

public class ConfigurableFileFilterTest extends TestCase {
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testAccept() {
        
        File parent = new File("../impala-repository/main");
        
        //impala is not in main
        checkSize(parent, "jetty:*;main:impala", "main:jmx", 0);
        
        //cglib is included in main
        checkSize(parent, "jetty:*;main:impala,cglib", "main:jmx", 1);
        
        //as above with spaces
        checkSize(parent, "jetty: *; main: impala, cglib", " main: jmx", 1);
        
        //now we also exclude cglib
        checkSize(parent, "jetty:*;main:impala,cglib", "main:jmx,cglib", 0);
        
        //impala is not in main. All files should appear
        checkSize(parent, "jetty:jetty;main:*", "main:jmx", 8);
        
        //now we also exclude cglib
        checkSize(parent, "jetty:*", null, 0);
        
    }

    private void checkSize(File parent, String includes, String excludes, int size) {
        ConfigurableFileFilter filter = new ConfigurableFileFilter(includes, excludes);
        List<File> fileList = Arrays.asList(parent.listFiles(filter));
        
        assertEquals(size, fileList.size());
    }

}
