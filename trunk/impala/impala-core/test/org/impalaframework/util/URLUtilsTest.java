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

package org.impalaframework.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.impalaframework.spring.resource.DirectoryResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class URLUtilsTest extends TestCase {

    private File[] files;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        File file1 = new File("../impala-core/src");
        File file2 = new File("../impala-repository/test/junit-3.8.2.jar");
        files = new File[] { file1, file2 };
    }

    public final void testCreateFromFiles() throws Exception {
        URL[] urls = URLUtils.createUrls(files);
        checkUrls(urls);
    }

    public final void testCreateFromResources() throws Exception {

        List<Resource> resources = new ArrayList<Resource>();
        for (File file : files) {
            if (file.isDirectory())
                resources.add(new DirectoryResource(file));
            else
                resources.add(new FileSystemResource(file));
        }
        URL[] urls = URLUtils.createUrls(resources);
        checkUrls(urls);
    }

    private void checkUrls(URL[] urls) throws IOException {
        assertEquals(2, urls.length);

        assertTrue(urls[0].getFile().endsWith("/impala-core/src/"));
        assertTrue(urls[1].getFile().endsWith("impala-repository/test/junit-3.8.2.jar"));

        for (URL url : urls) {
            assertNotNull(url.openConnection());
            assertNotNull(url.openStream());
        }
    }

}
