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

package org.impalaframework.web.integration;

import junit.framework.TestCase;

public class ModuleProxyUtilsTest extends TestCase {

    public void testGetTopLevelPathSegmentString() {
        assertEquals(null, ModuleProxyUtils.getTopLevelPathSegment("/test/"));
        assertEquals(null, ModuleProxyUtils.getTopLevelPathSegment("/test"));
        assertEquals("app", ModuleProxyUtils.getTopLevelPathSegment("/test/app"));
        assertEquals("app", ModuleProxyUtils.getTopLevelPathSegment("/test/app/stuff"));
        
        assertEquals(null, ModuleProxyUtils.getTopLevelPathSegment("test/"));
        assertEquals(null, ModuleProxyUtils.getTopLevelPathSegment("test"));
        assertEquals("app", ModuleProxyUtils.getTopLevelPathSegment("test/app"));
        assertEquals("app", ModuleProxyUtils.getTopLevelPathSegment("test/app/stuff"));
    }
    
    public void testGetSuffix() throws Exception {
        assertEquals("ext", ModuleProxyUtils.getSuffix("file.ext"));
        assertEquals("ext", ModuleProxyUtils.getSuffix("/file.ext"));
        assertEquals("ext", ModuleProxyUtils.getSuffix("/a.file.in/file.ext"));
        assertEquals("ext", ModuleProxyUtils.getSuffix("/a.file.in/.ext"));
        assertEquals(null, ModuleProxyUtils.getSuffix("/a.file.in/"));
        assertEquals(null, ModuleProxyUtils.getSuffix("/a.file.in/noext"));
        assertEquals(null, ModuleProxyUtils.getSuffix("noext"));
    }

}
