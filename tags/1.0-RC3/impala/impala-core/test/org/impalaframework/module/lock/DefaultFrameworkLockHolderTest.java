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

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

public class DefaultFrameworkLockHolderTest extends TestCase {
    
    private DefaultFrameworkLockHolder lockHolder;
    private AssertionFailedError error;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        lockHolder = new DefaultFrameworkLockHolder();
    }

    public void testLocking() throws Exception {
        assertFalse(lockHolder.hasLock());
        assertTrue(lockHolder.isAvailable());
        
        lockHolder.writeLock();
        assertTrue(lockHolder.hasLock());
        assertTrue(lockHolder.isAvailable());
        
        lockHolder.readLock();
        lockHolder.readUnlock();
        
        lockHolder.writeUnlock();
        assertFalse(lockHolder.hasLock());
        assertTrue(lockHolder.isAvailable());
    }
    
    public void testMultiThread() throws InterruptedException {
        lockHolder.writeLock();
        Thread t = new Thread(new Runnable(){

            public void run() {
                try {
                    assertFalse(lockHolder.isAvailable());
                } catch (AssertionFailedError e) {
                    error = e;
                }
            }
            
        });
        t.start();
        t.join();
        
        assertNull(error);
    }

    /**
     * This will not hang
     */
    public void testReadThenReadLock() throws Exception {
        lockHolder.readLock();
        Thread t = new Thread(new Runnable(){

            public void run() {
                lockHolder.readLock();
            }
            
        });
        t.start();
        t.join();
    }
    
    /**
     * Run this manually as it hangs
     */
    public void _testReadThenWriteLock() throws Exception {
        lockHolder.readLock();
        Thread t = new Thread(new Runnable(){

            public void run() {
                System.out.println("About to hang trying to get write lock");
                lockHolder.writeLock();
            }
            
        });
        t.start();
        t.join();
    }

    /**
     * Run this manually as it hangs
     */
    public void _testWriteThenReadLock() throws Exception {
        lockHolder.writeLock();
        Thread t = new Thread(new Runnable(){

            public void run() {
                System.out.println("About to hang trying to get read lock");
                lockHolder.readLock();
            }
            
        });
        t.start();
        t.join();
    }
    
}
