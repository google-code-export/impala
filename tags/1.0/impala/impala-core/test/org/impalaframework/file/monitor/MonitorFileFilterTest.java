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

package org.impalaframework.file.monitor;

import java.io.File;

import junit.framework.TestCase;

import org.impalaframework.util.CollectionStringUtils;

public class MonitorFileFilterTest extends TestCase {

    public void testAcceptDefault() {
        MonitorFileFilter filter = newFilter("", "");
        assertTrue(filter.accept(new File("somefile.txt")));
        assertFalse(filter.accept(new File(".hidden")));
    }
    
    public void testIncludesOnly() throws Exception {
        MonitorFileFilter filter = newFilter("do,txt", "");
        assertTrue(filter.accept(new File("somefile.txt")));
        assertFalse(filter.accept(new File("somefile.other")));
        assertFalse(filter.accept(new File(".hidden")));
    }
    
    public void testExcludesOnly() throws Exception {
        MonitorFileFilter filter = newFilter("", "do,txt");
        assertFalse(filter.accept(new File("somefile.txt")));
        assertTrue(filter.accept(new File("somefile.other")));
        assertFalse(filter.accept(new File(".hidden")));
    }
    
    public void testConflictingIncludesExcludes() throws Exception {
        MonitorFileFilter filter = newFilter("exe,txt,css", "do,txt");
        assertTrue(filter.accept(new File("somefile.txt")));
        assertFalse(filter.accept(new File("somefile.other")));
        assertFalse(filter.accept(new File(".hidden")));
    }

    private MonitorFileFilter newFilter(String includes, String excludes) {
        MonitorFileFilter filter = new MonitorFileFilter(CollectionStringUtils.parseStringList(includes), CollectionStringUtils.parseStringList(excludes));
        return filter;
    }

}
