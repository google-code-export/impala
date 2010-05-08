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

package org.impalaframework.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import junit.framework.TestCase;

import org.springframework.core.io.FileSystemResource;

/**
 * @author Phil Zoio
 */
public class FileUtilsTest extends TestCase {

    public void testGetClassBytesFromFile() throws IOException {
        assertNotNull(FileUtils.getBytes(new File(".classpath")));
    }

    public void testGetClassBytesFromResource() throws IOException {
        assertNotNull(FileUtils.getBytes(new FileSystemResource(".classpath")));
        assertEquals(FileUtils.getBytes(new File(".classpath")).length, FileUtils.getBytes(new FileSystemResource(
                ".classpath")).length);
    }
    
    public void testReadLines() throws Exception {
        InputStream inputStream = new FileSystemResource("../impala-core/resources/beanset.properties").getInputStream();
        Reader reader = new InputStreamReader(inputStream, "UTF8");
        List<String> lines = FileUtils.readLines(reader);
        assertTrue(lines.size() > 0);
        assertEquals("set1=applicationContext-set1.xml", lines.get(0));
    }

}
