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

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

/**
 * @author Phil Zoio
 */
public class PathUtilsTest extends TestCase {

    public void testGetCurrentDirectoryName() {
        try {
            assertEquals("impala-core", PathUtils.getCurrentDirectoryName());
        }
        catch (AssertionFailedError e) {
            assertEquals("tests", PathUtils.getCurrentDirectoryName());
        }
    }

    public void testGetPath() {
        assertEquals("/", PathUtils.getPath("", ""));
        assertEquals("/", PathUtils.getPath("/", ""));
        assertEquals("/", PathUtils.getPath(null, null));
        assertEquals("/", PathUtils.getPath("", null));
        assertEquals("/", PathUtils.getPath(null, ""));
        assertEquals("/myprefix/deploy/classes", PathUtils.getPath("/myprefix", "deploy/classes"));
        assertEquals("/myprefix/deploy/classes", PathUtils.getPath("/myprefix", "/deploy/classes"));
        assertEquals("/myprefix/deploy/classes", PathUtils.getPath("/myprefix/", "/deploy/classes"));
    }
    
    public void testTrimPrefix() throws Exception {

        assertEquals(null, PathUtils.trimPrefix(null, "/myprefix"));
        assertEquals("", PathUtils.trimPrefix("", "/myprefix"));
        assertEquals("", PathUtils.trimPrefix("/myprefix", "/myprefix"));
        assertEquals("/extra", PathUtils.trimPrefix("/myprefix/extra", "/myprefix"));
        assertEquals("/myprefix", PathUtils.trimPrefix("/myprefix", "/myprefix/extra"));
    }

}
