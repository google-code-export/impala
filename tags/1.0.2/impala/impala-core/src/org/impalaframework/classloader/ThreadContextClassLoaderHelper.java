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

/**
 * Simplifies the mechanism for executing some code within a modified thread context class loader
 * @see Thread#getContextClassLoader()
 * @author Phil Zoio
 */
public class ThreadContextClassLoaderHelper {

    public static Object doInClassLoader(ClassLoader classLoader, ClassLoaderCallback callback) {
        
        final Thread currentThread = Thread.currentThread();
        ClassLoader existingClassLoader = currentThread.getContextClassLoader();
        
        try {
            currentThread.setContextClassLoader(classLoader);
            return callback.perform();
        }
        finally {
            currentThread.setContextClassLoader(existingClassLoader);
        }
    }
    
}
