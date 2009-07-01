/*
 * Copyright 2007-2008 the original author or authors.
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

package org.impalaframework.web.integration;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.impalaframework.exception.InvalidStateException;
import org.impalaframework.radixtree.ConcurrentRadixTree;
import org.springframework.util.Assert;

/**
 * Holds a {@link ConcurrentRadixTree}, used to resolve URL prefixes to modules.
 * Also holds a mapping of modules to contributed prefix keys, so that if module is unloaded, 
 * relevant keys can be removed.
 * @author Phil Zoio
 */
public class PrefixTreeHolder {
    
    private ConcurrentRadixTree<String> trie = new ConcurrentRadixTree<String>();
    
    private Map<String,List<String>> contributions = new ConcurrentHashMap<String, List<String>>();
    
    /**
     * Used to add prefix key to module name mapping. This will be called zero to n times 
     * for each module which is interested in receiving URLs
     */
    public void add(String moduleName, String key) {
        
        Assert.notNull(moduleName, "moduleName cannot be null");
        Assert.notNull(key, "key cannot be null");
        
        
        if (trie.contains(key)) {
            String value = trie.findContainedValue(key);
            throw new InvalidStateException("Module '" + moduleName + "' cannot use key '" + key + "', as it is already being used by module '" + value + "'");
        }
        
        trie.insert(key, moduleName);
        List<String> list = contributions.get(moduleName);
        if (list == null) {
            list = new LinkedList<String>();
            contributions.put(moduleName, list);
        }
        
        list.add(key);
    }
    
    /**
     * Called when module is unloaded to remove all the prefix keys to module name associations
     * contributed by the module concerned
     */
    public int unloadForModule(String moduleName) {
        
        int unloaded = 0;
        
        List<String> list = contributions.get(moduleName);
        if (list != null) {
            for (String key : list) {
                trie.delete(key);
                unloaded++;
            }
        }
        
        return unloaded;
    }
    
}
