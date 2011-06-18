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

package org.impalaframework.radixtree;

import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

/**
 * Implementation of {@link RadixTree} which is protected by a java.util.Concurrent
 * read/write lock.
 * 
 * @author Phil Zoio
 */
public class ConcurrentRadixTree<T> implements RadixTree<T> {

    private final RadixTree<T> delegate;

    private final ReadLock readLock;

    private final WriteLock writeLock;

    public ConcurrentRadixTree(RadixTree<T> delegate) {
        super();
        this.delegate = delegate;
        
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();
    }
    
    public ConcurrentRadixTree() {
        this(new RadixTreeImpl<T>());
    }


    public void insert(String key, T value) throws DuplicateKeyException {
        try {
            writeLock.lock();
            delegate.insert(key, value);
        }
        finally {
            writeLock.unlock();
        }
    }

    public boolean delete(String key) {
        try {
            writeLock.lock();
            return delegate.delete(key);
        }
        finally {
            writeLock.unlock();
        }
    }


    public boolean contains(String key) {
        try {
            readLock.lock();
            return delegate.contains(key);
        }
        finally {
            readLock.unlock();
        }
    }

    public T find(String key) {
        try {
            readLock.lock();
            return delegate.find(key);
        }
        finally {
            readLock.unlock();
        }
    }

    public TreeNode<T> findContainedNode(String searchKey) {
        try {
            readLock.lock();
            return delegate.findContainedNode(searchKey);
        }
        finally {
            readLock.unlock();
        }
    }

    public T findContainedValue(String searchKey) {
        try {
            readLock.lock();
            return delegate.findContainedValue(searchKey);
        }
        finally {
            readLock.unlock();
        }
    }

    public long getSize() {
        try {
            readLock.lock();
            return delegate.getSize();
        }
        finally {
            readLock.unlock();
        }
    }
    
    public List<T> searchPrefix(String prefix, int recordLimit) {
        try {
            readLock.lock();
            return delegate.searchPrefix(prefix, recordLimit);
        }
        finally {
            readLock.unlock();
        }
    }

}
