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
 * Interface encapsulating public methods shared by module class loader
 * implementations. Note that implementations of this interface will typically
 * extend {@link ClassLoader}, although this is not necessarily the case.
 * 
 * @author Phil Zoio
 */
public interface ModularClassLoader {
    
    /**
     * Defines functionality for loading a named class. Implementations may 
     * throw {@link ClassNotFoundException} if class is not found, although
     * this does not necessarily apply.
     */
    public Class<?> loadClass(String className) throws ClassNotFoundException;
    
    /**
     * Returns true if <code>parentClassLoader</code> is visible from
     * the current {@link ModularClassLoader} instance.
     */
    public boolean hasVisibilityOf(ClassLoader parentClassLoader);
}
