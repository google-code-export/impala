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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

/**
 * @author Phil Zoio
 */
public class ResourceUtilsTest extends TestCase {
    public void testGetResources() {
        File[] files = new File[] { new File("src"), new File("test") };

        Resource[] resources = ResourceUtils.getResources(files);
        File[] filesAgain = ResourceUtils.getFiles(resources);

        assertTrue(Arrays.equals(files, filesAgain));
    }

    public void testGetClassPathResources() {
        final ClassLoader defaultClassLoader = ClassUtils.getDefaultClassLoader();
        List<String> list = new ArrayList<String>();
        list.add("log4j.properties");
        list.add("parentTestContext.xml");
        
        Resource[] resources = ResourceUtils.getClassPathResources(list, defaultClassLoader);
        
        assertEquals(2, resources.length);
        for (int i = 0; i < resources.length; i++) {
            assertTrue(resources[i].exists());
        }
    }
}
