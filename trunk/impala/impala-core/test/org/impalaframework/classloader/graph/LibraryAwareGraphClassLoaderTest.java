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

package org.impalaframework.classloader.graph;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import junit.framework.TestCase;

import org.impalaframework.classloader.ClassRetriever;
import org.impalaframework.module.ModuleDefinition;
import org.springframework.util.ClassUtils;

public class LibraryAwareGraphClassLoaderTest extends TestCase {
    
    public void testMaybeLoadExternalClassNull() {
        ClassRetriever moduleResourceRetriever = null;
        LibraryAwareGraphClassLoader cl = newClassLoader(moduleResourceRetriever);
        cl.maybeFindLibraryClassLocally("myclass");
    }
    
    public void testMaybeLoadExternalClassNonEmpty() {
        ClassRetriever moduleResourceRetriever = createMock(ClassRetriever.class);
        expect(moduleResourceRetriever.getClassBytes("myclass")).andReturn(null);
        
        replay(moduleResourceRetriever);
        LibraryAwareGraphClassLoader cl = newClassLoader(moduleResourceRetriever);
        cl.maybeFindLibraryClassLocally("myclass");
        verify(moduleResourceRetriever);
    }

    private LibraryAwareGraphClassLoader newClassLoader(
            ClassRetriever internalJarRetriever) {
        ClassLoader defaultClassLoader = ClassUtils.getDefaultClassLoader();
        ModuleDefinition definition = null;
        ClassLoaderOptions loadParentFirst = new ClassLoaderOptions(true, true, true);
        DelegateClassLoader delegateClassLoader = null;
        ClassRetriever moduleResourceRetriever = null;
        LibraryAwareGraphClassLoader cl = new LibraryAwareGraphClassLoader(defaultClassLoader, delegateClassLoader, moduleResourceRetriever, internalJarRetriever, definition, loadParentFirst);
        return cl;
    }

}
