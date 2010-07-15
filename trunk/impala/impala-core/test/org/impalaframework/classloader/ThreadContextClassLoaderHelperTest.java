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

package org.impalaframework.classloader;

import junit.framework.TestCase;

public class ThreadContextClassLoaderHelperTest extends TestCase {

    public void testFindResourceFromDirectory() {
        
        final ClassLoader startClassLoader = Thread.currentThread().getContextClassLoader();
        try {
        
            final Object[] holder = new Object[1];
            
            ClassLoader classLoader = new ClassLoader() {
            };
            
            final ClassLoader initialClassLoader = this.getClass().getClassLoader();
            Thread.currentThread().setContextClassLoader(initialClassLoader);
            
            ThreadContextClassLoaderHelper.doInClassLoader(classLoader, new ClassLoaderCallback() {
                
                public Object perform() {
                    holder[0] = Thread.currentThread().getContextClassLoader();
                    return null;
                }
            });
            
            assertSame(initialClassLoader, Thread.currentThread().getContextClassLoader());
            assertSame(classLoader, holder[0]);
            
        } finally {
            Thread.currentThread().setContextClassLoader(startClassLoader);
        }
    }

}
