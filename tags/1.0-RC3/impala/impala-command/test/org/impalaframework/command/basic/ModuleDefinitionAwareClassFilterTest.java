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
import java.io.IOException;

import junit.framework.TestCase;

import org.impalaframework.exception.ConfigurationException;

public class ModuleDefinitionAwareClassFilterTest extends TestCase {

    public final void testAccept() throws IOException {
        
        final ModuleDefinitionAwareClassFilter filter = new ModuleDefinitionAwareClassFilter();
        
        try {
            filter.accept(new File("test/org/impalaframework/command/basic/SpecAwareClass.class"));
            fail();
        }
        catch (ConfigurationException e) {
            assertEquals("root canonical path not set", e.getMessage());
        }
        
        filter.setRootPath(new File("test"));

        //directory is true
        assertTrue(filter.accept(new File("../impala-interactive/test/org")));
        
        //should find this one
        assertTrue(filter.accept(new File("test/org/impalaframework/command/basic/SpecAwareClass.class")));

        //won't work for interface
        assertFalse(filter.accept(new File("test/org/impalaframework/command/basic/SpecAwareInterface.class")));
        assertFalse(filter.accept(new File("test/org/impalaframework/command/basic/AbstractSpecAwareClass.class")));
        
        assertFalse(filter.accept(new File("test/org/impalaframework/command/basic/SpecAwareClass.java")));
        assertFalse(filter.accept(new File("nonexistentfile.class")));
        assertFalse(filter.accept(new File("test/org/impalaframework/command/basic/ModuleDefinitionAwareClassFilterTest.java")));
    }

}
