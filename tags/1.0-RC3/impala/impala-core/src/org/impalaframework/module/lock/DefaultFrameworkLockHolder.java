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

package org.impalaframework.module.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.impalaframework.module.spi.FrameworkLockHolder;

/**
 * Implements {@link FrameworkLockHolder} using the {@link ReentrantLock} class from the Java Concurrency API.
 * @author Phil Zoio
 */
public class DefaultFrameworkLockHolder implements FrameworkLockHolder {

    private static Log logger = LogFactory.getLog(DefaultFrameworkLockHolder.class);
    
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();

    public DefaultFrameworkLockHolder() {
        super();
    }
    
    public void writeLock() {
        this.w.lock();
    }
    
    public void writeUnlock() {
        this.w.unlock();
    }
    
    public void readLock() {
        this.r.lock();
    }
    
    public void readUnlock() {
        this.r.unlock();
    }
    
    /**
     * Returns false if write lock is taken by another thread. Otherwise returns true.
     */
    public boolean isAvailable() {
        
        if (this.rwl.isWriteLocked()) {
            if (!this.rwl.isWriteLockedByCurrentThread()) {
            
                if (logger.isDebugEnabled()) {
                    logger.debug("Module is unavailable with hold count of " + rwl.getWriteHoldCount() + " but not held by current thread");
                }
                return false;
            }
            return true;
        }
        return true;
    }

    /**
     * Returns true if write lock is currently held by current thread. Otherwise returns false
     */
    public boolean hasLock() {
        return this.rwl.isWriteLockedByCurrentThread();
    }

}
