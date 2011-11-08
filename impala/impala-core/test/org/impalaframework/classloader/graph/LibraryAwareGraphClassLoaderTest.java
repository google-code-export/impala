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

import java.io.File;

import junit.framework.TestCase;

import org.impalaframework.classloader.ClassRetriever;
import org.impalaframework.classloader.URLClassRetriever;
import org.impalaframework.module.ModuleDefinition;
import org.springframework.util.ClassUtils;

public class LibraryAwareGraphClassLoaderTest extends TestCase {
    
    private boolean internalLoad = false;
    
    public void testMaybeLoadExternalClassNull() {
        ClassRetriever moduleResourceRetriever = null;
        LibraryAwareGraphClassLoader cl = newClassLoader(moduleResourceRetriever, true);
        cl.maybeFindLibraryClassLocally("myclass", internalLoad);
    }
    
    public void testMaybeLoadExternalClassNonEmpty() {
        ClassRetriever moduleResourceRetriever = createMock(ClassRetriever.class);
        expect(moduleResourceRetriever.getClassBytes("myclass")).andReturn(null);
        
        replay(moduleResourceRetriever);
        LibraryAwareGraphClassLoader cl = newClassLoader(moduleResourceRetriever, true);
        cl.maybeFindLibraryClassLocally("myclass", internalLoad);
        verify(moduleResourceRetriever);
    }

    private LibraryAwareGraphClassLoader newClassLoader(
            ClassRetriever moduleLibraryRetriever, 
            boolean loadLibraryResources) {
        ClassLoader defaultClassLoader = ClassUtils.getDefaultClassLoader();
        ModuleDefinition definition = null;
        ClassLoaderOptions loadParentFirst = new ClassLoaderOptions(true, true, true, true);
        DelegateClassLoader delegateClassLoader = null;
        ClassRetriever moduleResourceRetriever = new URLClassRetriever(new File[0]);
        LibraryAwareGraphClassLoader cl = new LibraryAwareGraphClassLoader(defaultClassLoader, delegateClassLoader, moduleResourceRetriever, moduleLibraryRetriever, definition, loadParentFirst);
        return cl;
    }
    
    public void testFindLibraryResources() throws Exception {
        ClassRetriever libraryRetriever = new URLClassRetriever(new File[]{new File("../sample-module3/lib/ant-launcher-1.7.0.jar")});
        LibraryAwareGraphClassLoader cl = newClassLoader(libraryRetriever, true);
        assertNotNull(cl.getLocalResource("org/apache/tools/ant/launch/Locator.class"));
    }
    
    public void testNotFindResources() throws Exception {
        ClassRetriever libraryRetriever = new URLClassRetriever(new File[]{new File("../sample-module3/lib/ant-launcher-1.7.0.jar")});
        LibraryAwareGraphClassLoader cl = newClassLoader(libraryRetriever, false);
        assertNotNull(cl.getLocalResource("org/apache/tools/ant/launch/Locator.class"));
    }

}
