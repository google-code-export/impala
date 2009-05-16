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

package org.impalaframework.classloader;

import java.io.File;

import junit.framework.TestCase;

public class URLClassRetrieverTest extends TestCase {

    private URLClassRetriever retriever;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    public void testToString() {
        File file1 = new File("../impala-core/files/MyTestClass.jar");
        File file2 = new File("../impala-core/files/impala-classloader/module-a/bin");
        retriever = new URLClassRetriever(new File[]{ file1, file2 });
        System.out.println(retriever);
    }
    
    public void testFindBytesFromJar() {
        File file = new File("../impala-core/files/MyTestClass.jar");
        retriever = new URLClassRetriever(new File[]{ file });
        
        assertNull(retriever.getClassBytes("duffclass"));
        assertNotNull(retriever.getClassBytes("example.test.MyTestClass"));
    }

    public void testFindResourceFromJar() {
        File file = new File("../impala-core/files/MyTestClass.jar");
        retriever = new URLClassRetriever(new File[]{ file });

        assertNull(retriever.findResource("duffclass"));
        assertNotNull(retriever.findResource("example/test/MyTestClass.class"));
    }
    
    public void testFindBytesFromDirectory() {
        File file = new File("../impala-core/files/impala-classloader/module-a/bin");
        retriever = new URLClassRetriever(new File[]{ file });
        
        assertNull(retriever.getClassBytes("duff"));
        assertNotNull(retriever.getClassBytes("A"));
    }

    public void testFindResourceFromDirectory() {
        File file = new File("../impala-core/files/impala-classloader/module-a/bin");
        retriever = new URLClassRetriever(new File[]{ file });

        assertNull(retriever.findResource("duff"));
        assertNotNull(retriever.findResource("A.class"));
    }

}
