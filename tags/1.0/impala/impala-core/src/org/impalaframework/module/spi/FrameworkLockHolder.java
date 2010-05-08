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

package org.impalaframework.module.spi;


/**
 * Functionality for coarse grained framework locking, which is invoked when
 * module reload operations are invoked.
 * 
 * @author Phil Zoio
 */
public interface FrameworkLockHolder {
        
    /**
     * Locks framework for writing. Invoked when module operations are invoked
     */
    void writeLock();
    
    /**
     * Releases write lock. Invoked when module operations are completed
     */
    void writeUnlock();
    
    /**
     * Obtains read lock, which prevents framework from handling out a write lock until 
     * this lock is release
     */
    void readLock();
    
    /**
     * Releases read lock. This allows write lock to be obtained
     */
    void readUnlock();
    
    /**
     * Returns true if framework operations is not write locked by another thread. 
     */
    boolean isAvailable();
    
}
