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

package org.impalaframework.service.contribution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of a map which contains both local contributions as well as
 * contributions from another map (the external contributions map), typically
 * from the service registry. Read operations will cascade from the local to the
 * external contributions map. For example, <code>get</code> will first look
 * in the local map, then the external map. <code>size()</code> will count the
 * entries of both maps.
 * <p>
 * Write operations operate only on the local contributions. For example, if
 * <code>remove</code> is called, and the key is present only in the external
 * contributions map, the key will <b>not</b> be removed!
 */ 
public class ContributionMap<V extends Object> implements Map<String,V> {
    
    private Map<String,V> localContributions = new ConcurrentHashMap<String,V>();
    private BaseServiceRegistryMap<V> externalContributions;
    
    public void clear() {
        this.localContributions.clear();
    }

    public boolean containsKey(Object key) {
        boolean hasKey = this.localContributions.containsKey(key);
        if (!hasKey) hasKey = this.externalContributions.containsKey(key);
        return hasKey;
    }

    public boolean containsValue(Object value) {
        boolean hasValue = this.localContributions.containsValue(value);
        if (!hasValue) hasValue = this.externalContributions.containsValue(value);
        return hasValue;
    }

    public Set<Map.Entry<String,V>> entrySet() {
        Set<Map.Entry<String,V>> localSet = this.localContributions.entrySet();
        Set<Map.Entry<String,V>> externalSet = this.externalContributions.entrySet();
        Set<Map.Entry<String,V>> allSet = new LinkedHashSet<Map.Entry<String,V>>();
        allSet.addAll(localSet);
        allSet.addAll(externalSet);
        return allSet;
    }

    public V get(Object key) {
        V value = this.localContributions.get(key);
        if (value == null) {
            value = this.externalContributions.get(key);
        }
        return value;
    }

    public boolean isEmpty() {
        boolean isEmpty = this.localContributions.isEmpty();
        if (isEmpty) {
            isEmpty = this.externalContributions.isEmpty();
        }
        return isEmpty;
    }

    public Set<String> keySet() {
        Set<String> localSet = this.localContributions.keySet();
        Set<String> externalSet = this.externalContributions.keySet();
        Set<String> allSet = new LinkedHashSet<String>();
        allSet.addAll(localSet);
        allSet.addAll(externalSet);
        return allSet;
    }

    public V put(String key, V value) {
        return this.localContributions.put(key, value);
    }

    public void putAll(Map<? extends String,? extends V> map) {
        this.localContributions.putAll(map);
    }

    public V remove(Object key) {
        return this.localContributions.remove(key);
    }

    public int size() {
        int localSize = this.localContributions.size();
        int externalSize = this.externalContributions.size();
        return localSize + externalSize;
    }

    public Collection<V> values() {
        Collection<V> localValues = this.localContributions.values();
        Collection<V> externalValues = this.externalContributions.values();
        Collection<V> allValues = new ArrayList<V>();
        allValues.addAll(localValues);
        allValues.addAll(externalValues);
        return allValues;
    }
    
    public void setExternalContributions(BaseServiceRegistryMap<V> externalContributions) {
        this.externalContributions = externalContributions;
    }

    public void setLocalContributions(Map<String,V> localContributions) {
        this.localContributions = localContributions;
    }
    
    @Override
    public java.lang.String toString() {
        StringBuffer sb = new StringBuffer();
        String localString = (String) localContributions.toString();
        String externalString = (String) externalContributions.toString();
        sb.append("Local contributions: ").append(localString).append(", external contributions: ").append(externalString);
        return sb.toString();
    }

}
